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
            String validationQuery = "SELECT minimum_price, (current_bid IS NULL OR current_bid < ?) AS can_bid FROM items WHERE item_id = ?";
            PreparedStatement validationStmt = con.prepareStatement(validationQuery);
            validationStmt.setDouble(1, bidAmount);
            validationStmt.setInt(2, itemId);
            ResultSet rs = validationStmt.executeQuery();
            
            boolean canPlaceBid = false;
            double minimumPrice = 0.0;
            if (rs.next()) {
                canPlaceBid = rs.getBoolean("can_bid");
                minimumPrice = rs.getDouble("minimum_price");
            }

            if (canPlaceBid && bidAmount >= minimumPrice) {
                String updateQuery = "UPDATE items SET current_bid = ?, current_bid_user_id = ? WHERE item_id = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setDouble(1, bidAmount);
                updateStmt.setInt(2, userID);
                updateStmt.setInt(3, itemId);
                int affectedRows = updateStmt.executeUpdate();

                if (affectedRows > 0) {
                    session.setAttribute("message", "Bid successfully placed!");
                } else {
                    session.setAttribute("error", "No changes made, please try again.");
                }
            } else {
                session.setAttribute("error", "Bid too low or does not meet the minimum price of $" + minimumPrice);
            }
            response.sendRedirect("ViewItemsServlet");
        } catch (SQLException e) {
            session.setAttribute("error", "Database error while placing bid: " + e.getMessage());
            response.sendRedirect("ViewItemsServlet");
        }
    }
}
