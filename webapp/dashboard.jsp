<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.lostfound.Item" %>
<%
    if (!"user".equals(session.getAttribute("role"))) { response.sendRedirect("login.jsp"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <strong>Lost &amp; Found Portal</strong>
    <span>Hello, <%= Item.esc((String) session.getAttribute("name")) %> <a href="logout">Logout</a></span>
</div>
<div class="container">
    <div class="card">
        <h2>Student Dashboard</h2>
        <p>What would you like to do?</p>
        <a class="btn btn-red" href="reportItem.jsp?type=Lost">Report a Lost Item</a>
        <a class="btn btn-green" href="reportItem.jsp?type=Found">Report a Found Item</a>
        <a class="btn" href="viewItems">View / Search Items</a>
    </div>
</div>
</body>
</html>
