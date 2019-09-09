<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<!-- Redirects if already logged in -->


	<div align="center">
		<form action="add_customer_rep" method="post">
			User name : <input type="text" name="user" required="required">
			Password : <input type="password" name="password" required="required">
			<input type="submit" value="REGISTER">
		</form>
	</div>
	 <br>
	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>
</body>
</html>