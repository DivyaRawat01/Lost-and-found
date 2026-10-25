package com.lostfound;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Handles login for both students (database) and the admin (fixed login). */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L; 

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean adminLogin = "admin".equals(request.getParameter("role"));

        if (username == null || password == null) {
            fail(request, response, adminLogin, "Enter username and password.");
            return;
        }
        username = username.trim();

        // ---------- Admin ----------
        if (adminLogin) {
            if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("role", "admin");
                session.setAttribute("name", "Admin");
                response.sendRedirect("viewItems");      // loads data, then shows adminDashboard.jsp
            } else {
                fail(request, response, true, "Invalid admin credentials.");
            }
            return;
        }

        // ---------- Student ----------
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id, name FROM users WHERE username = ? AND password = ?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", rs.getInt("id"));
                    session.setAttribute("name", rs.getString("name"));
                    session.setAttribute("role", "user");
                    response.sendRedirect("dashboard.jsp");
                } else {
                    fail(request, response, false, "Invalid username or password.");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void fail(HttpServletRequest request, HttpServletResponse response,
                      boolean adminLogin, String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        RequestDispatcher rd = request.getRequestDispatcher(adminLogin ? "adminLogin.jsp" : "login.jsp");
        rd.forward(request, response);
    }
}
