package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/QuestionPost")
public class QuestionPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST [QuestionPost.java]");
		int message_id = (int) Integer.valueOf(request.getParameter("message_id"));
		
		Question qp = null;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			String sql = "SELECT * FROM Question WHERE message_id = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, message_id);
			
			ResultSet rsq = ps.executeQuery();
			
			String poster_id = null;
			String question = null;
			Timestamp question_post_time = null;
			
			while(rsq.next()) {
				poster_id = rsq.getString("poster_id");
				question = rsq.getString("question");
				question_post_time = rsq.getTimestamp("post_time");
			}
			
			qp =  new Question(message_id, poster_id, question, question_post_time);
			
			sql = "SELECT * FROM Response WHERE message_id = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, message_id);
			
			ResultSet rsr = ps.executeQuery();
			
			while(rsr.next()) {
				int response_id = rsr.getInt("response_id");
				String responder_id = rsr.getString("responder_id");
				String response_msg = rsr.getString("response");
				Timestamp reponse_post_time = rsr.getTimestamp("post_time");
				
				Response pr = new Response(response_id, message_id, responder_id, response_msg, reponse_post_time);
				
				qp.addResponse(pr);
			}
			
			conn.close();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.print(qp.toJSON());
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GET [QuestionPost.java]");
		int message_id = (int) Integer.valueOf(request.getParameter("message_id"));
		int perm_level = 1;
		
		HttpSession session = request.getSession();
		session.setAttribute("message_id", message_id);
		session.setAttribute("perm_level", perm_level);
		response.sendRedirect("question.jsp");
	}
}
