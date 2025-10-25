<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Check if user is already logged in
    if (session.getAttribute("user") != null) {
        response.sendRedirect("dashboard");
        return;
    }

    String error = (String) request.getAttribute("error");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SmartTaskTracker</title>
    <link rel="stylesheet" type="text/css" href="assets/css/login.css">
</head>
<body>
    <div class="container">
        <!-- Left Section -->
        <div class="left">
            <img src="<%=request.getContextPath()%>/assets/images/meeting.gif"
                 alt="Animated GIF"
                 style="margin-top: 55px; width:200px;">
            <h1>Welcome!</h1>
            <p>
                Organize your tasks, track progress, tackle issues,<br>
                and plan your day effortlessly here.....!!
            </p>
        </div>

        <!-- Right Section -->
        <div class="right">
            <div class="login-box">
                <h2>Let's Get Started!</h2>
                <p>Continue With Email</p>

                <!-- Success Message -->
                <% if (success != null) { %>
                    <div class="success-message"><%= success %></div>
                <% } %>

                <!-- Error Message -->
                <% if (error != null) { %>
                    <div class="error-message"><%= error %></div>
                <% } %>

                <!-- Login Form -->
                <form method="post" action="login">
                    <label class="input-label" for="emailInput">
                        Enter Your Email <span style="color:red;">*</span>
                    </label>
                    <input class="input-field"
                           id="emailInput"
                           name="email"
                           type="email"
                           required
                           placeholder="abc@gmail.com">

                    <label class="input-label" for="passwordInput">
                        Password <span style="color:red;">*</span>
                    </label>
                    <input class="input-field"
                           id="passwordInput"
                           name="password"
                           type="password"
                           required
                           placeholder="XXXXXXXX">

                    <button class="continue-btn" type="submit">Continue</button>

                    <div class="form-footer">
                        Don't have an account?
                        <a class="login-link" href="<%=request.getContextPath()%>/registration.jsp">SignIn</a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <span>©2025 Smart Task Tracker</span>
        </div>
    </div>
</body>
</html>
