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
String salesreport = "SELECT SUM(current_price) AS total FROM Auction_Post WHERE current_price > secret_minimum_price AND active = 0";
String shoecount=  "SELECT COUNT(*) AS total FROM Auction_Post INNER JOIN Item ON Auction_Post.item_id=Item.item_id WHERE Auction_Post.active = 1 and Item.item_category= 'shoes';";
String pantcount="SELECT COUNT(*) AS total FROM Auction_Post INNER JOIN Item ON Auction_Post.item_id=Item.item_id WHERE Auction_Post.active = 1 and Item.item_category= 'pants';";
String shirtcount="SELECT COUNT(*) AS total FROM Auction_Post INNER JOIN Item ON Auction_Post.item_id=Item.item_id WHERE Auction_Post.active = 1 and Item.item_category= 'shirt';";

Class.forName("com.mysql.jdbc.Driver");
Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
PreparedStatement pssalesreport = conn.prepareStatement(salesreport);
PreparedStatement psshoecount = conn.prepareStatement(shoecount);
PreparedStatement pspantcount = conn.prepareStatement(pantcount);
PreparedStatement psshirtcount = conn.prepareStatement(shirtcount);
ResultSet rssalesreport = pssalesreport.executeQuery();
ResultSet rsshoe = psshoecount.executeQuery();
ResultSet rspant = pspantcount.executeQuery();
ResultSet rsshirt = psshirtcount.executeQuery();
rsshoe.next();
rspant.next();
rsshirt.next();
int shoe=0;
int pant=0;
int shirt=0;
if(rsshoe.getString("total")!=null){
	shoe=Integer.parseInt(rsshoe.getString("total"));
}
if(rspant.getString("total")!=null){
	pant=Integer.parseInt(rspant.getString("total"));
}
if(rsshirt.getString("total")!=null){
	shirt=Integer.parseInt(rsshirt.getString("total"));
}
%>

<table>
<tr>
<td>Sales Report </td>
 </tr>
 <tr>
 <% if(rssalesreport.next()&&rssalesreport.getString("total")!=null){%>
 <td>Total Cost of all items sold: $<%=rssalesreport.getString("total") %> </td>
 <%}
 else{%>
  <td>no items sold </td>
  <%} %>
 </tr>
</table>
<br><br>Most Popular Listing Category<br>
<%
if(shoe==pant&&pant==shirt){
	%>
<br>None<br>
	 <%
}
else{

if(shirt>shoe){
	if(shirt>pant){
		%><br>Shirt<br><%
	}
	else{
		%><br>Pants<br><%
	}
}
else{
	if(shoe>pant){
		%><br>Shoes<br><%
	}
	else{
		%><br>Pants<br><%
	}
}
}
%>
<br><br>
<%conn.close(); %>
Report by category<br>
<form action="report.jsp" method="POST">
<select name=category>
<option value="shirt">shirt</option>
<option value="pants">pants</option>
<option value="shoes">shoes</option>
</select>
<input type="submit" value="Get Report"/>
</form>
Report by User<br>
<form action="report.jsp" method="POST">
<input type="text" name="user">
<input type="submit" value="Get Report"/>
</form>
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