package com.lostfound;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Saves a new lost / found report submitted by a logged-in student. */
@WebServlet("/reportItem")
public class ReportItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"user".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        String type = request.getParameter("type");
        String itemName = request.getParameter("itemName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String dateStr = request.getParameter("date");

        boolean validType = "Lost".equals(type) || "Found".equals(type);
        if (!validType || isBlank(itemName) || isBlank(location) || isBlank(dateStr)) {
            forwardBack(request, response, "Please fill in all required fields.");
            return;
        }

        Date date;
        try {
            date = Date.valueOf(dateStr);                 // expects yyyy-MM-dd
        } catch (IllegalArgumentException e) {
            forwardBack(request, response, "Invalid date.");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO items (user_id, type, item_name, description, location, date, status) "
                             + "VALUES (?, ?, ?, ?, ?, ?, 'Open')")) {

            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setString(3, itemName.trim());
            ps.setString(4, description == null ? "" : description.trim());
            ps.setString(5, location.trim());
            ps.setDate(6, date);
            ps.executeUpdate();

            response.sendRedirect("viewItems?msg=reported");

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
        RequestDispatcher rd = request.getRequestDispatcher("reportItem.jsp");
        rd.forward(request, response);
    }
}
