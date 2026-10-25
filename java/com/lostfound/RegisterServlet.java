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

/** Registers a new student. */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L; 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (isBlank(name) || isBlank(email) || isBlank(username) || isBlank(password)) {
            forwardBack(request, response, "All fields are required.");
            return;
        }
        username = username.trim();

        try (Connection con = DBConnection.getConnection()) {

            // 1. username must be unique
            try (PreparedStatement check = con.prepareStatement(
                    "SELECT id FROM users WHERE username = ?")) {
                check.setString(1, username);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        forwardBack(request, response, "Username already taken. Choose another.");
                        return;
                    }
                }
            }

            // 2. insert the new user
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (name, email, username, password) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, name.trim());
                ps.setString(2, email.trim());
                ps.setString(3, username);
                ps.setString(4, password);
                ps.executeUpdate();
            }

            response.sendRedirect("login.jsp?msg=registered");

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void forwardBack(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
        rd.forward(request, response);
    }
}
