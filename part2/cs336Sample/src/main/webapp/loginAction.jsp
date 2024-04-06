<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ page import="com.cs336.pkg.ApplicationDB" %>

<%
String username = request.getParameter("username");
String password = request.getParameter("password");

ApplicationDB db = new ApplicationDB();
try (Connection con = db.getConnection()) {
    PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM users WHERE username=? AND password=?");
    ps.setString(1, username);
    ps.setString(2, password);

    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
        session.setAttribute("user", username);
        response.sendRedirect("welcome.jsp"); // Redirect to another page on success
    } else {
        out.println("Invalid username or password");
        response.sendRedirect("Landing.jsp"); // Redirect back to the login page on failure
    }
} catch (Exception e) {
    e.printStackTrace();
    // Handle exception or redirect to an error page
}
%>
