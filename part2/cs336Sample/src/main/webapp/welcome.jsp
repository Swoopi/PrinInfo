<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Welcome Page</title>
</head>
<body>
    <%
        // Check if the 'user' session attribute is set
        String user = (String) session.getAttribute("user");
    
    	//want to put a bunch of hyperlinks here and maybe this is the place where you can browse products
        if (user != null && !user.trim().isEmpty()) {
            // User is logged in
            out.println("<h1>Welcome, " + user + "!</h1>");
            out.println("<p>You are logged in.</p>");
			
			// Add a logout button or link
            out.println("<a href='logout.jsp'>Logout</a>");
        } else {
            // User is not logged in or session has expired
            out.println("<h1>You are not logged in.</h1>");
            out.println("<p><a href='login.jsp'>Login</a></p>");
        }
    	
    %> 
    <ul>
    <li>
    Buy: <input type="submit" value="Buy">
    </li>
    <li>
	  Sell: <input type="submit" value="Sell">
	  </li>
	  </ul>
	  
</body>
</html>
