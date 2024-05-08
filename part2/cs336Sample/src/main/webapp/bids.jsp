<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.ItemDAO, com.cs336.dao.Bid" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Bids</title>
</head>
<body>
    <h1>Your Bids</h1>
    <%
        ItemDAO itemDAO = new ItemDAO();
        // Assuming you have a way to get the user's ID (e.g., from session)
        int userId = (Integer)session.getAttribute("userID"); // Example, replace with actual user ID retrieval
        List<Bid> bids = itemDAO.getUserBids(userId);
        if (bids != null && !bids.isEmpty()) {
            out.println("<ul>");
            for (Bid bid : bids) {
                out.println("<li>" + bid.getItemId() + " - Bid Amount: $" + bid.getBidAmount() + " - Type: " + bid.getBidType() + " - Time: " + bid.getBidTime() + "</li>");
            }
            out.println("</ul>");
        } else {
            out.println("<p>No bids found.</p>");
        }
    %>
</body>
</html>
