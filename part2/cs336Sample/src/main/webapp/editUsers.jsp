<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.User" %> 
<!DOCTYPE html>
<html>
<head>
    <title>Edit Users</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h1>Edit/Delete User Accounts</h1>
    <table border="1">
        <tr>
            <th>User ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <%
            List<User> users = (List<User>) request.getAttribute("users");
            if (users != null) {
                for (User user : users) {
        %>
            <tr>
                <td><%= user.getUserId() %></td>
                <td><%= user.getUsername() %></td>
                <td><%= user.getRole() %></td>
                <td>
                    <form action="RepServlet" method="post">
    					<input type="hidden" name="action" value="editUser">
    					<input type="hidden" name="userid" value="<%= user.getUserId() %>">
    					<input type="submit" value="Edit">
					</form>

                </td>
                <td>
                    <form action="RepServlet" method="post" onsubmit="return confirm('Are you sure you want to delete this user?');">
                        <input type="hidden" name="action" value="deleteUser">
                        <input type="hidden" name="userid" value="<%= user.getUserId() %>">
                        <input type="submit" value="Delete">
                    </form>
                </td>
            </tr>
        <%
                }
            } else {
        %>
            <tr><td colspan="5">No users found</td></tr>
        <%
            }
        %>
    </table>
    <a href="rep_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
