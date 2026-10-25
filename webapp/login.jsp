<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar"><strong>Lost &amp; Found Portal</strong><span><a href="index.jsp">Home</a></span></div>
<div class="container">
    <div class="card narrow">
        <h2>Student Login</h2>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) { %><div class="error"><%= error %></div><% }
            if ("registered".equals(request.getParameter("msg"))) { %>
                <div class="success">Registration successful. Please log in.</div><% }
        %>
        <form action="login" method="post">
            <input type="hidden" name="role" value="user">
            <label>Username</label>
            <input type="text" name="username" required>
            <label>Password</label>
            <input type="password" name="password" required>
            <button class="btn" type="submit">Login</button>
        </form>
        <p>New student? <a href="register.jsp">Register here</a></p>
    </div>
</div>
</body>
</html>
