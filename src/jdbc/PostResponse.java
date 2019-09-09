package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/PostResponse")
public class PostResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST [Response.java]");
		int message_id = (int) Integer.valueOf(request.getParameter("message_id"));
		String responder_id = request.getParameter("responder_id");
		String response_msg = request.getParameter("response");
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		
		HttpSession session = request.getSession();
		session.setAttribute("message_id", message_id);
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			String sql = "INSERT INTO Response(message_id, responder_id, response, post_Time) VALUES(?, ?, ?, ?)";
			
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, message_id);
			ps.setString(2, responder_id);
			ps.setString(3, response_msg);
			ps.setTimestamp(4, currentTime);
			
			ps.executeUpdate();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		response.sendRedirect("question.jsp");
	}
}