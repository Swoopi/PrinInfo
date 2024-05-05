package com.cs336.servlet;

import com.cs336.dao.Item;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RemoveSelectedAuctionsServlet")
public class RemoveSelectedAuctionsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] selectedItems = request.getParameterValues("selectedItems");
        if (selectedItems != null) {
            for (String itemId : selectedItems) {
                try {
                    int id = Integer.parseInt(itemId);
                    Item.removeAuction(id);
                } catch (NumberFormatException | SQLException e) {
                    // Handle exceptions
                    e.printStackTrace();
                }
            }
        }
        response.sendRedirect("rep_dashboard.jsp"); // Redirect back to the dashboard
    }
}
