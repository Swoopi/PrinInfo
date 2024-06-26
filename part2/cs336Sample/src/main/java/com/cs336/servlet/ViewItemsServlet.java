package com.cs336.servlet;

import com.cs336.dao.ItemDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/ViewItemsServlet")
public class ViewItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ItemDAO itemDAO = new ItemDAO();
        request.setAttribute("items", itemDAO.getActiveItems());
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewItems.jsp");
        dispatcher.forward(request, response);
    }

  

}
