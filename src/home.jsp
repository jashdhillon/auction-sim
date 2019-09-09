<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BuyMe Home Page</title>
</head>
<body>
	
	<%
		String username = (String)session.getAttribute("username");
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
		out.print("Welcome, ");
	%>
 <a href= "<%= request.getContextPath()%>/profile.jsp"><%= (String)username%> </a>
	<form action="AuctionList" name ="searchbar">
		<input type="text" name="searchtext" placeholder="Search for an auction">
		<select name="Filter">
			<option value="Item">Item</option>
			<option value="Description">Description</option>
			<option value="Category">Category</option>
			<input type="hidden" name="category" value="All Categories">
		</select>
		<input type="submit" value="Search">
	</form>
	<br>
	<form action="item_info.jsp">
		<input type="submit" value="Create An Auction">
	</form>
	<form action="AuctionList">
		<input type="hidden" name="category" value="All Categories">
		<input type="hidden" name="Filter" value="nofilter">
		<input type="hidden" name="searchtext" value="">
	    <input type="submit" value="Browse Items">
	</form>
	<form action="message_board.jsp">
		<input type="submit" value="Go to Message Board">
	</form>
	<form action="logout.jsp">
		<input type="submit" value="Logout">
	</form>
	<br>
	
	<%
		int perm_level = (Integer) session.getAttribute("perm_level");
		if(perm_level == 3) {
	%>
	 		<h4>Admin options</h4>
			<br>
			<form action="add_customer_rep.jsp" >
				<input type="submit" value="Create Account for Customer Representative" />
			</form>
			<form action="salesreport.jsp">
				<input type="submit" value="Generate Sales Report" />
			</form>
			
	<%
		}
		if(perm_level >= 2) {
	%>
	
			<h4>Editing Options</h4>
			<br>
			<form action="remove_bid.jsp" >
				<input type="submit" value=" Delete Bid" />
			</form>
			<form action="change_bid.jsp" >
				<input type="submit" value="Edit Bid" />
			</form>
			<form action="remove_auction.jsp" >
				<input type="submit" value="Delete Auction" />
			</form>
			
			<form action="editUser.jsp" >
			<input type="submit" value="Edit User Info" />
			</form>
			<form action="answerquestion.jsp" >
			<input type="submit" value="Answer Questions" />
			</form>
	<%
		}
	%>

</body>