<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.sql.ResultSet" %>
    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Bids</title>
</head>
<body>
    <h1>Manage Bids</h1>
    <form action="ManageBidsServlet" method="post">
        <table border="1">
            <tr>
                <th></th>
                <th>Bid ID</th>
                <th>Item ID</th>
                <th>User ID</th>
                <th>Bid Amount</th>
            </tr>
            <%
                ResultSet rs = (ResultSet) request.getAttribute("bidsResultSet");
                while (rs.next()) {
            %>
            <tr>
                <td><input type="checkbox" name="selectedBids" value="<%= rs.getInt("bid_id") %>"></td>
                <td><%= rs.getInt("bid_id") %></td>
                <td><%= rs.getInt("item_id") %></td>
                <td><%= rs.getInt("user_id") %></td>
                <td><%= rs.getDouble("bid_amount") %></td>
            </tr>
            <%
                }
            %>
        </table>
        <input type="submit" value="Delete Selected Bids">
    </form>
</body>
</html>
