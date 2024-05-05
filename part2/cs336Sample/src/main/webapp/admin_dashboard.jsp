
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css" /> <!-- Adjust the path as needed -->
</head>
<body>
    <h1>Admin Dashboard</h1>

    <% String successMessage = (String) session.getAttribute("successMessage");
       if (successMessage != null) {
           out.println("<p style='color: green;'>" + successMessage + "</p>");
           session.removeAttribute("successMessage"); // Remove attribute to prevent re-display on refresh
       }
    %>

    <h2>Create Customer Representative Account</h2>
    <form action="AdminServlet" method="post">
        <input type="hidden" name="action" value="createRep">
        Username: <input type="text" name="username" required><br>
        Password: <input type="password" name="password" required><br>
        <input type="submit" value="Create Account">
    </form>

   <h2>Reports</h2>
    <form action="AdminServlet" method="post">
        <input type="hidden" name="action" value="generateReport">
        <select name="reportType">
            <option value="totalEarnings">Total Earnings</option>
            <option value="earningsPerItem">Earnings Per Item</option>
            <option value="earningsPerItemType">Earnings Per Item Type</option>
            <option value="earningsPerUser">Earnings Per End-User</option>
            <option value="bestSellingItems">Best-Selling Items</option>
            <option value="bestBuyers">Best Buyers</option>
        </select>
        <input type="submit" value="Generate Report">
    </form>
    <!-- Add more functionality as needed -->
</body>
</html>
