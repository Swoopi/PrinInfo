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
            PreparedStatement ps = con.prepareStatement(
                    "SELECT userid, password FROM users WHERE username=?");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                int userId = rs.getInt("userid");
                
                if (password.equals(storedPassword)) { 
                    HttpSession session = request.getSession();
                    SessionManager.logInUser(session, username, userId);
                    response.sendRedirect("welcome.jsp");
                } else {
                    out.println("Invalid username or password");
                    response.sendRedirect("landing.jsp"); // Better to use forward with error message
                }
            } else {
                out.println("Username does not exist");
                response.sendRedirect("landing.jsp"); // Better to use forward with error message
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            out.close();
        }
    }
}
