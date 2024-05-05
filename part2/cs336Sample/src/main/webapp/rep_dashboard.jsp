<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Representative Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h1>Customer Representative Dashboard</h1>

    <!-- Section for managing user queries -->
    <h2>Manage User Queries</h2>
    <form action="RepServlet" method="post">
        <input type="hidden" name="action" value="manageQueries">
        <input type="submit" value="View Queries">
    </form>

    <!-- Section for editing or deleting user accounts -->
    <h2>Edit/Delete User Accounts</h2>
    <form action="RepServlet" method="post">
        <input type="hidden" name="action" value="editUsers">
        <input type="submit" value="Manage Users">
    </form>

    <!-- Section for managing bids -->
    <h2>Manage Bids</h2>
    <form action="RepServlet" method="post">
        <input type="hidden" name="action" value="manageBids">
        <input type="submit" value="View/Remove Bids">
    </form>

    <!-- Section for removing auctions -->
    <h2>Remove Auctions</h2>
    <form action="RepServlet" method="post">
        <input type="hidden" name="action" value="removeAuctions">
        <input type="submit" value="Manage Auctions">
    </form>

</body>
</html>
