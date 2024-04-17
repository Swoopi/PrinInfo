package com.cs336.utils;

import javax.servlet.http.HttpSession;

public class SessionManager {

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }

    public static void logInUser(HttpSession session, String username) {
        if (session != null) {
            session.setAttribute("user", username);
        }
    }

    public static void logOutUser(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    public static String getLoggedInUser(HttpSession session) {
        return (session != null) ? (String) session.getAttribute("user") : null;
    }
}
