package com.cs336.servlet;


import com.cs336.dao.ItemDAO;
import com.cs336.dao.Item;
import com.cs336.utils.SessionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;


@WebServlet("/ViewUserBidsServlet")
public class ViewUserBidsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userID");

        if (userId == null) {
            response.sendRedirect("login.jsp"); // Redirect to login if not logged in
            return;
        }

        response.sendRedirect("bids.jsp");
    }
}
