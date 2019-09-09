package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/AuctionPost")
public class AuctionPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			
			int post_id = Integer.valueOf(request.getParameter("post_id"));
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			//Validate Post
			boolean isActive = PostUtils.validatePost(conn, post_id);
			
			session.setAttribute("post_id", post_id);
			session.setAttribute("active", isActive);
			
			String auction_post_name = null;
			String seller_id = null;
			String auction_post_description = null;
			Timestamp open_time = null;
			Timestamp close_time = null;
			int item_id = 0;
			String item_name = null;
			String item_description = null;
			String item_condition = null;
			String item_category = null;;
			double current_price = 0;
			double bid_incrementer = 0;
			String current_buyer = null;
			
			String sql = "SELECT * FROM Auction_Post WHERE post_id = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, post_id);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				auction_post_name = rs.getString("name");
				seller_id = rs.getString("seller_id");
				auction_post_description = rs.getString("post_description");
				open_time = rs.getTimestamp("open_time");
				close_time = rs.getTimestamp("close_time");
				item_id = rs.getInt("item_id");
				current_price = rs.getDouble("current_price");
				bid_incrementer = rs.getDouble("bid_incrementer");
				current_buyer = rs.getString("current_buyer_id");
			}
			
			sql = "SELECT * FROM Item WHERE item_id = ?";
				
			ps = conn.prepareStatement(sql);
			ps.setInt(1, item_id);
			
			rs = ps.executeQuery();
			
			boolean flag = true;
			
			while(rs.next()) {
				item_name = rs.getString("item_name");
				item_description = rs.getString("description");
				item_condition = rs.getString("item_condition");
				item_category = rs.getString("item_category");
				flag = false;
			}
			
			response.setContentType("application/json");
			
			PrintWriter out = response.getWriter();
			
			String result = "{}";
			
			if(flag) {
				out.print(result);
				out.flush();
				conn.close();
				
				return;
			}
			
			ArrayList<String> bids = new ArrayList<String>();
			
			sql = "SELECT * FROM Bid WHERE post_id = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, post_id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int bid_id = rs.getInt("bid_id");
				int bid_post_id = rs.getInt("post_id");
				String user_id = rs.getString("user_id");
				double amount = rs.getDouble("amount");
				
				Bid bid = new Bid(bid_id, bid_post_id, user_id, amount);
				bids.add(bid.toJSON());
			}
			
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			
			
			PostUtils.addTuple(keys, values, "post_id", post_id);
			PostUtils.addTuple(keys, values, "auction_post_name", auction_post_name);
			PostUtils.addTuple(keys, values, "auction_post_seller", seller_id);
			PostUtils.addTuple(keys, values, "status", isActive ? "Active" : "Closed");
			PostUtils.addTuple(keys, values, "auction_post_description", auction_post_description);
			PostUtils.addTuple(keys, values, "open_time", open_time);
			PostUtils.addTuple(keys, values, "close_time", close_time);
			PostUtils.addTuple(keys, values, "item_name", item_name);
			PostUtils.addTuple(keys, values, "item_description", item_description);
			PostUtils.addTuple(keys, values, "item_condition", item_condition);
			PostUtils.addTuple(keys, values, "item_category", item_category);
			PostUtils.addTuple(keys, values, "current_price", current_price);
			PostUtils.addTuple(keys, values, "bid_incrementer", bid_incrementer);
			PostUtils.addTuple(keys, values, "current_buyer", current_buyer != null ? current_buyer : "None");
			
			PostUtils.addTuple(keys, values, "bids", bids.toString());
			
			result = PostUtils.gen_json(keys, values);
			
			out.print(result);
			out.flush();
			
			conn.close();
			
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("[AuctionPost.java]");
		try {
			HttpSession session = request.getSession();
			
			int post_id = -1;
			
			if(request.getParameter("post_id") != "" && request.getParameter("post_id") != null) {
				post_id = Integer.valueOf(request.getParameter("post_id"));
			}
			
			String user_id = (String) request.getSession().getAttribute("username");
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			//Validate Post
			boolean isActive = PostUtils.validatePost(conn, post_id);
			
			session.setAttribute("post_id", post_id);
			session.setAttribute("active", isActive);
			
			Object val = request.getAttribute("notification");
			
			session.setAttribute("notification", val != null ? val : "");
			
			if(post_id >= 0) {
				String sql = "SELECT * FROM Auto_Bid WHERE post_id = ? AND user_id = ?";
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, post_id);
				ps.setString(2, user_id);
				
				ResultSet rs = ps.executeQuery();
				
				boolean enabled = false;
				
				while(rs.next()) {
					enabled = rs.getInt("post_id") == post_id && rs.getString("user_id").equals(user_id);
				}
				
				session.setAttribute("enabled", enabled);
			}
			
			response.sendRedirect("post.jsp");
			
			conn.close();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
