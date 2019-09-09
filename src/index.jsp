<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Welcome Page</title>
</head>
<body>
<%
String username = (String)session.getAttribute("username");
if(username!=null){
	response.sendRedirect("home.jsp");
}
%>
Welcome to BuyMe:
<br>Select an option
<form action="login.jsp">
    <input type="submit" value="Login" />
</form>
<form action="registration.jsp">
    <input type="submit" value="Register" />
</form>
</body>
</html>
