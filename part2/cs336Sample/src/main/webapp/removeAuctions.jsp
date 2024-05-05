<%@ page import="com.cs336.dao.Item"%>
<%@ page import="java.util.List" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Remove Auctions</title>
</head>
<body>
    <h2>Remove Auctions</h2>
    
    <%-- Displaying auctions --%>
    <% List<Item> items = (List<Item>) request.getAttribute("items"); %>
    <% if (items != null && !items.isEmpty()) { %>
        <form action="RemoveSelectedAuctionsServlet" method="post">
            <table>
                <tr>
                    <th>Item ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Starting Price</th>
                    <th>Current Bid</th>
                    <th>Status</th>
                    <th>Select</th>
                </tr>
                <% for (Item item : items) { %>
                    <tr>
                        <td><%= item.getItemId() %></td>
                        <td><%= item.getTitle() %></td>
                        <td><%= item.getDescription() %></td>
                        <td><%= item.getStartingPrice() %></td>
                        <td><%= item.getCurrentBid() %></td>
                        <td><%= item.getStatus() %></td>
                        <td><input type="checkbox" name="selectedItems" value="<%= item.getItemId() %>"></td>
                    </tr>
                <% } %>
            </table>
            <input type="submit" value="End Selected Auctions">
        </form>
    <% } else { %>
        <p>No auctions available to end.</p>
    <% } %>

    <%-- Optionally, provide a link or button to navigate back to the dashboard --%>
    <a href="rep_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
