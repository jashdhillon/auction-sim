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
 * Servlet implementation class Reply
 */
@WebServlet("/Reply")
public class Reply extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String recipient = request.getParameter("user");
		PrintWriter out = response.getWriter();
		out.println("Reply:\n"
		+"<br><form action=\"SupportReply\" method=\"post\">"
		+"<textarea name = \"description\" placeholder=\"Reply to the customer\"></textarea>"
	    +"<br>"
	    +"<input type=\"hidden\" name=\"recipient\" value=\""+recipient+"\">"
		+"<input type=\"submit\" value=\"SUBMIT\">"
		+"</form>");
	}

}
