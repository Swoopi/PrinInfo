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

        function toggleAutoBidFields(itemId) {
            var bidTypeSelect = document.getElementById("bidType" + itemId);
            var autoBidFields = document.getElementById("autoBidFields" + itemId);
            if (bidTypeSelect.value === "AUTOMATIC") {
                autoBidFields.style.display = 'block';
            } else {
                autoBidFields.style.display = 'none';
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
                    <select name="bidType" id="bidType<%= item.getItemId() %>" onchange="toggleAutoBidFields(<%= item.getItemId() %>)">
                        <option value="MANUAL">Manual Bid</option>
                        <option value="AUTOMATIC">Automatic Bid</option>
                    </select>
                    <input type="number" name="bidAmount" min="<%= item.getCurrentBid() + item.getBidIncrement() %>" step="0.01" required>
                    <div id="autoBidFields<%= item.getItemId() %>" style="display: none;">
                        <input type="number" name="incAmount" placeholder="Auto Increment ($)" step="0.01" min="0.01">
                        <input type="number" name="autoLimit" placeholder="Auto Limit ($)" min="<%= item.getCurrentBid() + item.getBidIncrement() %>">
                    </div>
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
