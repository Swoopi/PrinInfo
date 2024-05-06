<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cs336.dao.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit User Details</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h1>Edit User Information</h1>
    <% 
        User user = (User) request.getAttribute("user");
        if (user != null) {
    %>
        <form action="RepServlet" method="post">
            <input type="hidden" name="action" value="updateUser">
            <input type="hidden" name="userid" value="<%= user.getUserId() %>">

            <div>
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" value="<%= user.getUsername() %>" required>
            </div>

            <div>
                <label for="role">Role:</label>
                <select id="role" name="role" required>
                    <option value="admin" <%= user.getRole().equals("admin") ? "selected" : "" %>>Admin</option>
                    <option value="customer_rep" <%= user.getRole().equals("customer_rep") ? "selected" : "" %>>Customer Representative</option>
                    <option value="user" <%= user.getRole().equals("user") ? "selected" : "" %>>User</option>
                </select>
            </div>

            <div>
                <input type="submit" value="Update User">
            </div>
        </form>
    <% 
        } else {
    %>
        <p>User information is not available.</p>
    <% 
        }
    %>
    <a href="rep_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
