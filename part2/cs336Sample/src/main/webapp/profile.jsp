<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <h1>User Profile</h1>
    
    <h3>View your items</h3>
    <form action="ProfileServlet" method="get">
        <input type="hidden" name="action" value="viewItems">
        <input type="submit" value="View Items">
    </form>
    
    <h3>Change Username</h3>
    <form action="ProfileServlet" method="get">
        <input type="hidden" name="action" value="changeUsername">
        <input type="text" name="newUsername" placeholder="Enter new username" required>
        <input type="submit" value="Change Username">
    </form>
    
    <h3>Delete Account</h3>
    <form action="ProfileServlet" method="get" onsubmit="return confirm('Are you sure you want to delete your account? This cannot be undone.');">
        <input type="hidden" name="action" value="delete">
        <input type="submit" value="Delete Account">
    </form>
</body>
</html>
