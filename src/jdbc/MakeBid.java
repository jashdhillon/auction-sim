package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;


@WebServlet("/MakeBid")
public class MakeBid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("[MakeBid.java]");
		try {
			int post_id = Integer.valueOf(request.getParameter("post_id")).intValue();
			String user_id = request.getParameter("user_id");
			double amount = Double.valueOf(request.getParameter("amount")).doubleValue();
			String prev_buyer = new String();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			//Validate Post
			boolean isActive = PostUtils.validatePost(conn, post_id);
			boolean canProccess = true;
			
			//Return if the post is inactive
			if(!isActive) {
				//Failure State: Auction post is not active
				System.out.println("Invalid Bid: Auction Post is not active");
				canProccess = false;
			} else {
				double dbCurrent_price = 0;
				double dbBid_incrementer = 0;
				
				String sql = "SELECT current_price, bid_incrementer, current_buyer_id FROM Auction_Post WHERE post_id = ?";
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, post_id);
				
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					dbCurrent_price = rs.getDouble("current_price");
					dbBid_incrementer = rs.getDouble("bid_incrementer");
					prev_buyer = rs.getString("current_buyer_id");
					if(rs.wasNull()){
						prev_buyer="";
					}
				}
				
				if(amount <= 0) {
					//Failure State: Amount must be greater than 0
					System.out.println("Invalid Bid: Amount must be greater than 0");
					canProccess = false;
				} else if(amount < dbBid_incrementer) {
					//Failure State: Amount must be greater than the min bid increment
					System.out.println("Invalid Bid: Amount must be greater than min bid increment");
					canProccess = false;
				}
				
				if(canProccess) {
					System.out.println("Valid Bid");
					//Bid is valid, so add entry to Bid table with bid_id, post_id and user_id and update Auction_Post table's post with new current_price and current_buyer
					sql = "UPDATE Auction_Post SET current_price = ?, current_buyer_id = ? WHERE post_id = ?";
					
					ps = conn.prepareStatement(sql);
					ps.setDouble(1, dbCurrent_price + amount);
					ps.setString(2, user_id);
					ps.setInt(3, post_id);
					ps.executeUpdate();
					
					sql = "INSERT INTO Bid(post_id, user_id, amount) VALUES(?, ?, ?)";
					
					ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, post_id);
					ps.setString(2, user_id);
					ps.setDouble(3, amount);
					ps.executeUpdate();
					
					//Update all auto-bids'
					System.out.println("Updating Auto-Bids");
					PostUtils.update_auto_bids(conn, post_id);

					if(!prev_buyer.equals(""))
					{
						String insert = "INSERT INTO Inbox(username, message, status, link, time) VALUES (?,?,1,?,?);";
						String msg = "A higher bid has been placed!";
						String link = "AuctionPost?post_id="+post_id;
						Timestamp time = new Timestamp(System.currentTimeMillis());
						ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1,prev_buyer);
						ps.setString(2,msg);
						ps.setString(3,link);
						ps.setTimestamp(4,time);
						ps.executeUpdate();
					}

					String checkAutoBid = "SELECT * FROM Auto_Bid";
					int ab_postid;
					double newAmnt = dbCurrent_price + amount;
					double maxBid;
					ps = conn.prepareStatement(checkAutoBid);
					rs = ps.executeQuery();

					while(rs.next()){
						ab_postid = rs.getInt("post_id");
						maxBid = rs.getDouble("max_bid");
						if(ab_postid==post_id && newAmnt > maxBid){
							String insert2 = "INSERT INTO Inbox(username, message, status, link, time) VALUES (?,?,1,?,?);";
							String msg2 = "Your max bid has been reached!";
							String link2 = "AuctionPost?post_id="+post_id;
							Timestamp time2 = new Timestamp(System.currentTimeMillis());
							PreparedStatement ps2 = conn.prepareStatement(insert2, Statement.RETURN_GENERATED_KEYS);
							ps2.setString(1,prev_buyer);
							ps2.setString(2,msg2);
							ps2.setString(3,link2);
							ps2.setTimestamp(4,time2);
							ps2.executeUpdate();
						}
					}
				}
			}
			
//			request.setAttribute("lastbid", !isActive ? -1.0 : (!canProccess ? -2.0 : amount));
			request.setAttribute("notification", 
					!isActive ? "Cannot Procces Request: Selected Auction Post is not Active" 
					: (!canProccess 
					? "Cannot Proccess Request: Invalid Bid(Must be greater than or equal to minimum bid increment)" 
					: "Successfully Submitted Bid for $" + PostUtils.df.format(amount)));
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AuctionPost");
			dispatcher.forward(request, response);
			
			conn.close();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
