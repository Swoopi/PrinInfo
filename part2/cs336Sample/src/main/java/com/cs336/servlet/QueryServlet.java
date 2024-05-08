package com.cs336.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import com.cs336.dao.Query;
import java.util.List;
import java.util.ArrayList;
import com.cs336.dao.ApplicationDB;
import com.cs336.utils.SessionManager;

@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Entering doGet of QueryServlet");
        ApplicationDB db = new ApplicationDB();
        List<Query> openQueries = new ArrayList<>();
        String sql = "SELECT * FROM user_queries WHERE status = 'open'";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Query query = new Query(
                    rs.getInt("query_id"),
                    rs.getInt("user_id"),
                    rs.getString("content"),
                    rs.getString("response"),
                    rs.getString("status")
                );
                openQueries.add(query);
            }
            System.out.println("Open queries fetched: " + openQueries.size());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
        request.setAttribute("queries", openQueries);
        RequestDispatcher dispatcher = request.getRequestDispatcher("support.jsp");
        dispatcher.forward(request, response);
        System.out.println("Dispatched to support.jsp with open queries");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("submitQuery".equals(action)) {
            submitQuery(request, response);
        } else if ("respondQuery".equals(action)) {
            respondQuery(request, response);
        }
    }
    



    private void submitQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); // Get session without creating a new one
        if (SessionManager.isLoggedIn(session)) {
            String content = request.getParameter("content");
            Integer userId = SessionManager.getLoggedInUserID(session);
            if (userId != null && !content.trim().isEmpty()) {
                ApplicationDB db = new ApplicationDB();
                try (Connection conn = db.getConnection()) {
                    String sql = "INSERT INTO user_queries (user_id, content, status) VALUES (?, ?, 'open')";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, userId);
                        ps.setString(2, content);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Database error: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
                    return;
                } finally {
                    // If using a method to close connection, use it here. Otherwise, connection is auto-closed
                }
            } else {
                System.out.println("Error: User ID is null or content is empty.");
            }
        } else {
            System.out.println("User not logged in.");
            response.sendRedirect("login.jsp"); // Redirect to login page if not logged in
            return;
        }
        response.sendRedirect("support.jsp"); // Redirect back to the support page
    }

    private void respondQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); // Get session without creating a new one
        if (SessionManager.isLoggedIn(session)) {
            int queryId = Integer.parseInt(request.getParameter("queryId"));
            String responseText = request.getParameter("response");
            if (responseText != null && !responseText.trim().isEmpty()) {
                ApplicationDB db = new ApplicationDB();
                try (Connection conn = db.getConnection()) {
                    String sql = "UPDATE user_queries SET response = ?, status = 'closed' WHERE query_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, responseText);
                        ps.setInt(2, queryId);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Database error: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
                    return;
                } finally {
                    // If using a method to close connection, use it here. Otherwise, connection is auto-closed
                }
            } else {
                System.out.println("Error: Response text is null or empty.");
            }
        } else {
            System.out.println("User not logged in.");
            response.sendRedirect("login.jsp"); // Redirect to login page if not logged in
            return;
        }
        response.sendRedirect("support.jsp"); // Redirect back to the support page
    }
}
