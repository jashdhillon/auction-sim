package jdbc;

import java.io.IOException;
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
import javax.servlet.http.HttpSession;

@WebServlet("/Changepass")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("[Changepass.java]");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			HttpSession session = request.getSession();
			String username= (String)session.getAttribute("username").toString();
			String oldpass = request.getParameter("oldpass");
			String newpass = request.getParameter("newpass");
			
			String dbpass = null;
			String sql = "SELECT password FROM User WHERE name = ?";
			
			
			PreparedStatement ps_sql = conn.prepareStatement(sql);
			ps_sql.setString(1, username);
			
			ResultSet rs = ps_sql.executeQuery();
			
			while (rs.next()) {
				dbpass = rs.getString("password");
			}
			
			if(oldpass.equals(dbpass)) {
			String change = "UPDATE User SET password = ? WHERE name = ?";
							
			PreparedStatement ps = conn.prepareStatement(change);
			ps.setString(1, newpass);
			ps.setString(2, username);
			ps.executeUpdate();
			conn.close();
			session.setAttribute("passwordchange", "1");
			response.sendRedirect("settings.jsp");
				return;
			}
			
			
			conn.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
