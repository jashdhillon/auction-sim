<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DELETE PAGE</title>
</head>
<body>
<%
Integer bid_id = (Integer)session.getAttribute("bid_id");
if(bid_id!=null){
	response.sendRedirect("home.jsp");

}%>

	<div align="center">
		<form action="Remove_bid" method="post">
			ID : <input type="text" name="bid_id" required="required">
			<input type="submit" value="DELETE">
		</form>
	</div>
	 <br>
	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>

</body>
</html>