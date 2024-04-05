<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<%
session.invalidate();
response.sendRedirect("HelloWorld.jsp"); // Redirect to the login page
%>
