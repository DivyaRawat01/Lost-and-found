<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.lostfound.Item" %>
<%
    if (!"user".equals(session.getAttribute("role"))) { response.sendRedirect("login.jsp"); return; }

    // Keep the chosen type (from dashboard link or after a validation error)
    String type = request.getParameter("type");
    if (!"Found".equals(type)) { type = "Lost"; }
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Report Item</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <strong>Lost &amp; Found Portal</strong>
    <span><a href="dashboard.jsp">Dashboard</a><a href="logout">Logout</a></span>
</div>
<div class="container">
    <div class="card narrow" style="max-width:560px">
        <h2>Report an Item</h2>
        <% if (error != null) { %><div class="error"><%= error %></div><% } %>
        <form action="reportItem" method="post">
            <label>Type</label>
            <select name="type">
                <option value="Lost"  <%= "Lost".equals(type)  ? "selected" : "" %>>Lost</option>
                <option value="Found" <%= "Found".equals(type) ? "selected" : "" %>>Found</option>
            </select>
            <label>Item Name</label>
            <input type="text" name="itemName" value="<%= Item.esc(request.getParameter("itemName")) %>" required>
            <label>Description</label>
            <textarea name="description"><%= Item.esc(request.getParameter("description")) %></textarea>
            <label>Location</label>
            <input type="text" name="location" value="<%= Item.esc(request.getParameter("location")) %>" required>
            <label>Date</label>
            <input type="date" name="date" value="<%= Item.esc(request.getParameter("date")) %>" required>
            <button class="btn" type="submit">Submit Report</button>
        </form>
    </div>
</div>
</body>
</html>
