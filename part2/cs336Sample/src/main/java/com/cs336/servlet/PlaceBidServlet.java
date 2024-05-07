package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import com.cs336.dao.ItemDAO;
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
        try {
            boolean isUpdated = itemDAO.updateBid(itemId, userId, bidAmount);
            if (isUpdated) {
                response.sendRedirect("ViewItemsServlet");
            } else {
            	System.out.println("HERE 1");

                request.setAttribute("errorMessage", "Bid too low or unable to place bid.");
                request.getRequestDispatcher("errorPage.jsp").forward(request, response);
            }
        } catch (Exception e) {
        	System.out.println("HERE 2");
            e.printStackTrace();  // Log the exception
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }
}
