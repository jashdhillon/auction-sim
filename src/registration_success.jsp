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
<%
String username = (String)session.getAttribute("username");
if(username!=null){
	response.sendRedirect("home.jsp");

}%>
You have successfully registered.
   <br>
	<form action="index.jsp" >
		<input type="submit" value="Go Home" />
	</form>
</body>
</html>