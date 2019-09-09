<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Profile</title>
</head>
<body>
<%@ page import="java.sql.*" %>
<%
		String username = (String)session.getAttribute("username");
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
		out.print("Welcome, ");
	%>
 <a href= "<%= request.getContextPath()%>/profile.jsp"><%= (String)username%> </a>
 <%
 String searchname=request.getParameter("name");
 if(searchname!=null){
String usercheck="SELECT name FROM User WHERE name = ?";
String activebids = "SELECT name,current_price FROM Auction_Post WHERE current_buyer_id = ? AND active = 1";
String wonbids = "SELECT name,current_price FROM Auction_Post WHERE current_buyer_id = ? AND active = 0";
String activeauctions = "SELECT name,current_buyer_id,current_price FROM Auction_Post WHERE seller_id = ? AND active = 1";
String pastauctions = "SELECT name,current_buyer_id,current_price FROM Auction_Post WHERE seller_id = ? AND active = 0";
String autobid = "SELECT post_id,max_bid FROM Auto_Bid WHERE user_id = ?";

Class.forName("com.mysql.jdbc.Driver");
Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
PreparedStatement psusercheck = conn.prepareStatement(usercheck);
psusercheck.setString(1, searchname);

PreparedStatement psactivebids = conn.prepareStatement(activebids);
PreparedStatement pswonbids = conn.prepareStatement(wonbids);
PreparedStatement psactiveauctions = conn.prepareStatement(activeauctions);
PreparedStatement pspostauctions = conn.prepareStatement(pastauctions);
PreparedStatement psautobid = conn.prepareStatement(autobid);
ResultSet rsusercheck = psusercheck.executeQuery();
if(rsusercheck.next()){
psactivebids.setString(1, searchname);
pswonbids.setString(1, searchname);
psactiveauctions.setString(1, searchname);
pspostauctions.setString(1, searchname);
psautobid.setString(1, searchname);


ResultSet rswonbids = pswonbids.executeQuery();
ResultSet rsautobid = psautobid.executeQuery();
ResultSet rsactivebids = psactivebids.executeQuery();
ResultSet rsactiveauctions = psactiveauctions.executeQuery();
ResultSet rspastauctions = pspostauctions.executeQuery();

%>
<!-- Buying -->

	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>

<div align="center">
<br>
<b><u>Account Activity for <%= searchname%></u></b>
<br><br><br>

<u>Bidding</u><br><br>
 Won Bids <br>
<%if(rswonbids.next()){ %>
<TABLE BORDER="5">
 <TR><TH>Auction Name</TH><TH>Price</TH></TR>
 <%
 do { %>
<TR><TD><%=rswonbids.getString(1) %> </TD><TD><%=rswonbids.getString(2) %> </TD></TR>
<%} while (rswonbids.next());
 %>
</TABLE>
<%}else{%>
None<br>
<%} %>
 Automated Bids <br>
 <%if(rsautobid.next()){ %>
<TABLE BORDER="5">
 <TR><TH>Auction ID</TH><TH>Price Threshold</TH></TR>
 <%
 do { %>
<TR><TD><%=rsautobid.getString(1)%> </TD><TD><%=rsautobid.getString(2) %> </TD></TR>
<%}while (rsautobid.next());%>
</TABLE>
<% } else{ %>
None<br>
<%} %>


Active Bids <br>
<% if(rsactivebids.next()){%>
<TABLE BORDER="5">
 <TR><TH>Auction Name</TH><TH>Price</TH></TR>
 <%do { %>
<TR><TD><%= rsactivebids.getString(1) %> </TD><TD><%= rsactivebids.getString(2) %> </TD></TR>
<%}while (rsactivebids.next());%>
</TABLE>
<%}
else{%>
none
<%} %>
<br><br><br>



<!-- Selling -->
<u>Selling</u><br><br>
Active Auctions<br>
<% if(rsactiveauctions.next()){ %>
<TABLE BORDER="5">
 <TR><TH>Auction Name</TH><TH>Current Buyer</TH><TH>Current Price</TH></TR>
 <%
 do { %>
<TR><TD><%= rsactiveauctions.getString(1) %> </TD><TD><%= rsactiveauctions.getString(2) %> </TD><TD><%= rsactiveauctions.getString(3) %> </TD></TR>
<%}while (rsactiveauctions.next());%>
</TABLE>
<%}
 else{%>
 None<br>
 <%} %>
Past Auctions<br>
<%if(rspastauctions.next()){ %>
<TABLE BORDER="5">
 <TR><TH>Auction Name</TH><TH>Buyer Name</TH><TH>Price</TH></TR>
 <% 
 do { %>
<TR><TD><%=rspastauctions.getString(1) %> </TD><TD><%= rspastauctions.getString(2) %> </TD><TD><%= rspastauctions.getString(3) %> </TD></TR>
<%}while (rspastauctions.next()); %>
</TABLE>
<%}else{ %>
None<br>
<%} %>
<%  conn.close();%>

<!-- Support Hotline Form -->
	</div>
	<%}
else{
%><br><br>User not found<%
		 }	
}

 else{
 %><br><br>User not found<%
 }%>
</body>
</html>