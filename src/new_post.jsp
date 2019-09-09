<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create an Auction</title>
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

<%
String itemcat= (String)request.getParameter("category");
if(itemcat!=null&&(itemcat.equals("Shirt")||itemcat.equals("Pants")||itemcat.equals("Shoes"))){
%>
<div align = "center">
<form action= "NewPost" method = "post">
<input type="hidden" name="category" value="<%=itemcat%>">
<tr><td>Post Name: </td><td><input type="text" name="post_name" id="post_name" required></td></tr><br></br>
<tr><td>Post Description: </td><td><input type="text" name="post_description" id="post_description" required></td></tr><br></br>
<tr><td>item name: </td><td><input type="text" name="name" id="name" required></td></tr><br></br>
<tr><td>item description: </td><td><input type="text" name="description" id="description" required></td></tr><br></br>
<tr><td>item condition: </td><td><select name="condition" required> 
<option value="New">New</option>
<option value="Used">Used</option>
<option value="Old">Old</option></select>
</td></tr><br></br>
<%if(itemcat.equals("Shirt")) {%>
<tr><td>Type: </td><td><select name="cat1" required>
<option value="Long Sleeves">Long Sleeves</option>
<option value="Short Sleeves">Short Sleeves</option>
<option value="No Sleeves">No Sleeves</option></select>
</td></tr><br></br>
<tr><td>Size: </td><td><select name="cat2" required>
<option value="XS">Extra Small</option>
<option value="S">Small</option>
<option value="M">Medium</option>
<option value="L">Large</option>
<option value="XL">Extra Large</option>
<option value="XXL">XXL</option>
<option value="XXXL">XXXL</option>
</select>
</td></tr><br></br>
<%}
else if(itemcat.equals("Pants")){%>
<tr><td>Width: </td><td><select name="cat1" required>
<option value="24">24in</option>
<option value="25">25in</option>
<option value="26">26in</option>
<option value="27">27in</option>
<option value="28">28in</option>
<option value="29">29in</option>
<option value="30">30in</option>
<option value="31">31in</option>
<option value="32">32in</option>
<option value="33">33in</option>
<option value="34">34in</option>
<option value="35">35in</option>
<option value="36">36in</option>
<option value="37">37in</option>
<option value="38">38in</option>
<option value="39">39in</option>
<option value="40">40in</option>
<option value="41">41in</option>
<option value="42">42in</option>
<option value="43">43in</option>
<option value="44">44in</option>
<option value="45">45in</option>
<option value="46">46in</option>
<option value="47">47in</option>
<option value="48">48in</option>
<option value="49">49in</option>
<option value="50">50in</option>
<option value="51">51in</option>
<option value="52">52in</option>
<option value="53">53in</option>
<option value="54">54in</option>
<option value="55">55in</option>
<option value="56">56in</option>
<option value="57">57in</option>
<option value="58">58in</option>
<option value="59">59in</option>
<option value="60">60in</option>
<option value="61">61in</option>
<option value="62">62in</option>
<option value="63">63in</option>
<option value="64">64in</option></select>
</td></tr><br></br>
<tr><td>Length: </td><td><select name="cat2" required>
<option value="24">24in</option>
<option value="25">25in</option>
<option value="26">26in</option>
<option value="27">27in</option>
<option value="28">28in</option>
<option value="29">29in</option>
<option value="30">30in</option>
<option value="31">31in</option>
<option value="32">32in</option>
<option value="33">33in</option>
<option value="34">34in</option>
<option value="35">35in</option></select>

</td></tr><br></br>
<%}
else{%>
<tr><td>Type: </td><td><select name="cat1" required>
<option value="Womens Sneakers">Women's Sneakers</option>
<option value="Heels">Heels</option>
<option value="Womens Boots">Women's Boots</option>
<option value="Mens Sneakers">Mens Sneakers</option>
<option value="Mens Dress">Mens Dress shoes</option>
<option value="Mens Boots">Mens Boots</option>
</select>
</td></tr><br></br>
<tr><td>US Standard Shoe Size: </td><td><select name="cat2" required>
<option value="3.5">3.5M/5W</option>
<option value="4">4M/5.5W</option>
<option value="4.5">4.5M/6W</option>
<option value="5">5M/6.5W</option>
<option value="5.5">5.5M/7W</option>
<option value="6">6M/7.5W</option>
<option value="6.5">6.5M/8W</option>
<option value="7">7M/8.5W</option>
<option value="7.5">7.5M/9W</option>
<option value="8">8M/9.5W</option>
<option value="8.5">8.5M/10W</option>
<option value="9">9M/10.5W</option>
<option value="9.5">9.5M/11W</option>
<option value="10">10M/11.5W</option>
<option value="10.5">10.5M/12W</option>
<option value="11">11M/12.5W</option>
<option value="11.5">11.5M/13W</option>
<option value="12">12M/13.5W</option>
<option value="12.5">12.5M/14W</option>
<option value="13">13M/14.5W</option>
<option value="13.5">13.5M/15W</option>
<option value="14">14M/15.5W</option>
</select>
</td></tr><br></br>

<%} %>
<tr><td>Color: </td><td><select name="cat3" required>
<option value="Red">Red</option>
<option value="Orange">Orange</option>
<option value="Yellow">Yellow</option>
<option value="Green">Green</option>
<option value="Blue">Blue</option>
<option value="Purple">Purple</option>
<option value="Brown">Brown</option>
<option value="Magenta">Magenta</option>
<option value="Tan">Tan</option>
<option value="Cyan">Cyan</option>
<option value="Olive">Olive</option>
<option value="Maroon">Maroon</option>
<option value="Navy">Navy</option>
<option value="Aquamarine">Aquamarine</option>
<option value="Turquoise">Turquoise</option>
<option value="Silver">Silver</option>
<option value="Lime">Lime</option>
<option value="Teal">Teal</option>
<option value="Indigo">Indigo</option>
<option value="Violet">Violet</option>
<option value="Pink">Pink</option>
<option value="Black">Black</option>
<option value="White">White</option>
<option value="Gray">Gray</option>

</select></td></tr><br></br>
<tr><td>Initial Price: $</td><td><input type="number" step="0.01" min=0 name="inital_price" required></td></tr><br></br>
<tr><td>Minimum Price to Sell (optional): $</td><td><input type="number" step="0.01" min=0 name="secret_minimum_price"></td></tr><br></br>
<tr><td>Bids should increment by: $</td><td><input type="number" step="0.01" min=0 name="bid_incrementer" required></td></tr><br></br>
<tr><td>Auction Duration: </td><td><input type="number" name="days" required>days</td><td><input type="number" name="hours" required>hours</td><td><input type="number" name="minutes" required>minutes</td></tr><br></br>
<input type = "submit" value= "Create Post" />
</form>

<form action = "home.jsp">
<input type = "submit" value= "Go Home" />
</form>


</div>
<%}
else{
	%>
<br><br>
invalid post type
<br>
	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>
	<%
}
%>




</body>
<script type="text/javascript">
document.getElementById("post_name").onkeypress = function(e) {
    this.value = this.value.replace(/[&\/\\#,+()$~%.'":*?<>{}\[\]]/g, '');
};

document.getElementById("post_description").onkeypress = function(e) {
	this.value = this.value.replace(/[&\/\\#,+()$~%.'":*?<>{}\[\]]/g, '');
};

document.getElementById("name").onkeypress = function(e) {
	this.value = this.value.replace(/[&\/\\#,+()$~%.'":*?<>{}\[\]]/g, '');
};

document.getElementById("description").onkeypress = function(e) {
	this.value = this.value.replace(/[&\/\\#,+()$~%.'":*?<>{}\[\]]/g, '');
};
</script>
</html>