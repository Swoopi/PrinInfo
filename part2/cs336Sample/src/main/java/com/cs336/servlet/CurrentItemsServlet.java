package com.cs336.servlet;

import com.cs336.dao.ItemDAO;
import com.cs336.dao.Item;
import com.cs336.utils.SessionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/CurrentItemsServlet")
public class CurrentItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
        if (!SessionManager.isLoggedIn(session)) {
            response.sendRedirect("landing.jsp");
            return;
        }
        Integer userID = SessionManager.getLoggedInUserID(session);

        ItemDAO itemDao = new ItemDAO();
   
        List<Item> items = itemDao.getItemsByUserId(userID);
        request.setAttribute("items", items);
        request.getRequestDispatcher("currentItems.jsp").forward(request, response);
    }
}
