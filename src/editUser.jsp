<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit User</title>
</head>
<body>
<!-- Password Management -->

<%
		String username = (String)session.getAttribute("username");
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
		out.print("Welcome, ");
	%>
 <a href= "<%= request.getContextPath()%>/profile.jsp"><%= (String)username%> </a>


<form action = "home.jsp">

<input type = "submit" value= "Go Home" />
</form>
<div align="center">
<br><b>Change Password</b><br>
<%if(session.getAttribute("passwordchange")!=null){ %>
<i>password has Been changed</i><br>
<%session.removeAttribute("passwordchange");
} %>
		<form action="RepPasswordChange" method="post">
		
			name: <input type="text" name="name" required="required">
			<br>
			Old Password : <input type="password" name="oldpass" required="required">
			<br>
			New Password : <input type="password" name="newpass" required="required">
			<br>
			<input type="submit" value="Change Password">
		</form>
		
		
		
	
<!-- ---------------------Payment Information ------------------------------------------------------------>
<b>Change Payment Information</b><br>
<%if(session.getAttribute("paymentchange")!=null){ %>
<i>payment information has been changed</i><br>
<%session.removeAttribute("paymentchange");
} %>
<form action="RepChangePayment" method="post">
			user id: <input type="text" name="user_id">
			<br>
			Card Holder: <input type="text" name="holdername">
			<br>
			Card Type : <select name="Filter">
			<option value= "Visa">Visa</option>
			<option value= "MasterCard">MasterCard</option>
			<option value= "American Express">American Express</option>
			<option value= "Chase">Chase</option>
			<option value= "Discover">Discover</option>
			</select>
			<br>
			Card# : <input type="number" maxlength="16" name="cardnum" required="required">
			<br>
			CVC : <input type="number" maxlength="3" name="cvc" required="required">
			<br>
			<label>Expiration Date</label>
                <select name="Month">
                    <option value="01">January</option>
                    <option value="02">February </option>
                    <option value="03">March</option>
                    <option value="04">April</option>
                    <option value="05">May</option>
                    <option value="06">June</option>
                    <option value="07">July</option>
                    <option value="08">August</option>
                    <option value="09">September</option>
                    <option value="10">October</option>
                    <option value="11">November</option>
                    <option value="12">December</option>
                </select>
                <select name= "Year">
                    <option value="2019"> 2019</option>
                    <option value="2020"> 2020</option>
                    <option value="2021"> 2021</option>
                    <option value="2022"> 2022</option>
                    <option value="2023"> 2023</option>
                    <option value="2024"> 2024</option>
                </select>
                <br>
			Address <input type="text" name="address" required="required">
			<br>
			City : <input type="text" name="city" required="required">
			<label>State</label>
                <select name="state">
	<option value="AL">Alabama</option>
	<option value="AK">Alaska</option>
	<option value="AZ">Arizona</option>
	<option value="AR">Arkansas</option>
	<option value="CA">California</option>
	<option value="CO">Colorado</option>
	<option value="CT">Connecticut</option>
	<option value="DE">Delaware</option>
	<option value="DC">District Of Columbia</option>
	<option value="FL">Florida</option>
	<option value="GA">Georgia</option>
	<option value="HI">Hawaii</option>
	<option value="ID">Idaho</option>
	<option value="IL">Illinois</option>
	<option value="IN">Indiana</option>
	<option value="IA">Iowa</option>
	<option value="KS">Kansas</option>
	<option value="KY">Kentucky</option>
	<option value="LA">Louisiana</option>
	<option value="ME">Maine</option>
	<option value="MD">Maryland</option>
	<option value="MA">Massachusetts</option>
	<option value="MI">Michigan</option>
	<option value="MN">Minnesota</option>
	<option value="MS">Mississippi</option>
	<option value="MO">Missouri</option>
	<option value="MT">Montana</option>
	<option value="NE">Nebraska</option>
	<option value="NV">Nevada</option>
	<option value="NH">New Hampshire</option>
	<option value="NJ">New Jersey</option>
	<option value="NM">New Mexico</option>
	<option value="NY">New York</option>
	<option value="NC">North Carolina</option>
	<option value="ND">North Dakota</option>
	<option value="OH">Ohio</option>
	<option value="OK">Oklahoma</option>
	<option value="OR">Oregon</option>
	<option value="PA">Pennsylvania</option>
	<option value="RI">Rhode Island</option>
	<option value="SC">South Carolina</option>
	<option value="SD">South Dakota</option>
	<option value="TN">Tennessee</option>
	<option value="TX">Texas</option>
	<option value="UT">Utah</option>
	<option value="VT">Vermont</option>
	<option value="VA">Virginia</option>
	<option value="WA">Washington</option>
	<option value="WV">West Virginia</option>
	<option value="WI">Wisconsin</option>
	<option value="WY">Wyoming</option>
</select>				
                <br>
			Zip Code : <input type="number" maxlength="5" name="zip" required="required">
			<br>
			<input type="submit" value="Update Payment Info">
		</form>
</div>
	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>
</body>
</html>