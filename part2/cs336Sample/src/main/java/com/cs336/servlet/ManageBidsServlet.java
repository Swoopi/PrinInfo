package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/ManageBidsServlet")
public class ManageBidsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userRole = (Integer) session.getAttribute("userRole");
        if (userRole == null || userRole != 2) { // Assuming 2 represents the customer representative role
            response.sendRedirect("landing.jsp");
            return;
        }

        try (Connection con = new ApplicationDB().getConnection()) {
            // Fetch bids from the database
            String selectQuery = "SELECT bid_id, item_id, user_id, bid_amount FROM bids";
            PreparedStatement selectStmt = con.prepareStatement(selectQuery);
            ResultSet rs = selectStmt.executeQuery();
            
            // Forward bids data to the JSP page
            request.setAttribute("bidsResultSet", rs);
            request.getRequestDispatcher("manageBids.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("error", "Database error while fetching bids: " + e.getMessage());
            response.sendRedirect("landing.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userRole = (Integer) session.getAttribute("userRole");
        if (userRole == null || userRole != 2) { // Assuming 2 represents the customer representative role
            response.sendRedirect("landing.jsp");
            return;
        }

        String[] selectedBids = request.getParameterValues("selectedBids");
        if (selectedBids == null || selectedBids.length == 0) {
            session.setAttribute("error", "No bids selected for deletion.");
            response.sendRedirect("ManageBidsServlet");
            return;
        }

        try (Connection con = new ApplicationDB().getConnection()) {
            // Delete the selected bids from the database
            String deleteQuery = "DELETE FROM bids WHERE bid_id = ?";
            PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
            for (String bidId : selectedBids) {
                deleteStmt.setInt(1, Integer.parseInt(bidId));
                deleteStmt.addBatch();
            }
            int[] affectedRows = deleteStmt.executeBatch();

            // Check if any bid was deleted successfully
            boolean success = false;
            for (int rows : affectedRows) {
                if (rows > 0) {
                    success = true;
                    break;
                }
            }

            if (success) {
                session.setAttribute("message", "Selected bids removed successfully!");
            } else {
                session.setAttribute("error", "Failed to remove selected bids, please try again.");
            }
        } catch (SQLException e) {
            session.setAttribute("error", "Database error while removing bids: " + e.getMessage());
        }

        // Redirect back to the manage bids page
        response.sendRedirect("ManageBidsServlet");
    }
}
