<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Auction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Items</title>
    <script type="text/javascript">
        window.onload = function() {
            // Display messages from the session if available
            var message = '<%=session.getAttribute("message")%>';
            var error = '<%=session.getAttribute("error")%>';
            if (message && message !== 'null') {
                alert(message);
                <% session.removeAttribute("message"); %>
            } else if (error && error !== 'null') {
                alert(error);
                <% session.removeAttribute("error"); %>
            }
        }
    </script>
</head>
<body>
    <h1>Active Auctions</h1>
    <%
        List<Auction> auctions = (List<Auction>)request.getAttribute("auctions");
        if (auctions != null && !auctions.isEmpty()) {
            for (Auction auction : auctions) {
    %>
                <p>
                    <%= auction.getTitle() %> - Current bid: $<%= auction.getCurrentBid() %> 
                    - Starting Price: $<%= auction.getStartingPrice() %> 
                    - Closes on: <%= auction.getClosingTime() %>
                </p>
                <!-- Updated form action to submit to PlaceBidServlet -->
                <form action="PlaceBidServlet" method="post">
                    <input type="hidden" name="auctionId" value="<%= auction.getAuctionId() %>">
                    <select name="Bid Type">
                    	<option>MANUAL</option>
                    	<option>AUTOMATIC</option>
                    </select>
                    <input type="number" name="incAmount" min="0.00" step="0.01" required>
                    <input type="submit" value="Set Auto-Bid">
                    <input type="number" name="autoLimit" min="0.00" step="0.01" required>
                    <input type="submit" value="Set Auto-Bid Limit">
                    <input type="number" name="bidAmount" min="<%= auction.getCurrentBid() + auction.getBidIncrement() %>" step="0.01" required>
                    <input type="submit" value="Place Bid">
                </form>

    <%
            }
        } else {
    %>
        <p>No active items to display.</p>
    <%
        }
    %>
</body>
</html>

