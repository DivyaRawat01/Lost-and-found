package com.lostfound;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Admin only. action=edit    -> update all fields of a report
 *             action=resolve -> mark a report as Resolved
 */
@WebServlet("/updateItem")
public class UpdateItemServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L; 
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("adminLogin.jsp");
            return;
        }

        String action = request.getParameter("action");

        try (Connection con = DBConnection.getConnection()) {
            int id = Integer.parseInt(request.getParameter("id"));

            if ("resolve".equals(action)) {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE items SET status = 'Resolved' WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                response.sendRedirect("viewItems?msg=resolved");

            } else if ("edit".equals(action)) {
                String type = request.getParameter("type");
                String status = request.getParameter("status");
                String itemName = request.getParameter("itemName");
                String location = request.getParameter("location");
                String dateStr = request.getParameter("date");

                boolean valid = ("Lost".equals(type) || "Found".equals(type))
                        && ("Open".equals(status) || "Resolved".equals(status))
                        && itemName != null && !itemName.trim().isEmpty()
                        && location != null && !location.trim().isEmpty();
                if (!valid) {
                    response.sendRedirect("viewItems?editId=" + id + "&msg=invalid");
                    return;
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE items SET type = ?, item_name = ?, description = ?, "
                                + "location = ?, date = ?, status = ? WHERE id = ?")) {
                    ps.setString(1, type);
                    ps.setString(2, itemName.trim());
                    ps.setString(3, request.getParameter("description"));
                    ps.setString(4, location.trim());
                    ps.setDate(5, Date.valueOf(dateStr));
                    ps.setString(6, status);
                    ps.setInt(7, id);
                    ps.executeUpdate();
                }
                response.sendRedirect("viewItems?msg=updated");

            } else {
                response.sendRedirect("viewItems");
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            response.sendRedirect("viewItems?msg=invalid");     // bad id or date
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
