package com.cs336.servlet;

import com.cs336.dao.ItemDAO;
import com.cs336.dao.Item;
import com.cs336.utils.SessionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionManager.isLoggedIn(session)) {
            response.sendRedirect("landing.jsp");
            return;
        }

        // Get the search query from the request
        String query = request.getParameter("query");

        // Perform search in the database
        ItemDAO itemDao = new ItemDAO();
        List<Item> searchResults = itemDao.searchItems(query);

        // Forward the search results to search.jsp
        request.setAttribute("searchResults", searchResults);
        request.getRequestDispatcher("search.jsp").forward(request, response);
    }
}
