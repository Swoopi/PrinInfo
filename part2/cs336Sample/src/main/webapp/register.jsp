<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.cs336.dao.*, com.cs336.servlet.*, com.cs336.utils.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Register New Account</title>
</head>
<body>
    <h2>Register</h2>
    <form action="RegisterServlet" method="post">
    Username: <input type="text" name="username" required><br>
    Password: <input type="password" name="password" required><br>
    <input type="hidden" name="role" value="customer_rep">
    <input type="submit" value="Create Account">
</form>

    </form>
</body>
</html>
