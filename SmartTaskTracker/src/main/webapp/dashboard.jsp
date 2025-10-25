<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Map" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String userName = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");

    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
    String formattedDate = today.format(formatter);

    // Get statistics from servlet
    Map<String, Integer> taskStats = (Map<String, Integer>) request.getAttribute("taskStats");
    Map<String, Integer> issueStats = (Map<String, Integer>) request.getAttribute("issueStats");

    // Task statistics with null checks
    int totalTasks = 0, ongoingTasks = 0, overdueTasks = 0, completedTasks = 0, scheduledTasks = 0;
    if (taskStats != null) {
        totalTasks = taskStats.getOrDefault("total", 0);
        ongoingTasks = taskStats.getOrDefault("ongoing", 0);
        overdueTasks = taskStats.getOrDefault("overdue", 0);
        completedTasks = taskStats.getOrDefault("completed", 0);
        scheduledTasks = taskStats.getOrDefault("scheduled", 0);
    }

    // Issue statistics with null checks
    int totalIssues = 0, openIssues = 0, closeIssues = 0, ignoreIssues = 0;
    if (issueStats != null) {
        totalIssues = issueStats.getOrDefault("total", 0);
        openIssues = issueStats.getOrDefault("open", 0);
        closeIssues = issueStats.getOrDefault("close", 0);
        ignoreIssues = issueStats.getOrDefault("ignore", 0);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - SmartTaskTracker</title>
    <link rel="stylesheet" href="assets/css/Dashboard.css">
        <link rel="stylesheet" href="assets/css/Logout.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="dashboard-wrapper">

        <!-- Left Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <img src="assets/images/logo.png" alt="Logo" class="logo">
                <h2>Task-Tracker</h2>
            </div>

            <nav class="sidebar-nav">
                <a href="dashboard" class="nav-item active">
                    <i class="fas fa-home"></i>
                    <span>Home</span>
                </a>
                <a href="tasks" class="nav-item">
                    <i class="fas fa-tasks"></i>
                    <span>Tasks</span>
                </a>
                <a href="issues" class="nav-item">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>Issues</span>
                </a>
                <a href="calendar" class="nav-item">
                    <i class="fas fa-calendar"></i>
                    <span>Calendar</span>
                </a>

            </nav>

            <div class="sidebar-footer">
                <div class="user-info">
                    <div class="user-avatar">
                        <%= userName != null ? userName.substring(0, 1).toUpperCase() : "U" %>
                    </div>
                    <div class="user-details">
                        <p class="user-name"><%= userName %></p>
                        <p class="user-role">Admin</p>
                    </div>
                </div>
                <a href="javascript:void(0)" onclick="confirmLogout()" class="logout-btn">
                    <i class="fas fa-sign-out-alt"></i>
                </a>

            </div>
        </aside>

        <!-- Main Content -->
        <div class="main-content">

            <!-- Top Navbar -->
            <header class="top-navbar">
                <div class="navbar-left">

                </div>

                <div class="navbar-right">
                    <div class="date-display">
                        <i class="fas fa-calendar-day"></i>
                        <span><%= formattedDate %></span>
                    </div>



                    <button class="create-btn" onclick="openCreateModal()">
                        <i class="fas fa-plus"></i>
                        Create
                    </button>
                </div>
            </header>

            <!-- Dashboard Content -->
            <div class="dashboard-content">

                <!-- Greeting Section -->
                <div class="greeting-section">
                    <h1>Good Afternoon! <%= userName %></h1>
                </div>

                <!-- Statistics Cards -->
                <div class="stats-cards">

                    <!-- Tasks Statistics Card -->
                    <div class="stat-card">
                        <div class="card-header">
                            <i class="fas fa-tasks card-icon"></i>
                            <h3><%= totalTasks %> Tasks</h3>
                        </div>
                        <div class="card-stats">
                            <div class="stat-item">
                                <span class="stat-label">Ongoing</span>
                                <span class="stat-value"><%= ongoingTasks %></span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">Overdue</span>
                                <span class="stat-value danger"><%= overdueTasks %></span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">Completed</span>
                                <span class="stat-value success"><%= completedTasks %></span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">Scheduled</span>
                                <span class="stat-value"><%= scheduledTasks %></span>
                            </div>
                        </div>
                    </div>

                    <!-- Issues Statistics Card -->
                    <div class="stat-card">
                        <div class="card-header">
                            <i class="fas fa-exclamation-triangle card-icon issue-icon-color"></i>
                            <h3><%= totalIssues %> Issues</h3>
                        </div>
                        <div class="card-stats">
                            <div class="stat-item">
                                <span class="stat-label">Open</span>
                                <span class="stat-value danger"><%= openIssues %></span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">Close</span>
                                <span class="stat-value success"><%= closeIssues %></span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">Ignore</span>
                                <span class="stat-value"><%= ignoreIssues %></span>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
        </div>
    </div>
<%@ include file="includes/createModals.jsp" %>

<%@ include file="includes/logout.jsp" %>



    <!-- Scripts -->
    <script src="assets/js/dashboard.js"></script>
    <script src="assets/js/createModals.js"></script>
        <script src="assets/js/Logout.js"></script>



</body>
</html>
