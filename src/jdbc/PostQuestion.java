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


@WebServlet("/PostQuestion")
public class PostQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST [PostQuestion.java]");
		int message_id = -1;
		String poster_id = request.getParameter("poster_id");
		String question = request.getParameter("question");
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			String sql = "INSERT INTO Question(poster_id, question, post_time) VALUES(?, ?, ?)";
			
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, poster_id);
			ps.setString(2, question);
			ps.setTimestamp(3, currentTime);
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			while(rs.next()) {
				message_id = rs.getInt(1);
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("message_id", message_id);
		
		response.sendRedirect("question.jsp");
	}
}