package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import com.cs336.dao.ItemDAO;
import com.cs336.dao.AlertDAO;

@WebServlet("/PlaceBidServlet")
public class PlaceBidServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        double bidAmount = Double.parseDouble(request.getParameter("bidAmount"));
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userID");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        ItemDAO itemDAO = new ItemDAO();
        AlertDAO alertDAO = new AlertDAO();
        
        try {
            int previousBidderId = itemDAO.updateBid(itemId, userId, bidAmount);
            if (previousBidderId > 0 && previousBidderId != userId) {
                // Create an alert for the previous highest bidder
                String message = "You have been outbid on item #" + itemId;
                alertDAO.createAlert(previousBidderId, message, itemId, "bid_update");
            }
            response.sendRedirect("ViewItemsServlet");
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }
}
