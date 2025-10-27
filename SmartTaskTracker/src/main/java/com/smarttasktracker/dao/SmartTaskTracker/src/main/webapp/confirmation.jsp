<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Check if user data exists in session
    String name = (String) session.getAttribute("reg_name");
    String username = (String) session.getAttribute("reg_username");
    String email = (String) session.getAttribute("reg_email");
    String profession = (String) session.getAttribute("reg_profession");
    String companyName = (String) session.getAttribute("reg_companyName");
    String designation = (String) session.getAttribute("reg_designation");
    String city = (String) session.getAttribute("reg_city");

    // If no data, redirect to registration
    if (name == null || email == null) {
        response.sendRedirect("registration.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Details - SmartTaskTracker</title>
    <link rel="stylesheet" type="text/css" href="assets/css/confirmation.css">
</head>
<body>
    <div class="container">
        <!-- Left Section  -->
        <div class="left">
            <img src="<%=request.getContextPath()%>/assets/images/meeting.gif"
                 alt="Animated GIF"
                 style="margin-top: 55px; width:200px;">
            <h1>Complete Profile</h1>
            <p>
                Please review your details before completing<br>
                your profile setup.
            </p>
        </div>

        <!-- Right Section -->
        <div class="right">
            <div class="confirmation-box">
                <h2>Confirm Your Details</h2>
                <p>Please verify all information is correct</p>

                <!-- Details Review -->
                <div class="details-container">
                    <!-- Personal Details -->
                    <div class="details-section">
                        <h3>Personal Information</h3>
                        <div class="detail-item">
                            <span class="label">Name:</span>
                            <span class="value"><%= name %></span>
                        </div>
                        <div class="detail-item">
                            <span class="label">Username:</span>
                            <span class="value"><%= username %></span>
                        </div>
                        <div class="detail-item">
                            <span class="label">Email:</span>
                            <span class="value"><%= email %></span>
                        </div>
                    </div>

                    <!-- Professional Details -->
                    <div class="details-section">
                        <h3>Professional Information</h3>
                        <div class="detail-item">
                            <span class="label">Profession:</span>
                            <span class="value"><%= profession %></span>
                        </div>
                        <div class="detail-item">
                            <span class="label">Company:</span>
                            <span class="value"><%= companyName %></span>
                        </div>
                        <% if (designation != null && !designation.isEmpty()) { %>
                        <div class="detail-item">
                            <span class="label">Designation:</span>
                            <span class="value"><%= designation %></span>
                        </div>
                        <% } %>
                        <div class="detail-item">
                            <span class="label">City:</span>
                            <span class="value"><%= city %></span>
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <form method="post" action="completeRegistration" style="display: inline;">
                        <button class="btn-edit" type="button" onclick="window.location.href='registration.jsp'">
                            Edit Details
                        </button>
                        <button class="btn-complete" type="submit">
                            Complete Profile
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <span>©2025 Smart Task Tracker</span>
        </div>
    </div>
</body>
</html>
