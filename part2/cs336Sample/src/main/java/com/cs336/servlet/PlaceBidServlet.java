package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/PlaceBidServlet")
public class PlaceBidServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        double bidAmount = Double.parseDouble(request.getParameter("bidAmount"));

        HttpSession session = request.getSession();
        Integer userID = (Integer) session.getAttribute("userID");
        if (userID == null) {
            response.sendRedirect("landing.jsp");
            return;
        }

        try (Connection con = new ApplicationDB().getConnection()) {
            // Fetch current bid and bid increment to validate against new bid
            String validationQuery = "SELECT current_bid, bid_increment FROM items WHERE item_id = ?";
            PreparedStatement validationStmt = con.prepareStatement(validationQuery);
            validationStmt.setInt(1, itemId);
            ResultSet rs = validationStmt.executeQuery();

            double currentBid = 0.0;
            double bidIncrement = 0.0;
            if (rs.next()) {
                currentBid = rs.getDouble("current_bid");
                bidIncrement = rs.getDouble("bid_increment");
            }

            // Check if new bid is at least current bid plus increment
            if (bidAmount >= (currentBid + bidIncrement)) {
                // Insert bid into bids table
                String insertBidQuery = "INSERT INTO bids (item_id, user_id, bid_amount) VALUES (?, ?, ?)";
                PreparedStatement insertBidStmt = con.prepareStatement(insertBidQuery);
                insertBidStmt.setInt(1, itemId);
                insertBidStmt.setInt(2, userID);
                insertBidStmt.setDouble(3, bidAmount);
                int affectedRows = insertBidStmt.executeUpdate();

                if (affectedRows > 0) {
                    session.setAttribute("message", "Bid successfully placed!");
                } else {
                    session.setAttribute("error", "No changes made, please try again.");
                }
            } else {
                // If the bid does not meet the required increment
                session.setAttribute("error", "Bid too low. It must be at least $" + (currentBid + bidIncrement) + ".");
            }
            response.sendRedirect("ViewItemsServlet");
        } catch (SQLException e) {
            session.setAttribute("error", "Database error while placing bid: " + e.getMessage());
            response.sendRedirect("ViewItemsServlet");
        }
    }
}
