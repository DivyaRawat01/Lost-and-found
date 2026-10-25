<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar"><strong>Lost &amp; Found Portal</strong><span><a href="index.jsp">Home</a></span></div>
<div class="container">
    <div class="card narrow">
        <h2>Admin Login</h2>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) { %><div class="error"><%= error %></div><% }
        %>
        <form action="login" method="post">
            <input type="hidden" name="role" value="admin">
            <label>Username</label>
            <input type="text" name="username" required>
            <label>Password</label>
            <input type="password" name="password" required>
            <button class="btn btn-red" type="submit">Login as Admin</button>
        </form>
    </div>
</div>
</body>
</html>
