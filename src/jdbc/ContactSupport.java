package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ContactSupport")
public class ContactSupport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = request.getParameter("description");

		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
	
			String sql = "SELECT name FROM User WHERE perm_level > 1;";
			String insert = "INSERT INTO Inbox (username, message, status, time, link) VALUES (?,?,1,?,?);";
			String username = new String();
			Timestamp time = new Timestamp(System.currentTimeMillis());

			String sender = request.getParameter("username");
			System.out.print(sender);
	
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			String link = "Reply?user="+sender;

			PreparedStatement psinsert = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			while(rs.next())
			{
				username = rs.getString("name");
				psinsert.setString(1,username);
				psinsert.setString(2,msg);
				psinsert.setTimestamp(3, time);
				psinsert.setString(4,link);
				psinsert.executeUpdate();
			}
			psinsert.close();
			ps.close();
			conn.close();
			}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("inbox.jsp");
	}
}
