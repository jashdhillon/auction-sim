package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AuctionList")
public class AuctionList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");	
			PrintWriter out = response.getWriter();
			int post_id = 0;
			String name = new String();
			String desc = new String();
			double cPrice = 0.0;
			String cat = request.getParameter("category");
			boolean active = false;
			String filter = request.getParameter("Filter");
			String searchtext = "%"+request.getParameter("searchtext")+"%";
			String sql = new String();
			String subcat = new String();

			String nofilter = "post_description";
			if(filter.equals("Item")){
				filter="name";
			}
			else if(filter.equals("Description")){
				filter="post_description";
			}
			else if(filter.equals("Category")){
				filter="item_category";
			}else{
				filter = nofilter;
			}
		try
		{

			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");

			String sql2 = "SELECT DISTINCT item_category FROM Item_Category;";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ResultSet rs2 = ps2.executeQuery();

			out.println("<title>Auction List</title>");

			// Search Bar
			out.println("<form action=\"AuctionList\" name =\"searchbar\">"
						+"<input type=\"text\" name=\"searchtext\" placeholder=\"Search for an auction\">"
						+"<select name=\"Filter\">"
						+"<option value=\"Item\">Item</option>"
						+"<option value=\"Description\">Description</option>"
						+"<option value=\"Category\">Category</option>"
						+"<input type=\"hidden\" name=\"category\" value=\"All Categories\">"
						+"</select>"
						+"<input type=\"submit\" value=\"Search\">"
						+"</form>");
			
			out.println("<form action=\"AuctionList\" name=\"Category\">"
						+"<select name=\"category\">"
						+"<option value=\"All Categories\">All Categories</option>");

			while(rs2.next()){
				subcat = rs2.getString("item_category");
				out.println("<option value=\""+subcat+"\">"+subcat+"</option>");
			}

			out.println("</select>"
						+"<input type=\"hidden\" name=\"Filter\" value=\"nofilter\">"
						+"<input type=\"hidden\" name=\"searchtext\" value=\"\">"
						+"<input type=\"submit\" value=\"Shop By Category\">"
						+"</form>");

			ps2.close();
			rs2.close();

			// Auction List Header
			out.println("<table border=\"1\">"
						+"<tr bgcolor=\"#d6d6d6\">"
						+"<td>Name</td>"
						+"<td>Description</td>"
						+"<td>Current Bidding Price</td>"
						+"<td>Category</td>"
						+"<td>Active</td></tr>");

			if (cat.equals("All Categories"))
			{
				sql = "SELECT * FROM Auction_Post NATURAL JOIN Item WHERE "+filter+" LIKE \""+searchtext+"\";";
			}else
			{
				sql = "SELECT * FROM Auction_Post NATURAL JOIN Item WHERE item_category = \""+cat+"\";";
			}

			PreparedStatement ps = conn.prepareStatement(sql);
			System.out.println(ps);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				post_id = rs.getInt("post_id");
				name = rs.getString("name");
				desc = rs.getString("post_description");
				cPrice = rs.getDouble("current_price");
				cat = rs.getString("item_category");
				active = PostUtils.validatePost(conn, post_id);

				out.println(
					"<tr><td><a href=AuctionPost?post_id="+post_id+">"+name+"</a></td>"
					+"<td>"+desc+"</td>"
					+"<td>$"+PostUtils.df.format(cPrice)+"</td>"
					+"<td>"+cat+"</td>"
					+"<td>"+(active?"Active":"Inactive")+"</td>"
					+"</tr>");

			}
			out.println("</table>");
			
			ps.close();
			rs.close();
			conn.close();

			out.println("<br><form action=\"index.jsp\">"
						+"<input type=\"submit\" value=\"Go Home\">"
						+"</form>");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
