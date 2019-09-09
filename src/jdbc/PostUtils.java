package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PostUtils {
	
	public static DecimalFormat df = new DecimalFormat("0.00");
	public static final String NEW_LINE_REP = "``nl``";
	public static final String CARRIAGE_RETURN_REP = "``cr``";
	
	public static boolean validatePost(Connection conn, int post_id) throws SQLException {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		int dbPost_id = -1;
		Timestamp dbClose_time = null;
		double current_price = 0;
		double secret_minimum_price = 0;
		String current_buyer = null;
		
		String sql = "SELECT post_id, close_time, current_price, secret_minimum_price, current_buyer_id FROM Auction_Post WHERE post_id = ?";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, post_id);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			dbPost_id = rs.getInt("post_id");
			dbClose_time = rs.getTimestamp("close_time");
			current_price = rs.getDouble("current_price");
			secret_minimum_price = rs.getDouble("secret_minimum_price");
			current_buyer = rs.getString("current_buyer_id");
		}
		
		if(post_id == dbPost_id) {
			if(dbClose_time != null) {
				boolean flag = currentTime.getTime() < dbClose_time.getTime();
				
				if(!flag) {
					//Auction is over
					if(current_price < secret_minimum_price) {
						//No winner
						current_buyer = null;
					}
				}
				
				//Update active value
				String sql_update = "update Auction_Post set active = ?, current_buyer_id = ? where post_id = ?";
				
				PreparedStatement ps_update = conn.prepareStatement(sql_update);
				ps_update.setBoolean(1, flag);
				ps_update.setString(2, current_buyer);
				ps_update.setInt(3, post_id);
				ps_update.executeUpdate();
				
				return flag;
			} else {
				System.out.println("Error: [null close_time]: " + dbPost_id + " " + dbClose_time);
			}
		} else {
			System.out.println("Error: [invalid post_id]");
		}
		
		return false;
	}
	
	private static boolean handle_auto_bid(Connection conn, String data[], int post_id, double dbCurrent_price, double dbBid_incrementer, String dbCurrent_buyer) throws SQLException {
		String current_user_id = data[1];
		
		//Exit if the current buyer is same as the auto-bid current buyer
		if(current_user_id.equals(dbCurrent_buyer)) {
			return false;
		}
		
		//Check to see if this would go over auto-bid upper limit
		if(dbCurrent_price + dbBid_incrementer > Double.valueOf(data[2])) {
			return false;
		}
		
		//Update auction post table
		String sql = "UPDATE Auction_Post SET current_price = ?, current_buyer_id = ? WHERE post_id = ?";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setDouble(1, dbCurrent_price + dbBid_incrementer);
		ps.setString(2, current_user_id);
		ps.setInt(3, post_id);
		ps.executeUpdate();
		
		//Update bid table
		sql = "INSERT INTO Bid(post_id, user_id, amount) VALUES(?, ?, ?)";
		
		ps = conn.prepareStatement(sql);
		ps.setInt(1, post_id);
		ps.setString(2, current_user_id);
		ps.setDouble(3, dbBid_incrementer);
		ps.executeUpdate();
		
		return true;
	}
	
	public static void update_auto_bids(Connection conn, int post_id) throws SQLException {
		ArrayList<String[]> auto_bid_users = new ArrayList<String[]>();
		
		String sql = "SELECT * FROM Auto_Bid WHERE post_id = ?";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, post_id);
		
		ResultSet rs = ps.executeQuery();;
		final int AUTO_BID_ATTR_COUNT = 3;
		
		while(rs.next()) {
			String data[] = new String[AUTO_BID_ATTR_COUNT];
			for(int i = 0; i < AUTO_BID_ATTR_COUNT; i++) {
				data[i] = rs.getString(i + 1);
			}
			auto_bid_users.add(data);
		}
		
		if(auto_bid_users.size() == 0) {
			System.out.println("No Auto-Bidders Left.");
			return;
		}
		
		double dbCurrent_price = 0;
		double dbBid_incrementer = 0;
		String dbCurrent_buyer = null;
		
		sql = "SELECT * FROM Auction_Post WHERE post_id = ?";
		
		ps = conn.prepareStatement(sql);
		ps.setInt(1, post_id);
		
		rs = ps.executeQuery();
		
		while(rs.next()) {
			dbCurrent_price = rs.getDouble("current_price");
			dbBid_incrementer = rs.getDouble("bid_incrementer");
			dbCurrent_buyer = rs.getString("current_buyer_id");
		}
		
		int auto_bid_count = 0;
		
		for(String[] data : auto_bid_users) {
			if(handle_auto_bid(conn, data, post_id, dbCurrent_price, dbBid_incrementer, dbCurrent_buyer)) {
				auto_bid_count++;
				dbCurrent_price += dbBid_incrementer;
			}
		}
		
		if(auto_bid_count > 0) {
			//Update all auto-bids
			update_auto_bids(conn, post_id);
		}
	}
	
	public static String gen_json(ArrayList<String> k, ArrayList<String> v) {
		String result = "";
		
		result += "{ ";
		
		for(int i = 0; i < k.size(); i++) {
			String key = k.get(i);
			String value = v.get(i);
			
			result += "\"" + key + "\"";
			result += ": ";
			
			if(!(value.contains("[") && value.contains("]"))) {
				result += "\"" + value;
				result += "\"" + (i == k.size() - 1 ? " " : ", ");
			} else {
				result += value;
				result += (i == k.size() - 1 ? " " : ", ");
			}
		}
		
		result += " }";
		
		return result;
	}
	
	public static void addTuple(ArrayList<String> keys, ArrayList<String> values, Object k, Object v) {
		keys.add(k.toString());
		values.add(v.toString());
	}

}
