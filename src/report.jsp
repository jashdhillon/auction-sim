<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sales Report</title>
</head>
<body>
<%@ page import="java.sql.*, jdbc.PostUtils" %>
<%
		String username = (String)session.getAttribute("username");
	char flag='x';
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
		out.print("Welcome, ");
	%>
 <a href= "<%= request.getContextPath()%>/profile.jsp"><%= (String)username%> </a>
 <%
		int perm_level = (Integer) session.getAttribute("perm_level");
		if(perm_level == 3) {
	%>
 
 <%
 Class.forName("com.mysql.jdbc.Driver");
 Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
 if(request.getParameter("user")!=null){
	 flag='a';}
 if(flag=='a'&&request.getParameter("category")!=null){
	 flag='c';
 }
 else if(request.getParameter("category")!=null){
	 flag='b';
 }
 if(flag=='a'||flag=='c'){
String userpurchasereport = "SELECT SUM(current_price) AS total FROM Auction_Post WHERE current_price > secret_minimum_price AND active = 0 AND current_buyer_id=?;";
String usersalesreport = "SELECT SUM(current_price) AS total FROM Auction_Post WHERE current_price > secret_minimum_price AND active = 0 AND seller_id=?;";
PreparedStatement pssalesreport = conn.prepareStatement(usersalesreport);
PreparedStatement pspurchasereport = conn.prepareStatement(userpurchasereport);
pssalesreport.setString(1, request.getParameter("user"));
pspurchasereport.setString(1, request.getParameter("user"));
ResultSet rssalesreport = pssalesreport.executeQuery();
ResultSet rspurchasereport = pspurchasereport.executeQuery();
%>
<br><br>User Total Sales<br>
<% 
if(rssalesreport.next()&&rssalesreport.getString("total")!=null){%>
$<%= rssalesreport.getString("total")%><br>
<%}
else{%>
	No Sales<br><% 
}
%>
<br>User Total Purchases<br>
<%
if(rspurchasereport.next()&&rspurchasereport.getString("total")!=null){%>
$<%= rspurchasereport.getString("total")%><br>
<%}
else{%>
	No Purchases<br><%
}
 }
 
 if(flag=='b' ||flag=='c'){
 String catpurchasereport = "SELECT SUM(Auction_Post.current_price) AS total FROM Auction_Post INNER JOIN Item ON Auction_Post.item_id=Item.item_id WHERE Auction_Post.current_price > Auction_Post.secret_minimum_price AND Auction_Post.active = 0 and Item.item_category=?;";


PreparedStatement pscatpurchasereport = conn.prepareStatement(catpurchasereport);

pscatpurchasereport.setString(1, request.getParameter("category"));
ResultSet rscatpurchasereport = pscatpurchasereport.executeQuery();
%>
<br>Sales for <%=request.getParameter("category") %>
<br>
<% 
if(rscatpurchasereport.next()&&rscatpurchasereport.getString("total")!=null){%>
$<%= rscatpurchasereport.getString("total")%><br>
<%}
else{%>
	No Sales<br><% 
}
} %>
<%}
		else{
		%>
		<br> You are not authorized to access this page
		<%} %>
		<form action = "home.jsp">
<input type = "submit" value= "Go Home" />
</form>
</body>
</html>