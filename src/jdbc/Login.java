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


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("[Login.java]");
		try {
			String name = request.getParameter("user");
			String password = request.getParameter("password");
			String dbName = null;
			String dbPassword = null;
			int perm_level = 1;
			String sql = "SELECT * FROM User WHERE name = ? AND password = ?";
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				dbName = rs.getString("name");
				dbPassword = rs.getString("password");
				perm_level = rs.getInt("perm_level");
			}
			
			if(name.equals(dbName) && password.equals(dbPassword)) {
				HttpSession session = request.getSession(true);
				session.setAttribute("username", name);
				session.setAttribute("perm_level", perm_level);
				response.sendRedirect("home.jsp");
			} else {
				response.sendRedirect("login_failed.jsp");
			}
			
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
