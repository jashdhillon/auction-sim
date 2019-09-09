<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Item registration</title>
</head>
<body>
<%
		String username = (String)session.getAttribute("username");
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
		out.print("User: ");
	%>
 <a href= "<%= request.getContextPath()%>/profile.jsp"><%= (String)username%> </a>

<br><br>
Please describe the item you wish to sell
<form action=new_post.jsp method="GET">
What type of item do you want to sell?
	<select name="category" required>
		<option value="Shirt">Shirt</option>
		<option value="Pants">Pants</option>
		<option value="Shoes">Shoes</option>
	</select>
<br>
<input type="submit" value="Continue"/>
</form>
<br>
<br>
	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>
</body>
</html>