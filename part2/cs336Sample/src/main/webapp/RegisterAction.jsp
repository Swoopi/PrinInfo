<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="com.cs336.pkg.ApplicationDB" %>

<%
// Initialization of variables
String username = request.getParameter("username");
String password = request.getParameter("password");
boolean userExists = false;

ApplicationDB db = new ApplicationDB();

// Try-with-resources to auto close resources
try (Connection conn = db.getConnection();
     PreparedStatement checkUser = conn.prepareStatement("SELECT username FROM users WHERE username = ?");
     PreparedStatement insertUser = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

    // Check if user already exists
    checkUser.setString(1, username);
    try (ResultSet rs = checkUser.executeQuery()) {
        if (rs.next()) {
            userExists = true;
        }
    }

    // If user does not exist, insert new user
    if (!userExists) {
        insertUser.setString(1, username);
        insertUser.setString(2, password); // In a real app, hash and salt the password!
        int result = insertUser.executeUpdate();

        if (result > 0) {
            // Registration successful
            out.println("<h2>Registration successful!</h2>");
            // Redirect or link to login page
            response.setHeader("Refresh", "5; URL=HelloWorld.jsp");
            out.println("<p>You will be redirected to the login page in 5 seconds.</p>");
            out.println("<p>Click <a href='login.jsp'>here</a> if you are not redirected.</p>");
        } else {
            // Registration failed
            out.println("<h2>Registration failed. Please try again.</h2>");
            response.setHeader("Refresh", "5; URL=register.jsp");
            out.println("<p>You will be redirected back to the registration page in 5 seconds.</p>");
            out.println("<p>Click <a href='register.jsp'>here</a> if you are not redirected.</p>");
        }
    } else {
        // User already exists
        out.println("<h2>Username already exists. Please choose a different one.</h2>");
        response.setHeader("Refresh", "5; URL=register.jsp");
        out.println("<p>You will be redirected back to the registration page in 5 seconds.</p>");
        out.println("<p>Click <a href='register.jsp'>here</a> if you are not redirected.</p>");
    }
} catch (SQLException e) {
    e.printStackTrace();
    out.println("<h2>Error during the registration process.</h2>");
    // Handle exception: could redirect to an error page, log the error, etc.
}
%>
