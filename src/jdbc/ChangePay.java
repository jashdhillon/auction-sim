package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/changepay")
public class ChangePay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("[Changepass.java]");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
			HttpSession session = request.getSession();
			String username= (String)session.getAttribute("username").toString();
			String holdername = request.getParameter("holdername");
			String Cardtype = request.getParameter("Filter");
			String cardnumber= request.getParameter("cardnum");
			String CVCcode= request.getParameter("cvc");
			String ExpiryMonth= request.getParameter("Month");
			String ExpiryYear= request.getParameter("Year");
			String Address=(String)request.getParameter("address")+", "+(String)request.getParameter("city")+", "+(String)request.getParameter("state")+", "+(String)request.getParameter("zip");
			
			String change = "INSERT INTO  Payment  ( user_id ,  card_name ,  card_type ,  card_number ,  cvc ,  card_expiry_month ,  card_expiry_year ,  biling_address ) VALUES ( ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? )ON DUPLICATE KEY UPDATE  card_name =VALUES(card_name),card_type=VALUES(card_type),card_number=VALUES(card_number),cvc=VALUES(cvc),card_expiry_month=VALUES(card_expiry_month),card_expiry_year=VALUES(card_expiry_year),biling_address=VALUES(biling_address);";
			
			PreparedStatement pschange = conn.prepareStatement(change);
			pschange.setString(1, username);
			pschange.setString(2, holdername);
			pschange.setString(3, Cardtype);
			pschange.setString(4, cardnumber);
			pschange.setString(5, CVCcode);
			pschange.setString(6, ExpiryMonth);
			pschange.setString(7, ExpiryYear);
			pschange.setString(8, Address);
			//pschange.setString(9, username);
			pschange.executeUpdate();
			conn.close();
			session.setAttribute("paymentchange", "1");
			conn.close();
			response.sendRedirect("settings.jsp");
			return;
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
