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


@WebServlet("/MessageBoard")
public class MessageBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String searchText = request.getParameter("search_text");
		String searchCat = request.getParameter("search_cat");
		String searchOrderCat = request.getParameter("search_order_cat");
		String searchOrderDir = request.getParameter("search_order_dir");
		
		System.out.println(searchText);
		
		ArrayList<Question> questionPosts = new ArrayList<Question>();
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			String sql = "SELECT * FROM Question ORDER BY post_time DESC";
			sql = "SELECT * FROM Question WHERE " + searchCat + " LIKE \'%" + searchText + "%\' ORDER BY " + searchOrderCat + " " + searchOrderDir;
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rsq = ps.executeQuery();
			
			while(rsq.next()) {
				int message_id = rsq.getInt("message_id");
				String poster_id = rsq.getString("poster_id");
				String question = rsq.getString("question");
				Timestamp question_post_time = rsq.getTimestamp("post_time");
				
//				System.out.println(rsq.getInt("message_id") + " " + rsq.getString("poster_id") + " " + rsq.getString("question") + " " + rsq.getTimestamp("post_time"));
				
				Question qp =  new Question(message_id, poster_id, question, question_post_time);
				
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
				
				questionPosts.add(qp);
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
		
		String masterJSON = "{ \"posts\": [";
		
		for(int i = 0; i < questionPosts.size() - 1; i++) {
			Question qp = questionPosts.get(i);
			masterJSON += qp.toJSON() + ", ";
		}
		
		if(questionPosts.size() > 0) {
			Question qp = questionPosts.get(questionPosts.size() - 1);
			masterJSON += qp.toJSON();
		}
		
		masterJSON += " ] }";
		
		out.print(masterJSON);
	}
}
