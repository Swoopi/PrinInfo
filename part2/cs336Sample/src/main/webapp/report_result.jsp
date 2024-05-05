<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Report Results</title>
</head>
<body>
    <h1>Report Results</h1>
    <h2>Results for <%= request.getAttribute("reportType") %></h2>
    <% 
        Object data = request.getAttribute("reportData");
        if (data instanceof List) {
            List<String> dataList = (List<String>) data;
            if (!dataList.isEmpty()) {
                out.println("<ul>");
                for (String item : dataList) {
                    out.println("<li>" + item + "</li>");
                }
                out.println("</ul>");
            } else {
                out.println("<p>No data to display.</p>");
            }
        } else if (data instanceof String) {
            out.println("<p>" + data + "</p>");
        } else if (data instanceof Double) {
            out.println("<p>$" + data + "</p>");
        } else {
            out.println("<p>No data to display.</p>");
        }
    %>
</body>
</html>
