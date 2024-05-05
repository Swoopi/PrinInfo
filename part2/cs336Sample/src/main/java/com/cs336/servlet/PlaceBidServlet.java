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
                
             // Retrieve the latest bid placed
                PreparedStatement nextBid = con.prepareStatement(
                        "SELECT userID, bid_increment, bid_limit FROM Bids WHERE auctionID = ? AND bid_type = 'AUTOMATIC' ORDER BY bid_limit DESC LIMIT 1;");
                nextBid.setInt(1, auctionId);
                ResultSet auto_b = nextBid.executeQuery();
                int auto_id = 0;
                double auto_inc = 0.0;
                double auto_lim = 0.0;
                if (auto_b.next())                		
                {
                	 auto_id = auto_b.getInt("userID");
                	 auto_inc = auto_b.getDouble("bid_increment");
                	 auto_lim = auto_b.getDouble("bid_limit");
                }
                
             // Retrieve the latest bid placed
                PreparedStatement lastLim = con.prepareStatement(
                        "SELECT bid_limit FROM Bids WHERE auctionID = ? AND bid_type = 'AUTOMATIC' ORDER BY bid_limit DESC LIMIT 1,1;");
                lastLim.setInt(1, auctionId);
                ResultSet lastLimRS = lastLim.executeQuery();
                
                double lastLimVal = 0.0;
                
                if (lastLimRS.next())                		
                {
                	 
                	 lastLimVal = lastLimRS.getDouble("bid_limit");
                }
                
                double nextAutoInc = lastLimVal + auto_inc;
                if (nextAutoInc <= auto_lim) {
                    // Insert the next auto-bid into the database
                    String insertAutoBidQuery = "INSERT INTO Bids (auctionID, userID, bid_amount, bid_type, bid_increment, bid_limit, post_time)"
                        + "VALUES (?, ?, ?, ?, ?, ?, NOW() )"; 
                    PreparedStatement insertAutoBidStmt = con.prepareStatement(insertAutoBidQuery);
                    
                    insertAutoBidStmt.setInt(1, auctionId);
                    insertAutoBidStmt.setInt(2, userID);
                    insertAutoBidStmt.setDouble(3, nextAutoInc);
                    insertAutoBidStmt.setString(4, "auto-post");
                    insertAutoBidStmt.setDouble(5, auto_inc);
                    insertAutoBidStmt.setDouble(6, auto_lim);
                    insertAutoBidStmt.executeUpdate();
                    con.commit();
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