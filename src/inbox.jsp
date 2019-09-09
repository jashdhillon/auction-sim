<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Messages</title>
</head>
<body>
	<%@ page import="java.sql.*"%>

	<%
		String username = (String)session.getAttribute("username");
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
		out.print("Welcome, ");
	%>
		<a href= "<%= request.getContextPath()%>/profile.jsp"><%= (String)username%> </a>
	<%
		String select = "SELECT message, status, link, time FROM Inbox WHERE username = ?;";
		String msg = new String();
		String link = new String();
		int status;
		java.sql.Timestamp time = java.sql.Timestamp.valueOf("2019-04-23 00:00:00.0");

		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
		PreparedStatement getMsgs = conn.prepareStatement(select);
		getMsgs.setString(1, username);
		ResultSet rs = getMsgs.executeQuery();
	%>
	<table border="1">
		<tr bgcolor="#d6d6d6">
		<td>Time</td>
		<td>Messages</td>
		<td>Post</td></tr>
	<%
		rs.afterLast();
		while(rs.previous()){
			msg = rs.getString("message");
			link = rs.getString("link");
			status = rs.getInt("status");
			time = rs.getTimestamp("time");
			out.println("<tr>");
			out.println("<td>"+time+"</td>");
			if(status == 0){
				out.println("<td>"+msg+"</td>");
			}else{
				out.println("<td><strong>"+msg+"</strong></td>");
			}
			out.println("<td><a href="+link+">Go To Post</a></td>");
			out.println("</tr>");
		}
	%>
	</table>

	<%
		getMsgs.close();
		rs.close();
	%>

	<%
		String update = "UPDATE Inbox SET status = '0';";
		PreparedStatement updateStatus = conn.prepareStatement(update);
		updateStatus.executeUpdate();
		
		updateStatus.close();
		conn.close();
	%>

	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>

</body>
</html>