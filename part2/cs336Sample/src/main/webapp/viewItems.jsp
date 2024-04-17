<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Item" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Items</title>
</head>
<body>
    <h1>Active Auctions</h1>
    <%
        List<Item> items = (List<Item>)request.getAttribute("items");
        if (items != null) {
            for (Item item : items) {
    %>
                <p><%= item.getTitle() %> - $<%= item.getStartingPrice() %> - Closes on: <%= item.getClosingTime() %></p>
    <%
            }
        }
    %>
</body>
</html>
