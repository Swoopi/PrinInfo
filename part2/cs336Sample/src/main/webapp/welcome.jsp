<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Welcome Page</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <%
        String user = (String) session.getAttribute("username");
        if (user != null && !user.trim().isEmpty()) {
            out.println("<h1>Welcome, " + user + "!</h1>");
            out.println("<p>You are logged in.</p>");
            // Add logout button
            out.println("<form action='LogoutServlet' method='get'>");
            out.println("<input type='submit' value='Logout'>");
            out.println("</form>");
    %>
            <!-- Buy and Sell Buttons -->
            <div>
                <form action="ViewItemsServlet" method="get">
                    <input type="submit" value="Buy Items">
                </form>
                <form action="ListItem.jsp" method="get">
                    <input type="submit" value="Sell Items">
                </form>
                <form action="CurrentItemsServlet" method="get">
                    <input type="submit" value="See personal Active Items">
                </form>
            </div>
    <%
        } else {
            // User is not logged in or session has expired
            response.sendRedirect("landing.jsp"); // Redirect to login page
        }
    %>
    <%-- Include the navigation bar --%>
    <%@ include file="navbar.jsp" %>
    
    <!-- Your page-specific content here -->
    <p>This is the main content of the page.</p>
</body>
</html>
