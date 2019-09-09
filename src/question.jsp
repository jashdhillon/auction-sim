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

	<div id="question_post">
		<h2 id=question></h2>
		<h5 id=poster_id></h5>
		<h5 id=post_time></h5>
	</div>
	
	<div id="response_posts">
	</div>
	
	<form action="PostResponse" method="post">
		<textarea rows="5" cols="80" name="response" required></textarea><br>
		<input type="hidden" name="message_id" value="${message_id}" />
		<input type="hidden" name="responder_id" value="${username}" />
		<input type="submit" value="Reply" />
	</form>
	<br>
	<form action="message_board.jsp">
		<input type="submit" value="Back" />
	</form>
</body>
<script>
var message_id = ${message_id};
loadXMLDoc();
function loadXMLDoc() {
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
	    if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
	      
	    	var obj = JSON.parse(xhttp.responseText);
	    	//document.getElementById("data").innerHTML = xhttp.responseText;
	    	
	    	var message_id = obj.message_id;
	    	var poster_id = obj.poster_id;
	    	var question = obj.question.replace(/``cr````nl``/g, "<br>").replace(/``nl``/g, "<br>").replace(/``cr``/g, "<br>");
	    	var post_time = obj.post_time;
	    	var responses = obj.responses;
	    	
	    	document.getElementById("question").innerHTML = "Question: " + question;
	    	document.getElementById("poster_id").innerHTML = "Asked By: " + poster_id;
	    	document.getElementById("post_time").innerHTML = "Posted: " + post_time;
	    	
	    	var final_content = "";
	    	
	    	for(var i = 0; i < responses.length; i++) {
	    		var r = responses[i];
	    		
	    		var responder_id = r.responder_id;
		    	var response = r.response.replace(/``cr````nl``/g, "<br>").replace(/``nl``/g, "<br>").replace(/``cr``/g, "<br>");
		    	var post_time = r.post_time;
	    		
	    		var content = "<hr><br>";
	    		
	    		content += "<h4><p1>Answer: " + response + "</p1></h4>";
	    		content += "<h5>Answered By: " + responder_id + "</h5>";
	    		content += "<h5>Posted: " + post_time + "</h5><br>";
	    		
	    		content += "<hr>"
	    		
	    		final_content += content;
	    	}
	    	
	    	document.getElementById("response_posts").innerHTML = final_content;
	    }
	};
	
	xhttp.open("POST", "QuestionPost?message_id=" + message_id, true);
	xhttp.send();
}
</script>
</html>