<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Item" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search Results</title>
</head>
<body>
    <h1>Search Results</h1>
    <form action="SearchServlet" method="get">
        <input type="text" name="query" placeholder="Enter your search query">
        <button type="submit">Search</button>
    </form>
    <hr>
    <table border="1">
        <tr>
            <th>Title</th>
            <th>Starting Price</th>
            <th>Closing Time</th>
        </tr>
        <%
            List<Item> searchResults = (List<Item>) request.getAttribute("searchResults");
            if (searchResults != null && !searchResults.isEmpty()) {
                for (Item item : searchResults) {
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
