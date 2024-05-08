package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import com.cs336.dao.Bid;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ManageBidsServlet")
public class ManageBidsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        

        List<Bid> bids = new ArrayList<>();
        try (Connection con = new ApplicationDB().getConnection()) {
            String selectQuery = "SELECT bid_id, item_id, user_id, bid_type, bid_amount, auto_increment, auto_limit, bid_time FROM bids";
            try (PreparedStatement selectStmt = con.prepareStatement(selectQuery);
                 ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    bids.add(new Bid(
                        rs.getInt("bid_id"),
                        rs.getInt("item_id"),
                        rs.getInt("user_id"),
                        rs.getString("bid_type"),
                        rs.getDouble("bid_amount"),
                        rs.getDouble("auto_increment"),
                        rs.getDouble("auto_limit"),
                        rs.getTimestamp("bid_time")
                       
                    ));
                    
                }
            }
            request.setAttribute("bids", bids);
            request.getRequestDispatcher("manageBids.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("error", "Database error while fetching bids: " + e.getMessage());
            response.sendRedirect("landing.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("deleteBids".equals(action)) {
            deleteSelectedBids(request, response);
        }
    }

    private void deleteSelectedBids(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] selectedBids = request.getParameterValues("selectedBids");
        if (selectedBids == null || selectedBids.length == 0) {
            request.getSession().setAttribute("error", "No bids selected for deletion.");
            response.sendRedirect("ManageBidsServlet");
            return;
        }

        try (Connection con = new ApplicationDB().getConnection()) {
            con.setAutoCommit(false); // Start transaction
            String deleteQuery = "DELETE FROM bids WHERE bid_id = ?";
            try (PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)) {
                for (String bidId : selectedBids) {
                    deleteStmt.setInt(1, Integer.parseInt(bidId));
                    deleteStmt.addBatch();
                }
                int[] affectedRows = deleteStmt.executeBatch();
                con.commit(); // Commit transaction

                if (affectedRows.length > 0) {
                    request.getSession().setAttribute("message", "Selected bids removed successfully!");
                } else {
                    request.getSession().setAttribute("error", "Failed to remove selected bids.");
                }
            } catch (SQLException e) {
                con.rollback(); // Rollback transaction on error
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Database error while removing bids: " + e.getMessage());
        }

        response.sendRedirect("ManageBidsServlet");
    }


}
