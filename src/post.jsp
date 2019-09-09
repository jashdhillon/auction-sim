<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="ISO-8859-1">
	<title>Insert title here</title>
	</head>
	<style>
		.split {
	  		height: 100%;
	  		width: 50%;
	  		position: fixed;
	  		z-index: 1;
	  		top: 0;
	  		overflow-x: hidden;
	  		padding-top: 20px;
  		}
  		
  		/* Control the left side */
		.left {
			left: 0;
			margin-bottom: 10%;
		}
		
		/* Control the right side */
		.right {
			right: 0;
			margin-bottom: 10%;
		}
		
		/* If you want the content centered horizontally and vertically */
		.centered {
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
			text-align: center;
		}
		
		.scrollit {
    		overflow:scroll;
    		height: 603px;
    		overflow-x: hidden;
		}
		
		.button {
		  font: bold 11px Arial;
		  text-decoration: none;
		  background-color: #EEEEEE;
		  color: #333333;
		  padding: 2px 6px 2px 6px;
		  border-top: 1px solid #CCCCCC;
		  border-right: 1px solid #333333;
		  border-bottom: 1px solid #333333;
		  border-left: 1px solid #CCCCCC;
		}
	</style>
	<body>
		<!-- Redirects if already logged in -->
		<%
		String username = (String)session.getAttribute("username");
		if(username==null){
			response.sendRedirect(request.getContextPath());
		}
		%>
		
		<div id="left" class="split left">
			<div class="center">
				<h2>Auction Info</h2>
				<div id="data">
					<h1 id="auction_post_name"><br></h1>
					<h4 id="auction_post_seller"><br></h4>
					<h4 id="auction_post_description"><br></h4>
					<h2 id="status">${status}<br></h2>
					<h3 id="open_time"><br></h3>
					<h3 id="close_time"><br></h3>
					<h3 id="item_name"><br></h3>
					<h4 id="item_description"></h4>
					<h3 id="item_condition"><br></h3>
					<h3 id="item_category"><br></h3>
					<h4 id="current_price"><br></h4>
					<h4 id="bid_incrementer"><br></h4>
					<h4 id="current_buyer"><br></h4>
				</div>
				<div id="bidding">
					<%
						Object activeObj = session.getAttribute("active");
						boolean isActive = activeObj == null ? false : (Boolean) activeObj;
						
						if(isActive) {
					%>
							${notification}
							<form action="MakeBid" method="get">
								<input type="hidden" name="post_id" value="${post_id}">
								<input type="hidden" name="user_id" value="${username}">
								<input type="number" step="0.01" min=0 name="amount" placeholder="Enter bid increase amount">
								<input type="submit" value="Bid">
							</form>
					
					<%
							Object enabledObj = session.getAttribute("enabled");
							boolean enabled = enabledObj == null ? false : (Boolean) enabledObj;
							
							if(enabled) {
					%>	
								<form action="AutoBid" method="get">
									<input type="hidden" name="post_id" value="${post_id}">
									<input type="hidden" name="user_id" value="${username}">
									<input type="submit" name="enabled" value="Disable Auto-Bidding">
								</form>
					<%
							} else {
					%>
								<form action="AutoBid" method="get">
									<input type="hidden" name="post_id" value="${post_id}">
									<input type="hidden" name="user_id" value="${username}">
									<input type="number" step="0.01" min=0 name="max_bid" placeholder="Enter max auto-bid threshold" required>
									<input type="submit" value="Enable Auto-Bidding">
								</form>
					<%
							}
						}	
					%>
				<br>
				</div>
				<a id="similar_items" class="button">Similar Items</a>
				<br>
				<br>
					<form action="home.jsp" >
						<input type="submit" value="Go Home" />
					</form>
			</div>		
		</div>
		
		<div id="right" class="split right">
			<div class="center">
				<h2>Auction Bid History</h2>
					<div id="bid_table" class="scrollit">
					</div>
			</div>
		</div>
		
	</body>
	<script>
		function get_cell(item) {
			return "<td>" + item + "</td>";
		}
		
		function get_row(user_id, amount) {
			return "<tr>" + get_cell(user_id) + get_cell(amount) + "</tr>";
		}
		
		function si() {
			loadXMLDoc();
			
		}
		
		var post_id = ${post_id};
		loadXMLDoc();
		function loadXMLDoc() {
			var xhttp = new XMLHttpRequest();
			
			xhttp.onreadystatechange = function() {
			    if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
			       // Typical action to be performed when the document is ready:
			    	var obj = JSON.parse(xhttp.responseText);
			    	document.getElementById("auction_post_name").innerHTML = obj.auction_post_name;
			    	document.getElementById("auction_post_seller").innerHTML = "Seller: " + obj.auction_post_seller;
			    	document.getElementById("auction_post_description").innerHTML = obj.auction_post_description;
			    	document.getElementById("status").innerHTML = "Auction Status: " + obj.status;
			    	document.getElementById("open_time").innerHTML = "Open Time: " + obj.open_time;
			    	document.getElementById("close_time").innerHTML = "Close Time: " + obj.close_time;
			    	document.getElementById("item_name").innerHTML = "Item: " + obj.item_name;
			    	document.getElementById("item_description").innerHTML = obj.item_description;
			    	document.getElementById("item_condition").innerHTML = "Condition: " + obj.item_condition;
			    	document.getElementById("item_category").innerHTML = "Category: " + obj.item_category;
			    	document.getElementById("current_price").innerHTML = "Current Price: $" + parseFloat(obj.current_price).toFixed(2);
			    	document.getElementById("bid_incrementer").innerHTML = "Minimum Bid Incrementer: $" + parseFloat(obj.bid_incrementer).toFixed(2);
			    	document.getElementById("current_buyer").innerHTML = "Current Bid Leader: " + obj.current_buyer;
			    	document.getElementById("similar_items").href = ("AuctionList?category=" + obj.item_category + "&Filter=nofilter");
			    	
			    	var bids = obj.bids;
			    	
			    	var table_contents = "";
			    	
			    	table_contents += "<table border=\"1\"> ";
			    	table_contents += "<tr bgcolor=\"#d6d6d6\">";
			    	table_contents += "	<td>Place By</td>";
			    	table_contents += "	<td>Amount</td>";
			    	table_contents += "</tr>";
			    	
			    	for(var i = 0; i < bids.length; i++) {
			    		var b = bids[i];
			    		table_contents += get_row(b.user_id, "$" + parseFloat(b.amount).toFixed(2));
			    	}
			    	
			    	table_contents += "</table>";
			    	
			    	document.getElementById("bid_table").innerHTML = table_contents;
			    }
			};
			
			xhttp.open("POST", "AuctionPost?post_id=" + post_id, true);
			xhttp.send();
			setTimeout(loadXMLDoc, 5000);
		};
	</script>
</html>