package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import com.cs336.utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        ApplicationDB db = new ApplicationDB();
        try (Connection con = db.getConnection()) {
            // Include role in the SELECT query
            PreparedStatement ps = con.prepareStatement(
                    "SELECT userid, password, role FROM users WHERE username=?");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");  // Make sure this matches the database column name exactly
                int userId = rs.getInt("userid");
                
                if (password.equals(storedPassword)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userid", userId);
                    session.setAttribute("username", username);
                    session.setAttribute("role", role);
                    
                    // Redirect based on role
                    switch (role) {
                        case "admin":
                            response.sendRedirect("admin_dashboard.jsp");
                            break;
                        case "customer_rep":
                            response.sendRedirect("rep_dashboard.jsp");
                            break;
                        default:
                            response.sendRedirect("welcome.jsp");
                            break;
                    }
                } else {
                    out.println("Invalid username or password");
                    // Using forward to keep the user on the login page with an error message
                    request.setAttribute("errorMessage", "Invalid username or password");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                out.println("Username does not exist");
                // Using forward to keep the user on the login page with an error message
                request.setAttribute("errorMessage", "Username does not exist");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            out.close();
        }
    }
}

