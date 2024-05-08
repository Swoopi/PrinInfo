<%@ page import="java.util.List" %>
<%@ page import="com.cs336.dao.Query" %> <!-- Corrected import statement -->
<!DOCTYPE html>
<html>
<head>
    <title>Support Queries</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h1>Support Queries</h1>
    <h2>Ask a question</h2>
    <form action="QueryServlet" method="post">
        <textarea name="content" rows="4" cols="50" required></required></textarea><br/>
        <input type="hidden" name="action" value="submitQuery">
        <input type="submit" value="Submit Query">
    </form>

    <h2>Open Queries</h2>
    <form action="QueryServlet" method="post">
        <table border="1">
    <tr>
        <th>Query ID</th>
        <th>Content</th>
        <th>Response</th>
        <th>Respond</th>
    </tr>
    <%
    List<Query> queries = (List<Query>) request.getAttribute("queries");
    if (queries == null || queries.isEmpty()) {
        %>
        <tr><td colspan="4">No open queries</td></tr>
        <%
    } else {
        for (Query query : queries) {
            %>
            <tr>
                <td><%= query.getQueryId() %></td>
                <td><%= query.getContent() %></td>
                <td><%= query.getResponse() != null ? query.getResponse() : "No response yet" %></td>
                <td>
                    <!-- Respond form here if needed -->
                </td>
            </tr>
            <%
        }
    }
    %>
</table>

    </form>
    <a href="welcome.jsp">Back to Dashboard</a>
</body>
</html>
