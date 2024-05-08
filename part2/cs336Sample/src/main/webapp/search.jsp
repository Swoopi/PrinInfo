<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Item" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title>Search and Browse Items</title>
</head>
<body>
    <h1>Search Auctions</h1>
    <form action="SearchServlet" method="post">
        <input type="text" name="query" required>
        <select name="searchType">
            <option value="itemSearch">Search Items</option>
            <option value="bidHistory">Bid History</option>
            <option value="userAuctions">User Auctions</option>
        </select>
        <select name="sortBy">
            <option value="current_bid">Current Bid</option>
            <option value="starting_price">Starting Price</option>
            <option value="item_type">Item Type</option>
        </select>
        <select name="sortOrder">
            <option value="ASC">Ascending</option>
            <option value="DESC">Descending</option>
        </select>
        <input type="submit" value="Search">
    </form>

    <%
        List<Item> items = (List<Item>) session.getAttribute("searchResults");
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                out.println("<p>" + item.getTitle() + " - $" + item.getCurrentBid() + " - " + item.getItemType() + " - Closes: " + item.getClosingTime() + "</p>");
            }
        } else {
            out.println("<p>No results found.</p>");
        }
    %>
</body>
</html>
