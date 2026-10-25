<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.lostfound.Item" %>
<%
    if (!"admin".equals(session.getAttribute("role"))) { response.sendRedirect("adminLogin.jsp"); return; }

    @SuppressWarnings("unchecked")
    List<Item> items = (List<Item>) request.getAttribute("items");
    if (items == null) { response.sendRedirect("viewItems"); return; }   // opened directly
    Item editItem = (Item) request.getAttribute("editItem");

    String q = request.getParameter("q");
    String typeFilter = request.getParameter("type");

    // Map the ?msg= code to a fixed message
    String msg = request.getParameter("msg");
    String msgText = null;
    boolean msgIsError = false;
    if ("updated".equals(msg))       msgText = "Report updated.";
    else if ("resolved".equals(msg)) msgText = "Report marked as Resolved.";
    else if ("deleted".equals(msg))  msgText = "Report deleted.";
    else if ("invalid".equals(msg))  { msgText = "Invalid input. Please check the values and try again."; msgIsError = true; }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <strong>Lost &amp; Found Portal - Admin</strong>
    <span><a href="logout">Logout</a></span>
</div>
<div class="container">
    <% if (msgText != null) { %>
        <div class="<%= msgIsError ? "error" : "success" %>"><%= msgText %></div>
    <% } %>

    <% if (editItem != null) { %>
    <div class="card">
        <h3>Edit Report #<%= editItem.getId() %></h3>
        <form action="updateItem" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="id" value="<%= editItem.getId() %>">

            <label>Type</label>
            <select name="type">
                <option value="Lost"  <%= "Lost".equals(editItem.getType())  ? "selected" : "" %>>Lost</option>
                <option value="Found" <%= "Found".equals(editItem.getType()) ? "selected" : "" %>>Found</option>
            </select>
            <label>Item Name</label>
            <input type="text" name="itemName" value="<%= Item.esc(editItem.getItemName()) %>" required>
            <label>Description</label>
            <textarea name="description"><%= Item.esc(editItem.getDescription()) %></textarea>
            <label>Location</label>
            <input type="text" name="location" value="<%= Item.esc(editItem.getLocation()) %>" required>
            <label>Date</label>
            <input type="date" name="date" value="<%= editItem.getDate() %>" required>
            <label>Status</label>
            <select name="status">
                <option value="Open"     <%= "Open".equals(editItem.getStatus())     ? "selected" : "" %>>Open</option>
                <option value="Resolved" <%= "Resolved".equals(editItem.getStatus()) ? "selected" : "" %>>Resolved</option>
            </select>
            <button class="btn" type="submit">Save Changes</button>
            <a class="btn btn-red" href="viewItems">Cancel</a>
        </form>
    </div>
    <% } %>

    <div class="card">
        <h2>Search Reports</h2>
        <form class="search-bar" action="viewItems" method="get">
            <input type="text" name="q" placeholder="Item, description or location" value="<%= Item.esc(q) %>">
            <select name="type">
                <option value="">All</option>
                <option value="Lost"  <%= "Lost".equals(typeFilter)  ? "selected" : "" %>>Lost</option>
                <option value="Found" <%= "Found".equals(typeFilter) ? "selected" : "" %>>Found</option>
            </select>
            <button class="btn" type="submit">Search</button>
        </form>
    </div>

    <div class="card">
        <h2>All Reports (<%= items.size() %>)</h2>
        <% if (items.isEmpty()) { %>
            <p>No reports found.</p>
        <% } else { %>
        <table>
            <tr><th>ID</th><th>Type</th><th>Item</th><th>Location</th><th>Date</th><th>Status</th><th>Actions</th></tr>
            <% for (Item it : items) { %>
            <tr>
                <td><%= it.getId() %></td>
                <td><span class="badge <%= it.getType().toLowerCase() %>"><%= it.getType() %></span></td>
                <td><strong><%= Item.esc(it.getItemName()) %></strong><br><%= Item.esc(it.getDescription()) %></td>
                <td><%= Item.esc(it.getLocation()) %></td>
                <td><%= it.getDate() %></td>
                <td><span class="badge <%= it.getStatus().toLowerCase() %>"><%= it.getStatus() %></span></td>
                <td>
                    <a class="btn btn-small" href="viewItems?editId=<%= it.getId() %>">Edit</a>

                    <% if ("Open".equals(it.getStatus())) { %>
                    <form class="inline" action="updateItem" method="post">
                        <input type="hidden" name="action" value="resolve">
                        <input type="hidden" name="id" value="<%= it.getId() %>">
                        <button class="btn btn-small btn-green" type="submit">Resolve</button>
                    </form>
                    <% } %>

                    <form class="inline" action="deleteItem" method="post"
                          onsubmit="return confirm('Delete this report?');">
                        <input type="hidden" name="id" value="<%= it.getId() %>">
                        <button class="btn btn-small btn-red" type="submit">Delete</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
        <% } %>
    </div>
</div>
</body>
</html>
