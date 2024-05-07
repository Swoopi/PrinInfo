<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Item" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Items</title>
    <script type="text/javascript">
        window.onload = function() {
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
        List<Item> items = (List<Item>)request.getAttribute("items");
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {

    %>			
                <p>
                    <%= item.getTitle() %> - Current bid: $<%= item.getCurrentBid() %>
                    - Starting Price: $<%= item.getStartingPrice() %>
                    - Closes on: <%= item.getClosingTime() %>
                    - Bid Increment: $<%= item.getBidIncrement() %>
                    - Type: <%= item.getItemType() %>
                </p>
                <!-- Updated form action to submit to PlaceBidServlet -->
                <form action="PlaceBidServlet" method="post">
                    <input type="hidden" name="itemId" value="<%= item.getItemId() %>">
                    <input type="number" name="bidAmount" min="<%= item.getCurrentBid() + item.getBidIncrement() %>" step="0.01" required>
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
