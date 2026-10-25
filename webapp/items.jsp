<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.lostfound.Item" %>
<%
    if (!"user".equals(session.getAttribute("role"))) { response.sendRedirect("login.jsp"); return; }

    @SuppressWarnings("unchecked")
    List<Item> items = (List<Item>) request.getAttribute("items");
    if (items == null) { response.sendRedirect("viewItems"); return; }   // opened directly

    String q = request.getParameter("q");
    String typeFilter = request.getParameter("type");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Reported Items</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <strong>Lost &amp; Found Portal</strong>
    <span><a href="dashboard.jsp">Dashboard</a><a href="logout">Logout</a></span>
</div>
<div class="container">
    <% if ("reported".equals(request.getParameter("msg"))) { %>
        <div class="success">Your report has been submitted.</div>
    <% } %>

    <div class="card">
        <h2>Search Items</h2>
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
        <h2>Reported Items (<%= items.size() %>)</h2>
        <% if (items.isEmpty()) { %>
            <p>No items found.</p>
        <% } else { %>
        <table>
            <tr><th>Type</th><th>Item</th><th>Description</th><th>Location</th><th>Date</th><th>Status</th></tr>
            <% for (Item it : items) { %>
            <tr>
                <td><span class="badge <%= it.getType().toLowerCase() %>"><%= it.getType() %></span></td>
                <td><%= Item.esc(it.getItemName()) %></td>
                <td><%= Item.esc(it.getDescription()) %></td>
                <td><%= Item.esc(it.getLocation()) %></td>
                <td><%= it.getDate() %></td>
                <td><span class="badge <%= it.getStatus().toLowerCase() %>"><%= it.getStatus() %></span></td>
            </tr>
            <% } %>
        </table>
        <% } %>
    </div>
</div>
</body>
</html>
