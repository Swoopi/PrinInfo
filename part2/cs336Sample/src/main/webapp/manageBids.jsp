<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Bid" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Bids</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h1>Edit/Delete Bids</h1>
    <form action="ManageBidsServlet" method="post">
        <table border="1">
            <tr>
                <th>Select</th>
                <th>Bid ID</th>
                <th>Item ID</th>
                <th>User ID</th>
                <th>Bid amount</th>
            </tr>
            <%
            List<Bid> bids = (List<Bid>) request.getAttribute("bids");
            if (bids == null || bids.isEmpty()) {
                %>
                <tr><td colspan="5">No bids found</td></tr>
                <%
            } else {
                for (Bid bid : bids) {
                    %>
                    <tr>
                        <td><input type="checkbox" name="selectedBids" value="<%= bid.getBidId() %>"></td>
                        <td><%= bid.getBidId() %></td>
                        <td><%= bid.getItemId() %></td>
                        <td><%= bid.getUserId() %></td>
                        <td><%= bid.getBidAmount() %></td>
                    </tr>
                    <%
                }
            }
            %>
        </table>
        <input type="hidden" name="action" value="deleteBids">
        <input type="submit" value="Delete Selected Bids">
    </form>
    <a href="rep_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
