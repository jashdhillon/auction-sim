package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


@WebServlet("/AutoBid")
public class AutoBid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("[MakeBid.java]");
		try {
			int post_id = Integer.valueOf(request.getParameter("post_id"));
			String user_id = request.getParameter("user_id");
			
			Object max_bidObj = request.getParameter("max_bid");
			Object enabledObj = request.getParameter("enabled");
			
			double max_bid = max_bidObj == null ? 0.0 : Double.valueOf(request.getParameter("max_bid"));
			boolean enabled = enabledObj == null ? true : Boolean.valueOf(request.getParameter("enabled"));
			
			System.out.println(enabled + " " + request.getParameter("enabled"));
			
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
				if(enabled) {
					//Enable Auto-Bidding
					double dbCurrent_price = 0;
					
					String sql = "SELECT current_price FROM Auction_Post WHERE post_id = ?";
					
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, post_id);
					
					ResultSet rs = ps.executeQuery();
					
					while(rs.next()) {
						dbCurrent_price = rs.getDouble("current_price");
					}
					
					if(max_bid <= 0) {
						//Failure State: Amount must be greater than 0
						System.out.println("Invalid Bid: Amount must be greater than 0");
						canProccess = false;
					} else if(max_bid <= dbCurrent_price) {
						//Failure State: Amount must be greater than the min bid increment
						System.out.println("Invalid Bid: Amount must be greater than min bid increment");
						canProccess = false;
					}
					
					if(canProccess) {
						System.out.println("Valid Bid");
						//AutoBid is valid, so add entry to Auto_Bid table with post_id, user_id and max_bid
						sql = "INSERT INTO Auto_Bid VALUES(?, ?, ?)";
						
						ps = conn.prepareStatement(sql);
						ps.setInt(1, post_id);
						ps.setString(2, user_id);
						ps.setDouble(3, max_bid);
						
						//TODO: Fix lazy dupe handling
						try {
							ps.executeUpdate();
						} catch(MySQLIntegrityConstraintViolationException e) {
							canProccess = false;
						}
						
						//Update all auto-bids'
						System.out.println("Updating Auto-Bids");
						PostUtils.update_auto_bids(conn, post_id);
					}
				} else {
					//Disable Auto-Bidding
					String sql = "DELETE FROM Auto_Bid WHERE post_id = ? AND user_id = ?";
					
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, post_id);
					ps.setString(2, user_id);
					ps.executeUpdate();
					
					System.out.println("Auto-Bid Removed");
				}
			}
			
			request.setAttribute("notification", 
					!isActive ? "Cannot Procces Request: Selected Auction Post is not Active" 
					: (!canProccess 
					? "Cannot Proccess Request: Invalid Max Threshold(Threashold must be greater than current price)" 
					: (enabled
					? "Successfully Enabled Auto-Bidding with a Max Threshold of $" + PostUtils.df.format(max_bid)
					: "Successfully Disabled Auto-Bidding")));
			
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
