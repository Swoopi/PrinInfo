<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<%
String username = request.getParameter("username");
String password = request.getParameter("password");

Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/FinalProject", "root", "openoct19");
PreparedStatement ps = con.prepareStatement(
        "SELECT * FROM users WHERE username=? AND password=?");
ps.setString(1, username);
ps.setString(2, password);

ResultSet rs = ps.executeQuery();
if (rs.next()) {
    session.setAttribute("user", username);
    out.println("Login successful!");
    response.sendRedirect("welcome.jsp"); // Redirect to another page on success
} else {
    out.println("Invalid username or password");
    response.sendRedirect("login.jsp"); // Redirect back to the login page on failure
}
%>
