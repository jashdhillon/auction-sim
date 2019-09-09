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
String activebids = "SELECT name,current_price FROM Auction_Post WHERE current_buyer_id = ? AND active = 1";
String wonbids = "SELECT name,current_price FROM Auction_Post WHERE current_buyer_id = ? AND active = 0";
String activeauctions = "SELECT name,current_buyer_id,current_price FROM Auction_Post WHERE seller_id = ? AND active = 1";
String pastauctions = "SELECT name,current_buyer_id,current_price FROM Auction_Post WHERE seller_id = ? AND active = 0";
String autobid = "SELECT post_id,max_bid FROM Auto_Bid WHERE user_id = ?";

Class.forName("com.mysql.jdbc.Driver");
Connection conn = DriverManager.getConnection("jdbc:mysql://db336section4.clmnawk954xs.us-east-2.rds.amazonaws.com:3306/BuyMe", "db336", "LetMeInPlease");
PreparedStatement psactivebids = conn.prepareStatement(activebids);
PreparedStatement pswonbids = conn.prepareStatement(wonbids);
PreparedStatement psactiveauctions = conn.prepareStatement(activeauctions);
PreparedStatement pspostauctions = conn.prepareStatement(pastauctions);
PreparedStatement psautobid = conn.prepareStatement(autobid);

psactivebids.setString(1, username);
pswonbids.setString(1, username);
psactiveauctions.setString(1, username);
pspostauctions.setString(1, username);
psautobid.setString(1, username);

ResultSet rswonbids = pswonbids.executeQuery();
ResultSet rsautobid = psautobid.executeQuery();
ResultSet rsactivebids = psactivebids.executeQuery();
ResultSet rsactiveauctions = psactiveauctions.executeQuery();
ResultSet rspastauctions = pspostauctions.executeQuery();
%>
<!-- Buying -->

<div align="center">
<br>
<b><u>Account Activity</u></b>
<br><br><br>

<u>Bidding</u><br><br>
 Won Bids <br>
<%if(rswonbids.next()){ %>
<TABLE BORDER="5">
 <TR><TH>Auction Name</TH><TH>Price</TH></TR>
 <%
 do { 
	 String anp = PostUtils.df.format(rswonbids.getDouble(2));
 %>
<TR><TD><%=rswonbids.getString(1) %> </TD><TD><%=anp %> </TD></TR>
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
 do { 
	 String abt = PostUtils.df.format(rsautobid.getDouble(2));
 %>
<TR><TD><%=rsautobid.getString(1)%> </TD><TD><%=abt %> </TD></TR>
<%}while (rsautobid.next());%>
</TABLE>
<% } else{ %>
None<br>
<%} %>


Active Bids <br>
<% if(rsactivebids.next()){%>
<TABLE BORDER="5">
 <TR><TH>Auction Name</TH><TH>Price</TH></TR>
 <%do { 
	 String abp = PostUtils.df.format(rsactivebids.getDouble(2));
 %>
<TR><TD><%= rsactivebids.getString(1) %> </TD><TD><%= abp %> </TD></TR>
<%}while (rsactivebids.next());%>
</TABLE>
<%}
else{%>
none
<%} %>
<br><br><br>



<u>Selling</u><br><br>
Active Auctions<br>
<% if(rsactiveauctions.next()){ %>
<TABLE BORDER="5">
 <TR><TH>Auction Name</TH><TH>Current Buyer</TH><TH>Current Price</TH></TR>
 <%
 do { 
	 String aacp = PostUtils.df.format(rsactiveauctions.getDouble(3));
	 String aacb = rsactiveauctions.getString(2);

	 if(aacb != null) {
		 aacb = "<a href=\""+request.getContextPath()+"/viewprofile.jsp?name="+rsactiveauctions.getString(2)+"\">" + rsactiveauctions.getString(2) + "</a>";
	 } else {
		 aacb = "None";
	 }
 %>
<TR><TD><%= rsactiveauctions.getString(1) %> </TD><TD><%= aacb %> </TD><TD><%= aacp %> </TD></TR>
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
 do { 
	 String pap = PostUtils.df.format(rspastauctions.getDouble(3));
	 String pacb = rsactiveauctions.getString(2);

	 if(pacb != null) {
		 pacb = "<a href=\""+request.getContextPath()+"/viewprofile.jsp?name="+rspastauctions.getString(2)+"\">" + rspastauctions.getString(2) + "</a>";
	 } else {
		 pacb = "None";
	 }
	 
 %>
<TR><TD><%=rspastauctions.getString(1) %> </TD><TD><%= pacb %> </TD><TD><%= pap %> </TD></TR>
<%}while (rspastauctions.next()); %>
</TABLE>
<%}else{ %>
None<br>
<%} %>
<%  conn.close();%>


<!-- Support Hotline Form -->
		<br>
		Contact Support:
		<br>
		<form action="ContactSupport" method="post">
            <textarea name = "description" placeholder="Please explain your problem in detail"></textarea>
            <br>
            <input type="hidden" name="username" value=${username}>
            <input type="submit" value="SUBMIT">
        </form>
		<br><br>
		<form action="inbox.jsp">
			<input type="submit" value="Messages">
		</form>
		<form action = "home.jsp">
		<input type = "submit" value= "Go Home" />
		</form>
		<form action = "settings.jsp">
		<input type = "submit" value= "Settings" />
		</form>
	</div>
<!-- Billing Options -->

<!-- Password Management -->
</body>
</html>