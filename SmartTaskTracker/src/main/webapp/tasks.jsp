<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smarttasktracker.model.Task" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Check if user is logged in
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String userName = (String) session.getAttribute("userName");
    Integer currentUserId = (Integer) session.getAttribute("userId");

    // Get current date
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
    String formattedDate = today.format(formatter);

    // Get tasks from request
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    String currentStatus = (String) request.getAttribute("currentStatus");
    if (currentStatus == null) currentStatus = "today";

    // Get success/error messages
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tasks - SmartTaskTracker</title>
    <link rel="stylesheet" href="assets/css/Dashboard.css">
    <link rel="stylesheet" href="assets/css/Tasks.css">
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
                <a href="tasks" class="nav-item active">
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



                    <button class="create-btn" onclick="openTaskForm()">
                        <i class="fas fa-plus"></i>
                        Create
                    </button>

                </div>
            </header>

            <!-- Tasks Content -->
            <div class="tasks-content">

                <!-- Breadcrumb -->
                <div class="breadcrumb">
                    <a href="dashboard"><i class="fas fa-home"></i> Home</a>
                    <span>/</span>
                    <span>Tasks</span>
                </div>

                <!-- Success/Error Messages -->
                <% if (success != null) { %>
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle"></i>
                        <%= success %>
                    </div>
                <% } %>

                <% if (error != null) { %>
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-circle"></i>
                        <%= error %>
                    </div>
                <% } %>

                <!-- Task Status Tabs -->
                <div class="task-status-tabs">
                    <a href="tasks" class="status-tab <%= "today".equals(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-calendar-day"></i>
                        Today's Tasks
                    </a>
                    <a href="tasks?status=ongoing" class="status-tab <%= "ongoing".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-play-circle"></i>
                        Ongoing
                    </a>
                    <a href="tasks?status=overdue" class="status-tab <%= "overdue".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-exclamation-triangle"></i>
                        Overdue
                    </a>
                    <a href="tasks?status=scheduled" class="status-tab <%= "scheduled".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-clock"></i>
                        Scheduled
                    </a>
                    <a href="tasks?status=completed" class="status-tab <%= "completed".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-check-circle"></i>
                        Completed
                    </a>
                    <a href="tasks?status=ongoing_with_issue" class="status-tab <%= "ongoing_with_issue".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-bug"></i>
                        With Issues
                    </a>
                </div>

                <!-- Tasks Table -->
                <div class="tasks-table-container">
                    <table class="tasks-table">
                        <thead>
                            <tr>
                                <th>Task Name</th>
                                <th>Assigned To</th>
                                <th>Priority</th>
                                <th>Status</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Completion</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            <% if (tasks != null && !tasks.isEmpty()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                                for (Task task : tasks) {
                                    boolean isCreator = task.getCreatedBy() == currentUserId;
                                    boolean isCompleted = "COMPLETED".equals(task.getStatus());
                            %>
                                <tr class="<%= isCompleted ? "completed-task" : "" %>">
                                    <td class="task-name-cell">
                                        <a href="javascript:void(0)" onclick="viewTaskDetails(<%= task.getId() %>)" class="task-name-link">
                                            <i class="fas fa-tasks task-icon"></i>
                                            <%= task.getTitle() %>
                                        </a>
                                    </td>
                                    <td><%= task.getCreatorName() %></td>
                                    <td>
                                        <span class="priority-badge priority-<%= task.getPriority().toLowerCase() %>">
                                            <%= task.getPriority() %>
                                        </span>
                                    </td>
                                    <td>
                                        <span class="status-badge status-<%= task.getStatus().toLowerCase() %>">
                                            <%= task.getStatus() %>
                                        </span>
                                    </td>
                                    <td>
                                        <%= task.getStartDate() != null ? dateFormat.format(task.getStartDate()) : "N/A" %>
                                    </td>
                                    <td>
                                        <%= task.getEndDate() != null ? dateFormat.format(task.getEndDate()) : "N/A" %>
                                    </td>
                                    <td>
                                        <label class="completion-switch">
                                            <input type="checkbox"
                                                   <%= isCompleted ? "checked disabled" : "" %>
                                                   onchange="toggleCompletion(<%= task.getId() %>, this.checked)"
                                                   data-task-id="<%= task.getId() %>">
                                            <span class="slider <%= isCompleted ? "disabled" : "" %>"></span>
                                        </label>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <button class="btn-action btn-view"
                                                    onclick="viewTaskDetails(<%= task.getId() %>)"
                                                    title="View Details">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                            <% if (isCreator && !isCompleted) { %>
                                            <button class="btn-action btn-edit"
                                                    onclick="openEditModal(<%= task.getId() %>)"
                                                    title="Edit Task">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn-action btn-delete"
                                                    onclick="confirmDelete(<%= task.getId() %>, '<%= task.getTitle() %>')"
                                                    title="Delete Task">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                            <% } else if (isCompleted) { %>
                                            <span class="completed-badge">
                                                <i class="fas fa-lock"></i> Completed
                                            </span>
                                            <% } %>
                                        </div>
                                    </td>
                                </tr>
                            <% }
                            } else { %>
                                <tr>
                                    <td colspan="8" class="no-tasks">
                                        <div class="empty-state">
                                            <i class="fas fa-clipboard-list"></i>
                                            <p>No tasks found</p>
                                            <small>Create a new task to get started</small>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>

                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

    <!-- Task Details Modal -->
    <div class="modal-overlay" id="taskDetailsModal">
        <div class="modal-box large-modal">
            <button class="modal-close" onclick="closeTaskDetails()">&times;</button>
            <h2 id="modalTaskTitle">Task Details</h2>

            <div class="task-details-content">
                <div class="detail-row">
                    <div class="detail-label">Description:</div>
                    <div class="detail-value" id="taskDescription">N/A</div>
                </div>

                <div class="detail-row">
                    <div class="detail-group">
                        <div class="detail-label">Created By:</div>
                        <div class="detail-value" id="taskCreator">N/A</div>
                    </div>
                    <div class="detail-group">
                        <div class="detail-label">Assignment Type:</div>
                        <div class="detail-value" id="taskAssignmentType">N/A</div>
                    </div>
                </div>

                <div class="detail-row">
                    <div class="detail-group">
                        <div class="detail-label">Status:</div>
                        <div class="detail-value" id="taskStatus">N/A</div>
                    </div>
                    <div class="detail-group">
                        <div class="detail-label">Priority:</div>
                        <div class="detail-value" id="taskPriority">N/A</div>
                    </div>
                </div>

                <div class="detail-row">
                    <div class="detail-group">
                        <div class="detail-label">Start Date:</div>
                        <div class="detail-value" id="taskStartDate">N/A</div>
                    </div>
                    <div class="detail-group">
                        <div class="detail-label">End Date:</div>
                        <div class="detail-value" id="taskEndDate">N/A</div>
                    </div>
                </div>

                <div class="detail-row" id="fileAttachmentRow" style="display: none;">
                    <div class="detail-label">Attached File:</div>
                    <div class="detail-value">
                        <a id="taskFileLink" href="#" target="_blank" class="file-link">
                            <i class="fas fa-paperclip"></i>
                            <span id="taskFileName">Download File</span>
                        </a>
                    </div>
                </div>
            </div>

            <div class="modal-actions">
                <button class="btn-secondary" onclick="closeTaskDetails()">Close</button>
            </div>
        </div>
    </div>

    <!-- Edit Task Modal -->
    <div class="modal-overlay" id="editTaskModal">
        <div class="modal-box large-modal">
            <button class="modal-close" onclick="closeEditModal()">&times;</button>
            <h2>Edit Task</h2>

            <form id="editTaskForm" action="updateTask" method="post" enctype="multipart/form-data">
                <input type="hidden" name="taskId" id="editTaskId">

                <div class="form-row">
                    <div class="form-group full-width">
                        <label>Task Title <span class="required">*</span></label>
                        <input type="text" name="title" id="editTitle" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group full-width">
                        <label>Description</label>
                        <textarea name="description" id="editDescription" rows="4"></textarea>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Start Date</label>
                        <input type="datetime-local" name="startDate" id="editStartDate">
                    </div>
                    <div class="form-group">
                        <label>End Date</label>
                        <input type="datetime-local" name="endDate" id="editEndDate">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Priority</label>
                        <select name="priority" id="editPriority">
                            <option value="LOW">Low</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="HIGH">High</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Status</label>
                        <select name="status" id="editStatus">
                            <option value="ONGOING">Ongoing</option>
                            <option value="COMPLETED">Completed</option>
                            <option value="OVERDUE">Overdue</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group full-width">
                        <label>Update File (Optional)</label>
                        <input type="file" name="file" accept=".pdf,.doc,.docx,.xls,.xlsx,.txt,.jpg,.png">
                    </div>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn-cancel" onclick="closeEditModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Update Task</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal-overlay" id="deleteConfirmModal">
        <div class="modal-box small-modal">
            <button class="modal-close" onclick="closeDeleteModal()">&times;</button>
            <div class="delete-confirm-content">
                <div class="delete-icon">
                    <i class="fas fa-trash-alt"></i>
                </div>
                <h2>Delete Task?</h2>
                <p>Are you sure you want to delete <strong id="deleteTaskName"></strong>?</p>
                <p class="warning-text">This action cannot be undone.</p>
            </div>
            <div class="modal-actions">
                <button class="btn-cancel" onclick="closeDeleteModal()">Cancel</button>
                <button class="btn-delete-confirm" onclick="deleteTask()">Yes, Delete</button>
            </div>
        </div>
    </div>

    <!-- Completion Confirmation Modal -->
    <div class="modal-overlay" id="completionConfirmModal">
        <div class="modal-box small-modal">
            <button class="modal-close" onclick="closeCompletionModal()">&times;</button>
            <div class="completion-confirm-content">
                <div class="completion-icon">
                    <i class="fas fa-check-circle"></i>
                </div>
                <h2>Mark as Completed?</h2>
                <p>Is this task completed?</p>
            </div>
            <div class="modal-actions">
                <button class="btn-cancel" onclick="cancelCompletion()">No</button>
                <button class="btn-complete-confirm" onclick="confirmCompletion()">Yes, Completed</button>
            </div>
        </div>
    </div>
    <%@ include file="includes/createModals.jsp" %>
        <%@ include file="includes/logout.jsp" %>

    <script src="assets/js/dashboard.js"></script>
    <script src="assets/js/tasks.js"></script>
    <script src="assets/js/logout.js"></script>

    <script src="assets/js/createModals.js"></script>
    <script>
        // Auto-open task form on page load if needed
        function directCreateTask() {
            openTaskForm();
        }
    </script>


</body>
</html>
