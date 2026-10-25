package com.lostfound;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Lists / searches items (GET).
 *   q       -> keyword matched against item name, description and location
 *   type    -> Lost or Found filter
 *   editId  -> (admin only) also load one item so it can be edited
 * Students are forwarded to items.jsp, the admin to adminDashboard.jsp.
 */
@WebServlet("/viewItems")
public class ViewItemsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L; 
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        boolean isAdmin = "admin".equals(session.getAttribute("role"));

        String q = request.getParameter("q");
        String type = request.getParameter("type");

        // Build the query depending on the search filters
        StringBuilder sql = new StringBuilder("SELECT * FROM items WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND (item_name LIKE ? OR description LIKE ? OR location LIKE ?)");
            String like = "%" + q.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }
        if ("Lost".equals(type) || "Found".equals(type)) {
            sql.append(" AND type = ?");
            params.add(type);
        }
        sql.append(" ORDER BY date DESC, id DESC");

        List<Item> items = new ArrayList<>();
        Item editItem = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }

            // Admin clicked "Edit" on a row
            String editId = request.getParameter("editId");
            if (isAdmin && editId != null) {
                try (PreparedStatement ps2 = con.prepareStatement("SELECT * FROM items WHERE id = ?")) {
                    ps2.setInt(1, Integer.parseInt(editId));
                    try (ResultSet rs = ps2.executeQuery()) {
                        if (rs.next()) {
                            editItem = mapRow(rs);
                        }
                    }
                } catch (NumberFormatException ignored) {
                    // bad id in the URL - simply show no edit form
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }

        request.setAttribute("items", items);
        request.setAttribute("editItem", editItem);

        RequestDispatcher rd = request.getRequestDispatcher(isAdmin ? "adminDashboard.jsp" : "items.jsp");
        rd.forward(request, response);
    }

    private Item mapRow(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("type"),
                rs.getString("item_name"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getDate("date"),
                rs.getString("status"));
    }
}
