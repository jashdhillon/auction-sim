<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login:</title>
</head>
<body>
<!--Section 1: Session/Account Management -->
<!--Redirects if already logged in--> 
<%
String username = (String)session.getAttribute("username");
if(username!=null){
	response.sendRedirect("home.jsp");
}
%>
<!-- End of Section 1 -->

	<div align="center">
		<form action="Login" method="post">
			User name : <input type="text" name="user" required="required">
			Password : <input type="password" name="password" required="required">
			<input type="submit" value="LOGIN">
		</form>
	</div>
	<br>
   <br>
	<form action="index.jsp" >
		<input type="submit" value="Go Home" />
	</form>
</body>
</html>