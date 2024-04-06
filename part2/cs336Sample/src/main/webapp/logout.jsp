<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<%
session.invalidate();
response.sendRedirect("Landing.jsp"); // Redirect to the login page
%>
