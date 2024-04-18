package com.cs336.servlet;

import com.cs336.dao.UserDAO;
import com.cs336.utils.SessionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || SessionManager.getLoggedInUserID(session) == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            deleteUser(session, request, response);
        } else if ("changeUsername".equals(action)) {
            String newUsername = request.getParameter("newUsername");
            changeUsername(session, newUsername, response);
        } else if ("viewItems".equals(action)) {
            response.sendRedirect("CurrentItemsServlet");
        }
    }

    private void deleteUser(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDAO userDAO = new UserDAO();
        Integer userID = SessionManager.getLoggedInUserID(session);
        userDAO.deleteUser(userID);
        SessionManager.logOutUser(session);
        response.sendRedirect("landing.jsp");
    }

    private void changeUsername(HttpSession session, String newUsername, HttpServletResponse response) throws IOException {
        if (newUsername != null && !newUsername.isEmpty()) {
            UserDAO userDAO = new UserDAO();
            Integer userID = SessionManager.getLoggedInUserID(session);
            userDAO.updateUsername(userID, newUsername);
            session.setAttribute("user", newUsername);  // Update session with new username
            response.sendRedirect("profile.jsp");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid username");
        }
    }
}
