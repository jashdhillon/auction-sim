<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DELETE Auction</title>
</head>
<body>
<%
Integer post_id = (Integer)session.getAttribute("post_id");
if(post_id!=null){
	response.sendRedirect("home.jsp");

}%>

	<div align="center">
		<form action="remove_auction" method="post">
			ID : <input type="text" name="post_id" required="required">
			<input type="submit" value="DELETE">
		</form>
	</div>
	 <br>
	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>

</body>
</html>