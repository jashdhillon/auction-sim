package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/NewPost")
public class NewPost extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	     
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			//post variables
			HttpSession session = request.getSession();
			int days= Integer.parseInt(request.getParameter("days"));
			int hours= Integer.parseInt(request.getParameter("hours"));
			int minutes= Integer.parseInt(request.getParameter("minutes"));
			String postname= request.getParameter("post_name");
			String postdescription= request.getParameter("post_description");
			String Initial_Price= request.getParameter("inital_price");
			String cat1= request.getParameter("cat1");
			String cat2= request.getParameter("cat2");
			String cat3= request.getParameter("cat3");
			
			String Minprice =request.getParameter("secret_minimum_price");
			if(Minprice=="") {
			Minprice=Initial_Price;
			System.out.print(Minprice);
			}
			String bidincrementer= request.getParameter("bid_incrementer");
			String username = (String) session.getAttribute("username").toString();
			double startprice =Double.parseDouble(Minprice)-Double.parseDouble(bidincrementer);
			//item variables
			String name = request.getParameter("name");	//item name'
			String description  = request.getParameter("description");
			String condition = request.getParameter("condition");
			String category = request.getParameter("category");
			
			int item_id = -1;
			int post_id = -1;
			
			String sql = "INSERT INTO Item(item_name, description, item_condition, item_category,cat_1,cat_2,cat_3) VALUES(?, ?, ?, ?, ?, ?, ?);";
			String postsql = "INSERT INTO Auction_Post(open_time, close_time, name, item_id, seller_id, active, post_description, initial_price, current_price, secret_minimum_price, bid_incrementer) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement postps = conn.prepareStatement(postsql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, name);
			ps.setString(2, description);
			ps.setString(3, condition);
			ps.setString(4, category);
			ps.setString(5, cat1);
			ps.setString(6, cat2);
			ps.setString(7, cat3);
			
			ps.executeUpdate();	//creates item post
			
			ResultSet rs = ps.getGeneratedKeys();
			
			while(rs.next()) {
				item_id = rs.getInt(1);
			}
			
			Timestamp opentime = new Timestamp(System.currentTimeMillis());
			//days, hours. minutes
			Timestamp closetime=opentime;
			closetime.setTime(opentime.getTime() + (((days*24*60*60)+(hours*60*60)+(minutes * 60))* 1000));
			
			postps.setTimestamp(1, opentime);
			postps.setTimestamp(2, closetime);
			postps.setString(3, postname);
			postps.setInt(4, item_id);
			postps.setString(5, username);
			postps.setBoolean(6, true);
			postps.setString(7, postdescription);
			postps.setString(8, Initial_Price);
			postps.setDouble(9, startprice);
			postps.setString(10, Minprice);
			postps.setString(11, bidincrementer);
			
			postps.executeUpdate();	//creates item post
			
			rs = postps.getGeneratedKeys();
			
			while(rs.next()) {
				post_id = rs.getInt(1);
			}
			
			response.sendRedirect("AuctionPost?post_id=" + post_id);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}