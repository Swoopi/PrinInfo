<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
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
            <th>Description</th>
            <th>Starting Price</th>
            <th>Bid Increment</th>
            <th>Minimum Price</th>
            <th>Closing Time</th>
        </tr>
        <%
            ResultSet items = (ResultSet) request.getAttribute("items");
            try {
                while (items != null && items.next()) {
                    %>
                    <tr>
                        <td><%= items.getString("title") %></td>
                        <td><%= items.getString("description") %></td>
                        <td><%= items.getDouble("starting_price") %></td>
                        <td><%= items.getDouble("bid_increment") %></td>
                        <td><%= items.getDouble("minimum_price") %></td>
                        <td><%= items.getTimestamp("closing_time").toString() %></td>
                    </tr>
                    <%
                }
            } catch (SQLException e) {
                out.println("Error retrieving data: " + e.getMessage());
            }
        %>
    </table>
    <a href="welcome.jsp">Back to Home</a>
</body>
</html>
