<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar"><strong>Lost &amp; Found Portal</strong><span><a href="index.jsp">Home</a></span></div>
<div class="container">
    <div class="card narrow">
        <h2>Student Registration</h2>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) { %><div class="error"><%= error %></div><% }
        %>
        <form action="register" method="post">
            <label>Full Name</label>
            <input type="text" name="name" required>
            <label>Email</label>
            <input type="email" name="email" required>
            <label>Username</label>
            <input type="text" name="username" required>
            <label>Password</label>
            <input type="password" name="password" required>
            <button class="btn btn-green" type="submit">Register</button>
        </form>
        <p>Already registered? <a href="login.jsp">Login</a></p>
    </div>
</div>
</body>
</html>
