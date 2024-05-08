<%@page import="com.cs336.dao.Query"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Queries</title>
</head>
<body>
    <h1>Manage Queries</h1>
    <% 
        List<Query> queries = (List<Query>) request.getAttribute("queries");
        if (queries != null && !queries.isEmpty()) {
            out.println("<ul>");
            for (Query query : queries) {
                out.println("<li>");
                out.println("<p>Query ID: " + query.getQueryId() + "</p>");
                out.println("<p>User ID: " + query.getUserId() + "</p>");
                out.println("<p>Content: " + query.getContent() + "</p>");
                if ("open".equals(query.getStatus())) {
                    out.println("<form action='QueryServlet' method='POST'>");
                    out.println("<input type='hidden' name='action' value='respondQuery'>");
                    out.println("<input type='hidden' name='queryId' value='" + query.getQueryId() + "'>");
                    out.println("<textarea name='response' rows='3' cols='30' required></required></textarea>");
                    out.println("<button type='submit'>Submit Response</button>");
                    out.println("</form>");
                }
                out.println("</li>");
            }
            out.println("</ul>");
        } else {
            out.println("<p>No open queries at the moment.</p>");
        }
    %>
</body>
</html>
