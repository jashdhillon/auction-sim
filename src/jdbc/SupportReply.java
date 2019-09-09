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

/**
 * Servlet implementation class SupportReply
 */
@WebServlet("/SupportReply")
public class SupportReply extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String insert = "INSERT INTO Inbox (username, message, status, time,link) VALUES (?,?,1,?,?);";
			String username = request.getParameter("recipient");
			System.out.println(username);
			Timestamp time = new Timestamp(System.currentTimeMillis());
			String msg = request.getParameter("description");
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			PreparedStatement psinsert = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

			String link = "home.jsp";

			psinsert.setString(1,username);
			psinsert.setString(2,msg);
			psinsert.setTimestamp(3, time);
			psinsert.setString(4,link);
			psinsert.executeUpdate();

			psinsert.close();
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
