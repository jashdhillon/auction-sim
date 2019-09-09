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

@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("[Registration.java]");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			
			String name = request.getParameter("user");
			String password = request.getParameter("password");
			
			String dbName = null;
			String sql_check = "SELECT * FROM User WHERE name = ?";
			
			PreparedStatement ps_check = conn.prepareStatement(sql_check);
			ps_check.setString(1, name);
			
			ResultSet rs = ps_check.executeQuery();
			
			while (rs.next()) {
				dbName = rs.getString("name");
			}
			
			if(name.equals(dbName)) {
				response.sendRedirect("registration_failed.jsp");
				return;
			}
			
			String sql = "insert into User(name, password) values(?, ?)";
							
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			ps.executeUpdate();
			
			response.sendRedirect("registration_success.jsp");
			
			conn.close();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
