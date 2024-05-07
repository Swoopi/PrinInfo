<%@ page import="java.util.List"%>
<%@ page import="com.cs336.dao.AlertDAO"%>
<%@ page import="com.cs336.dao.Alert"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Alerts</title>
</head>
<body>
    <h1>Your Alerts</h1>
    <%
        Integer userId = (Integer) session.getAttribute("userID");
        if (userId == null) {
            out.println("<p>Please log in to view your alerts.</p>");
        } else {
            AlertDAO alertDAO = new AlertDAO();
            List<Alert> alerts = alertDAO.getAlertsForUser(userId);
            if (alerts != null && !alerts.isEmpty()) {
                for (Alert alert : alerts) {
                    out.println("<p>" + alert.getMessage() + "</p>");
                }
            } else {
                out.println("<p>No new alerts.</p>");
            }
        }
    %>
</body>
</html>
