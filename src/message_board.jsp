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
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
	%>
	
	<form action="#" id="searchbar">
		<input type="text" id="searchtext" placeholder="Search for previous questions asked">
		<label>Filter:</label>
		<select id="sort_cat">
			<option value="question" selected="selected">Question</option>
			<option value="poster_id">Poster</option>
			<option value="post_time">Post Time</option>
		</select>
		<label>Order By:</label>
		<select id="order_cat">
			<option value="question">Question</option>
			<option value="poster_id">Poster</option>
			<option value="post_time" selected="selected">Post Time</option>
		</select>
		<label>Direction:</label>
		<select id="order_dir">
			<option value="asc">Ascending</option>
			<option value="desc" selected="selected">Descending</option>
		</select>
		<button onclick="loadXMLDoc(true);">Search</button>
	</form>
	<br>
	<h3>Ask Question Here:</h3>
	<form action="PostQuestion" method="post">
		<textarea rows="5" cols="80" name="question" id="post_q" required></textarea><br>
		<input type="hidden" name="poster_id" value="${username}" />
		<input type="submit" value="Ask" />
	</form>
	<br>
	<div id="msg_table">
	</div>
	<br>
	<form action="home.jsp" >
		<input type="submit" value="Go Home" />
	</form>
</body>
<script>
	document.getElementById("post_q").onkeypress = function(e) {
	    this.value = this.value.replace(/[&\/\\#+()~'":*<>{}\[\]]/g, '');
	};
	
	function get_cell(item) {
		return "<td>" + item + "</td>";
	}
	
	function get_row(message_id, question, poster_id, post_time) {
		return "<tr>" + get_cell("<a href=\"QuestionPost?message_id=" + message_id + "\">" + question.replace(/``cr````nl``/g, "<br>").replace(/``nl``/g, "<br>").replace(/``cr``/g, "<br>") + "</a>") + get_cell(poster_id) + get_cell(post_time) + "</tr>";
	}
	
	loadXMLDoc(false);
	function loadXMLDoc(poll_searchtext) {
		var xhttp = new XMLHttpRequest();
		
		xhttp.onreadystatechange = function() {
		    if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
		       
		    	var obj = JSON.parse(xhttp.responseText);
		    	
		    	var posts = obj.posts;
		    	
		    	var table_contents = "";
		    	
		    	table_contents += "<table border=\"1\"> ";
		    	table_contents += "<tr bgcolor=\"#d6d6d6\">";
		    	table_contents += "	<td>Question</td>";
		    	table_contents += "	<td>Posted By</td>";
		    	table_contents += "	<td>Post Time</td>";
		    	table_contents += "</tr>";
		    	
		    	for(var i = 0; i < posts.length; i++) {
		    		var p = posts[i];
		    		table_contents += get_row(p.message_id, p.question, p.poster_id, p.post_time);
		    	}
		    	
		    	table_contents += "</table>";
		    	
		    	document.getElementById("msg_table").innerHTML = table_contents;
		    }
		};
		
		var search_text = document.getElementById("searchtext").value;
		var search_cat = document.getElementById("sort_cat").value;
		var search_order_cat = document.getElementById("order_cat").value;
		var search_order_dir = document.getElementById("order_dir").value;
		
		var searchQuery = "MessageBoard?" + 
				(poll_searchtext ? 
						"search_text=" + search_text
						+ "&search_cat=" + search_cat
						+ "&search_order_cat=" + search_order_cat
						+ "&search_order_dir=" + search_order_dir
						: "search_text="
						+ "&search_cat=question"
						+ "&search_order_cat=post_time"
						+ "&search_order_dir=DESC");
		
		console.log(searchQuery);
		
		xhttp.open("GET", searchQuery, true);
		xhttp.send();
	}
</script>
</html>