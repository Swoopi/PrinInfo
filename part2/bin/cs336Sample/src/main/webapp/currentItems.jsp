<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Item" %>
<!DOCTYPE html>
<html>
<head>
    <title>Current Items</title>
</head>
<body>
    <h1>Items Currently Up for Auction</h1>
    <table border="1">
        <tr>
            <th>Title</th>
            <th>Starting Price</th>
            <th>Closing Time</th>
        </tr>
        <%
            List<Item> items = (List<Item>) request.getAttribute("items");
            if (items != null && !items.isEmpty()) {
                for (Item item : items) {
                    %>
                    <tr>
                        <td><%= item.getTitle() %></td>
                        <td><%= item.getStartingPrice() %></td>
                        <td><%= item.getClosingTime().toString() %></td>
                        
                        
                    </tr>
                    <%
                }
            } else {
                %>
                <tr>
                    <td colspan="3">No items found.</td>
                </tr>
                <%
            }
        %>
    </table>
    <a href="welcome.jsp">Back to Home</a>
</body>
</html>
