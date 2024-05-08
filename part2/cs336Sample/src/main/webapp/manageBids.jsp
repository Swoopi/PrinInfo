<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Bid" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manage Bids</title>
</head>
<body>
    <h1>Manage Bids</h1>
    <table border="1">
        <tr>
            <th>Bid ID</th>
            <th>Item ID</th>
            <th>User ID</th>
            <th>Bid Type</th>
            <th>Bid Amount</th>
            <th>Auto Increment</th>
            <th>Auto Limit</th>
            <th>Bid Time</th>
        </tr>
        <%
            List<Bid> bids = (List<Bid>) request.getAttribute("bids");
            for (Bid bid : bids) {
        %>
        <tr>
            <td><%= bid.getBidId() %></td>
            <td><%= bid.getItemId() %></td>
            <td><%= bid.getUserId() %></td>
            <td><%= bid.getBidType() %></td>
            <td><%= bid.getBidAmount() %></td>
            <td><%= bid.getAutoIncrement() %></td>
            <td><%= bid.getAutoLimit() %></td>
            <td><%= bid.getBidTime() %></td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
