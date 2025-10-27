<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.UUID" %>
<%
    String error = (String) request.getAttribute("error");

    // Generate CSRF token
    String csrfToken = UUID.randomUUID().toString();
    session.setAttribute("csrfToken", csrfToken);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration - SmartTaskTracker</title>
    <link rel="stylesheet" type="text/css" href="assets/css/registration.css">
</head>
<body>
    <div class="container">
        <div class="left">
            <img src="<%=request.getContextPath()%>/assets/images/meeting.gif" alt="Animated GIF" style="margin-top: 55px; width:200px;">
            <h1>Welcome!</h1>
            <p>Organize your tasks, track progress, tackle issues,<br>and plan your day effortlessly here.....!!</p>
        </div>

        <div class="right">
            <div class="registration-box">
                <h2>Registration Page</h2>
                <p>Create your account to get started</p>

                <% if (error != null) { %>
                    <div class="error-message"><c:out value="<%= error %>" escapeXml="true"/></div>
                <% } %>

                <form method="post" action="register" id="registrationForm">
                    <!-- CSRF Token -->
                    <input type="hidden" name="csrfToken" value="<%= csrfToken %>">

                    <div class="form-section-title">Personal Details</div>

                    <div class="form-group">
                        <label class="input-label">Name <span style="color:red;">*</span></label>
                        <input class="input-field" name="name" type="text" required
                               pattern="[A-Za-z\s]{2,50}"
                               title="Name should contain only letters and spaces"
                               placeholder="Enter your full name">
                    </div>

                    <div class="form-group">
                        <label class="input-label">Username <span style="color:red;">*</span></label>
                        <input class="input-field" name="username" type="text" required
                               pattern="[a-zA-Z0-9_]{3,20}"
                               title="Username: 3-20 characters, letters, numbers, underscore only"
                               placeholder="Create unique username">
                    </div>

                    <div class="form-group">
                        <label class="input-label">Email <span style="color:red;">*</span></label>
                        <input class="input-field" name="email" type="email" required
                               placeholder="abc@gmail.com">
                    </div>

                    <div class="form-group">
                        <label class="input-label">Password <span style="color:red;">*</span></label>
                        <input class="input-field" name="password" type="password" required
                               minlength="8"
                               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$"
                               title="Password: 8+ chars, 1 uppercase, 1 lowercase, 1 number, 1 special character"
                               placeholder="Create strong password">
                        <small style="color: #666; font-size: 12px;">8+ chars, uppercase, lowercase, number, special char</small>
                    </div>

                    <div class="form-section-title">Professional Details</div>

                    <div class="form-group">
                        <label class="input-label">Profession <span style="color:red;">*</span></label>
                        <input class="input-field" name="profession" type="text" required
                               placeholder="e.g., Software Developer">
                    </div>

                    <div class="form-group">
                        <label class="input-label">Company Name <span style="color:red;">*</span></label>
                        <input class="input-field" name="companyName" type="text" required
                               placeholder="Your company name">
                    </div>

                    <div class="form-group">
                        <label class="input-label">Designation</label>
                        <input class="input-field" name="designation" type="text"
                               placeholder="Your job title (optional)">
                    </div>

                    <div class="form-group">
                        <label class="input-label">City <span style="color:red;">*</span></label>
                        <input class="input-field" name="city" type="text" required
                               placeholder="Your city">
                    </div>

                    <button class="register-btn" type="submit">Register</button>

                    <div class="form-footer">
                        Already have an account?
                        <a class="login-link" href="<%=request.getContextPath()%>/login.jsp">Login here</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="footer">
            <span>©2025 Smart Task Tracker</span>
        </div>
    </div>
</body>
</html>
