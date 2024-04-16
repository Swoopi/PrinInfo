<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<%
session.invalidate();
response.sendRedirect("landing.jsp"); // Redirect to the login page
%>
