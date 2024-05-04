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
        int auctionId = Integer.parseInt(request.getParameter("auctionId"));
        String bidType = request.getParameter("bidType");
        double autoInc = Double.parseDouble(request.getParameter("incAmount"));
        double autoLimit = Double.parseDouble(request.getParameter("autoLimit"));
        double bidAmount = Double.parseDouble(request.getParameter("bidAmount"));

        HttpSession session = request.getSession();
        Integer userID = (Integer) session.getAttribute("userID");
        if (userID == null) {
            response.sendRedirect("landing.jsp");
            return;
        }

        try (Connection con = new ApplicationDB().getConnection()) {
            // Fetch current bid and bid increment to validate against new bid
            String validationQuery = "SELECT current_bid, bid_increment FROM Auctions WHERE auctionID = ?";
            PreparedStatement validationStmt = con.prepareStatement(validationQuery);
            validationStmt.setInt(1, auctionId);
            ResultSet rs = validationStmt.executeQuery();

            double currentBid = 0.0;
            double bidIncrement = 0.0;
            if (rs.next()) {
                currentBid = rs.getDouble("current_bid");
                bidIncrement = rs.getDouble("bid_increment");
            }

            // Check if new bid is at least current bid plus increment
            if (bidAmount >= (currentBid + bidIncrement)) {
                String updateQuery = "INSERT INTO Bids (auctionID, userID, bid_amount, bid_type, bid_increment, bid_limit, post_time)"
                + "VALUES (?, ?, ?, ?, ?, ?, NOW() )"; 
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                
                updateStmt.setInt(1, auctionId);
                updateStmt.setInt(2, userID);
                updateStmt.setDouble(3, bidAmount);
                updateStmt.setString(4, bidType);
                updateStmt.setDouble(5, autoInc);
                updateStmt.setDouble(6, autoLimit);
                updateStmt.executeUpdate();

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
