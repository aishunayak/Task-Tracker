<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.smarttasktracker.model.Task" %>
<%@ page import="com.smarttasktracker.model.Issue" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Timestamp" %>
<%
    // Check if user is logged in
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String userName = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");
    Integer userId = (Integer) session.getAttribute("userId");

    // Get attributes from servlet
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    List<Issue> issues = (List<Issue>) request.getAttribute("issues");
    String currentView = (String) request.getAttribute("currentView");
    if (currentView == null) currentView = "day";

    Calendar selectedDate = (Calendar) request.getAttribute("selectedDate");
    if (selectedDate == null) selectedDate = Calendar.getInstance();

    // Format dates for display
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd MMM, yyyy");
    SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM, yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd MMM, yyyy");

    String currentDisplayDate = dayFormat.format(selectedDate.getTime());
    String currentMonthYear = monthYearFormat.format(selectedDate.getTime());

    // Week range calculation
    Calendar weekStart = (Calendar) selectedDate.clone();
    weekStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    Calendar weekEnd = (Calendar) weekStart.clone();
    weekEnd.add(Calendar.DAY_OF_MONTH, 6);
    String weekRange = dayFormat.format(weekStart.getTime()) + " - " + dayFormat.format(weekEnd.getTime());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calendar - SmartTaskTracker</title>
    <link rel="stylesheet" href="assets/css/Dashboard.css">
    <link rel="stylesheet" href="assets/css/Calendar.css">
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
                <a href="dashboard" class="nav-item">
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
                <a href="calendar" class="nav-item active">
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
                        <span><%= dayFormat.format(Calendar.getInstance().getTime()) %></span>
                    </div>


                    <button class="create-btn" onclick="openCreateModal()">
                        <i class="fas fa-plus"></i>
                        Create
                    </button>
                </div>
            </header>

            <!-- Calendar Content -->
            <div class="calendar-content">

                <!-- Breadcrumb -->
                <div class="breadcrumb">
                    <a href="dashboard"><i class="fas fa-home"></i> Home</a>
                    <span>/</span>
                    <span>Calendar</span>

                </div>

                <!-- Calendar Controls Header -->
                <div class="calendar-controls-header">
                    <!-- Left: Month Navigation -->
                    <div class="month-navigation">
                        <button class="btn-nav" onclick="navigate('prev')">
                            <i class="fas fa-chevron-left"></i>
                        </button>
                        <span class="current-date" id="currentDateDisplay">
                            <%
                            if ("day".equals(currentView)) {
                                out.print(currentDisplayDate);
                            } else if ("week".equals(currentView)) {
                                out.print(weekRange);
                            } else {
                                out.print(currentMonthYear);
                            }
                            %>
                        </span>
                        <button class="btn-nav" onclick="navigate('next')">
                            <i class="fas fa-chevron-right"></i>
                        </button>
                    </div>

                    <!-- Right: View Toggle Buttons -->
                    <div class="view-controls">
                        <button class="btn-today" onclick="goToToday()">Today</button>
                        <div class="view-toggle">
                            <button class="btn-view <%= "day".equals(currentView) ? "active" : "" %>" onclick="changeView('day')">
                                Day
                            </button>
                            <button class="btn-view <%= "week".equals(currentView) ? "active" : "" %>" onclick="changeView('week')">
                                Week
                            </button>
                            <button class="btn-view <%= "month".equals(currentView) ? "active" : "" %>" onclick="changeView('month')">
                                Month
                            </button>
                        </div>

                    </div>
                </div>

                <!-- Task/Issues Tabs -->
                <div class="content-tabs">
                    <button class="tab-btn active" onclick="showTabContent('tasks','issues')">Tasks & Issues</button>
                </div>

                <!-- Calendar Views -->
                <div class="calendar-view-container">

                    <!-- DAY VIEW -->
                    <div id="dayView" class="view-content <%= "day".equals(currentView) ? "active" : "" %>">
                        <div class="day-view-container">
                            <%
                            String selectedDateStr = dateFormat.format(selectedDate.getTime());
                            boolean hasTasksToday = false;

                            if (tasks != null && !tasks.isEmpty()) {
                                for (Task task : tasks) {
                                    if (task.getStartDate() != null) {
                                        String taskDateStr = dateFormat.format(task.getStartDate());
                                        if (selectedDateStr.equals(taskDateStr)) {
                                            hasTasksToday = true;
                            %>
                            <div class="task-card" onclick="viewTaskDetails(<%= task.getId() %>)">
                                <div class="task-card-header">
                                    <span class="task-id">ID #<%= task.getId() %></span>
                                    <span class="task-comments"><i class="far fa-comment"></i> 0</span>
                                </div>
                                <h3 class="task-title"><%= task.getTitle() %></h3>
                                <p class="task-description"><%= task.getDescription() != null ? task.getDescription() : "No description" %></p>
                                <div class="task-meta">
                                    <span class="task-date">
                                        <i class="far fa-calendar"></i>
                                        <%= dayFormat.format(task.getStartDate()) %>
                                    </span>
                                    <span class="task-time">
                                        <i class="far fa-clock"></i>
                                        <%= timeFormat.format(task.getStartDate()) %>
                                        <% if (task.getEndDate() != null) { %>
                                         - <%= timeFormat.format(task.getEndDate()) %>
                                        <% } %>
                                    </span>
                                </div>
                                <div class="task-footer">
                                    <span class="status-badge status-<%= task.getStatus().toLowerCase() %>">
                                        <%= task.getStatus() %>
                                    </span>
                                    <span class="priority-badge priority-<%= task.getPriority().toLowerCase() %>">
                                        <%= task.getPriority() %>
                                    </span>
                                    <div class="task-assignee">
                                        <i class="fas fa-user"></i>
                                        <%= task.getCreatorName() != null ? task.getCreatorName() : "Admin" %>
                                    </div>
                                </div>
                            </div>
                            <%
                                        }
                                    }
                                }
                            }

                            if (!hasTasksToday) {
                            %>
                            <div class="empty-state">
                                <i class="fas fa-calendar-day"></i>
                                <p>No tasks scheduled for this day</p>
                            </div>
                            <% } %>
                        </div>
                    </div>

                     <!-- WEEK VIEW - Grid Layout with Day Headers -->
                     <div id="weekView" class="view-content <%= "week".equals(currentView) ? "active" : "" %>">
                         <div class="week-view-grid">
                             <!-- Header Row with Day Names and Dates -->
                             <div class="week-time-header">Time</div>
                             <%
                             Calendar weekCal = (Calendar) selectedDate.clone();
                             weekCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                             SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEE");
                             SimpleDateFormat weekDateFormat = new SimpleDateFormat("dd");
                             Calendar today = Calendar.getInstance();

                             for (int i = 0; i < 7; i++) {
                                 String dayName = weekDayFormat.format(weekCal.getTime());
                                 String dayDate = weekDateFormat.format(weekCal.getTime());
                                 boolean isToday = weekCal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) &&
                                                  weekCal.get(Calendar.YEAR) == today.get(Calendar.YEAR);
                             %>
                             <div class="week-day-header <%= isToday ? "today-header" : "" %>">
                                 <div class="day-name"><%= dayName %></div>
                                 <div class="day-date"><%= dayDate %></div>
                             </div>
                             <%
                                 weekCal.add(Calendar.DAY_OF_MONTH, 1);
                             }
                             %>

                             <!-- Time Rows -->
                             <%
                             String[] timeSlots = {"12 AM - 4 AM", "4 AM - 8 AM", "8 AM - 12 PM", "12 PM - 4 PM", "4 PM - 8 PM", "8 PM - 12 AM"};
                             int[] startHours = {0, 4, 8, 12, 16, 20};
                             int[] endHours = {4, 8, 12, 16, 20, 24};
                             SimpleDateFormat taskDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                             for (int slot = 0; slot < 6; slot++) {
                             %>
                             <!-- Time Label -->
                             <div class="week-time-cell"><%= timeSlots[slot] %></div>

                             <%
                             weekCal = (Calendar) selectedDate.clone();
                             weekCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

                             for (int day = 0; day < 7; day++) {
                                 String currentDayStr = taskDateFormat.format(weekCal.getTime());
                             %>
                             <div class="week-day-cell">
                                 <%
                                 // Get tasks for this day and time slot
                                 if (tasks != null) {
                                     for (Task task : tasks) {
                                         if (task.getStartDate() != null) {
                                             String taskDateStr = taskDateFormat.format(task.getStartDate());
                                             Calendar taskCal = Calendar.getInstance();
                                             taskCal.setTime(task.getStartDate());
                                             int taskHour = taskCal.get(Calendar.HOUR_OF_DAY);

                                             if (currentDayStr.equals(taskDateStr) && taskHour >= startHours[slot] && taskHour < endHours[slot]) {
                                 %>
                                 <a href="tasks" class="week-item week-task">
                                     <i class="fas fa-tasks"></i> <%= task.getTitle() %>
                                 </a>
                                 <%
                                             }
                                         }
                                     }
                                 }

                                 // Get issues for this day
                                 if (issues != null) {
                                     for (Issue issue : issues) {
                                         if (issue.getCreatedAt() != null) {
                                             String issueDateStr = taskDateFormat.format(issue.getCreatedAt());
                                             Calendar issueCal = Calendar.getInstance();
                                             issueCal.setTime(issue.getCreatedAt());
                                             int issueHour = issueCal.get(Calendar.HOUR_OF_DAY);

                                             if (currentDayStr.equals(issueDateStr) && issueHour >= startHours[slot] && issueHour < endHours[slot]) {
                                 %>
                                 <a href="issues" class="week-item week-issue">
                                     <i class="fas fa-exclamation-circle"></i> <%= issue.getIssueName() %>
                                 </a>
                                 <%
                                             }
                                         }
                                     }
                                 }
                                 %>
                             </div>
                             <%
                                 weekCal.add(Calendar.DAY_OF_MONTH, 1);
                             }
                             } %>
                         </div>
                     </div>

                    <!-- MONTH VIEW -->
                    <div id="monthView" class="view-content <%= "month".equals(currentView) ? "active" : "" %>">
                        <div class="month-view-container">
                            <!-- Month Day Headers -->
                            <div class="month-day-headers">
                                <div class="month-day-header">Sun</div>
                                <div class="month-day-header">Mon</div>
                                <div class="month-day-header">Tue</div>
                                <div class="month-day-header">Wed</div>
                                <div class="month-day-header">Thu</div>
                                <div class="month-day-header">Fri</div>
                                <div class="month-day-header">Sat</div>
                            </div>

                            <!-- Month Calendar Grid -->
                            <div class="month-grid">
                                <%
                                Calendar monthCal = (Calendar) selectedDate.clone();
                                monthCal.set(Calendar.DAY_OF_MONTH, 1);
                                int firstDayOfWeek = monthCal.get(Calendar.DAY_OF_WEEK);
                                int daysInMonth = monthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                                Calendar todayMonth = Calendar.getInstance();

                                // Print empty cells for days before month starts
                                for (int i = 1; i < firstDayOfWeek; i++) {
                                    Calendar prevMonth = (Calendar) monthCal.clone();
                                    prevMonth.add(Calendar.MONTH, -1);
                                    int prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
                                    int prevMonthDay = prevMonthDays - (firstDayOfWeek - i) + 1;
                                %>
                                <div class="month-day other-month">
                                    <div class="day-number"><%= prevMonthDay %></div>
                                </div>
                                <% } %>

                                <!-- Print days of current month -->
                                <%
                                SimpleDateFormat monthDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                for (int day = 1; day <= daysInMonth; day++) {
                                    monthCal.set(Calendar.DAY_OF_MONTH, day);
                                    String dayStr = monthDateFormat.format(monthCal.getTime());
                                    boolean isToday = day == todayMonth.get(Calendar.DAY_OF_MONTH) &&
                                                     monthCal.get(Calendar.MONTH) == todayMonth.get(Calendar.MONTH) &&
                                                     monthCal.get(Calendar.YEAR) == todayMonth.get(Calendar.YEAR);

                                    // Get tasks for this day
                                    List<Task> dayTasks = new ArrayList<>();
                                    if (tasks != null) {
                                        for (Task task : tasks) {
                                            if (task.getStartDate() != null) {
                                                String taskDateStr = monthDateFormat.format(task.getStartDate());
                                                if (dayStr.equals(taskDateStr)) {
                                                    dayTasks.add(task);
                                                }
                                            }
                                        }
                                    }

                                    // Get issues for this day
                                    List<Issue> dayIssues = new ArrayList<>();
                                    if (issues != null) {
                                        for (Issue issue : issues) {
                                            if (issue.getCreatedAt() != null) {
                                                String issueDateStr = monthDateFormat.format(issue.getCreatedAt());
                                                if (dayStr.equals(issueDateStr)) {
                                                    dayIssues.add(issue);
                                                }
                                            }
                                        }
                                    }

                                    int totalItems = dayTasks.size() + dayIssues.size();
                                %>
                                <div class="month-day <%= isToday ? "today" : "" %> <%= (totalItems > 0) ? "has-items" : "" %>">
                                    <div class="day-number"><%= day %></div>

                                    <!-- Display Tasks (Blue) -->
                                    <%
                                    int displayTaskCount = Math.min(dayTasks.size(), 2);
                                    for (int t = 0; t < displayTaskCount; t++) {
                                        Task dayTask = dayTasks.get(t);
                                    %>
                                    <a href="tasks" class="month-item month-task">
                                        <i class="fas fa-tasks"></i>
                                        <%= dayTask.getTitle().length() > 12 ? dayTask.getTitle().substring(0, 12) + "..." : dayTask.getTitle() %>
                                    </a>
                                    <% } %>

                                    <!-- Display Issues (Red) -->
                                    <%
                                    int remainingSpace = 2 - displayTaskCount;
                                    int displayIssueCount = Math.min(dayIssues.size(), remainingSpace);
                                    for (int i = 0; i < displayIssueCount; i++) {
                                        Issue dayIssue = dayIssues.get(i);
                                    %>
                                    <a href="issues" class="month-item month-issue">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <%= dayIssue.getIssueName().length() > 12 ? dayIssue.getIssueName().substring(0, 12) + "..." : dayIssue.getIssueName() %>
                                    </a>
                                    <% } %>

                                    <!-- Show "more" if items exceed display limit -->
                                    <% if (totalItems > 2) { %>
                                    <div class="more-items">+<%= totalItems - 2 %> more</div>
                                    <% } %>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>


        <%@ include file="includes/createModals.jsp" %>
                <%@ include file="includes/logout.jsp" %>

        <script src="assets/js/calendar.js"></script>
        <script src="assets/js/createModals.js"></script>
                <script src="assets/js/logout.js"></script>



</body>
</html>
