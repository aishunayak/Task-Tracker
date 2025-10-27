<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smarttasktracker.model.Issue" %>
<%@ page import="com.smarttasktracker.model.Task" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
   // Prevent caching - MUST BE AT THE TOP
       response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
       response.setHeader("Pragma", "no-cache");
       response.setDateHeader("Expires", 0);

    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String userName = (String) session.getAttribute("userName");
    Integer currentUserId = (Integer) session.getAttribute("userId");
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
    String formattedDate = today.format(formatter);

    List<Issue> issues = (List<Issue>) request.getAttribute("issues");
    List<Task> userTasks = (List<Task>) request.getAttribute("userTasks");
    String currentStatus = (String) request.getAttribute("currentStatus");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Issues - SmartTaskTracker</title>

    <!-- CSS Files -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/Dashboard.css">
    <link rel="stylesheet" href="assets/css/Tasks.css">
    <link rel="stylesheet" href="assets/css/Logout.css">
</head>
<body>
    <div class="dashboard-wrapper">
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
                <a href="issues" class="nav-item active">
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
                <!-- Logout Button -->
                <a href="javascript:void(0)" onclick="confirmLogout()" class="logout-btn" title="Logout">
                    <i class="fas fa-sign-out-alt"></i>
                </a>
            </div>
        </aside>

        <div class="main-content">
            <header class="top-navbar">
                <div class="navbar-left">

                </div>
                <div class="navbar-right">
                    <div class="date-display">
                        <i class="fas fa-calendar-day"></i>
                        <span><%= formattedDate %></span>
                    </div>

                    <button class="create-btn" onclick="openIssueForm()">
                        <i class="fas fa-plus"></i>
                        Create
                    </button>
                </div>
            </header>

            <div class="tasks-content">
                <div class="breadcrumb">
                    <a href="dashboard"><i class="fas fa-home"></i> Home</a>
                    <span>/</span>
                    <span>Issues</span>
                </div>

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

                <div class="task-status-tabs">
                    <a href="issues" class="status-tab <%= currentStatus == null || "all".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-list"></i>
                        All
                    </a>
                    <a href="issues?status=open" class="status-tab <%= "open".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-exclamation-circle"></i>
                        Open
                    </a>
                    <a href="issues?status=close" class="status-tab <%= "close".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-check-circle"></i>
                        Close
                    </a>
                    <a href="issues?status=ignore" class="status-tab <%= "ignore".equalsIgnoreCase(currentStatus) ? "active" : "" %>">
                        <i class="fas fa-ban"></i>
                        Ignore
                    </a>
                </div>

                <div class="tasks-table-container">
                    <table class="tasks-table">
                        <thead>
                            <tr>
                                <th>Issue Name</th>
                                <th>Created By</th>
                                <th>Status</th>
                                <th>Priority</th>
                                <th>Created Date</th>
                                <th>Close</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (issues != null && !issues.isEmpty()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                                for (Issue issue : issues) {
                                    boolean isCreator = issue.getCreatedBy() == currentUserId;
                                    boolean isClosed = "CLOSE".equalsIgnoreCase(issue.getStatus());
                            %>
                            <tr class="<%= isClosed ? "closed-issue" : "" %>">
                                <td class="task-name-cell">
                                    <a href="javascript:void(0)" onclick="viewIssueDetails(<%= issue.getId() %>)" class="task-name-link">
                                        <i class="fas fa-exclamation-triangle issue-icon"></i>
                                        <%= issue.getIssueName() %>
                                    </a>
                                    <% if (issue.getTaskTitle() != null) { %>
                                    <small class="task-reference">Related: <%= issue.getTaskTitle() %></small>
                                    <% } %>
                                </td>
                                <td><%= issue.getCreatorName() != null ? issue.getCreatorName() : "N/A" %></td>
                                <td>
                                    <span class="status-badge status-<%= issue.getStatus().toLowerCase() %>">
                                        <%= issue.getStatus() %>
                                    </span>
                                </td>
                                <td>
                                    <span class="priority-badge priority-<%= issue.getPriority().toLowerCase() %>">
                                        <%= issue.getPriority() %>
                                    </span>
                                </td>
                                <td>
                                    <%= issue.getCreatedAt() != null ? dateFormat.format(issue.getCreatedAt()) : "N/A" %>
                                </td>
                                <td>
                                    <label class="completion-switch">
                                        <input type="checkbox"
                                               <%= isClosed ? "checked disabled" : "" %>
                                               onchange="toggleIssueClose(<%= issue.getId() %>, this.checked)"
                                               data-issue-id="<%= issue.getId() %>">
                                        <span class="slider <%= isClosed ? "disabled" : "" %>"></span>
                                    </label>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <% if (isCreator && !isClosed) { %>
                                        <button class="btn-action btn-delete" onclick="confirmDeleteIssue(<%= issue.getId() %>, '<%= issue.getIssueName() %>')" title="Delete">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                        <% } else if (isClosed) { %>
                                        <span class="closed-badge">
                                            <i class="fas fa-lock"></i> Closed
                                        </span>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                            <% } } else { %>
                            <tr>
                                <td colspan="7" class="no-tasks">
                                    <div class="empty-state">
                                        <i class="fas fa-inbox"></i>
                                        <p>No issues found</p>
                                        <small>Create a new issue to get started</small>
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

    <!-- Issue Details Modal -->
    <div class="modal-overlay" id="issueDetailsModal">
        <div class="modal-box large-modal">
            <button class="modal-close" onclick="closeIssueDetails()">&times;</button>
            <h2 id="modalIssueTitle">Issue Details</h2>
            <div class="task-details-content">
                <div class="detail-row">
                    <div class="detail-label">Issue Name:</div>
                    <div class="detail-value" id="issueNameDetail">N/A</div>
                </div>
                <div class="detail-row">
                    <div class="detail-group">
                        <div class="detail-label">Status:</div>
                        <div class="detail-value" id="issueStatusDetail">N/A</div>
                    </div>
                    <div class="detail-group">
                        <div class="detail-label">Priority:</div>
                        <div class="detail-value" id="issuePriorityDetail">N/A</div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">Description:</div>
                    <div class="detail-value" id="issueDescriptionDetail">No description</div>
                </div>
            </div>
            <div class="modal-actions">
                <button class="btn-secondary" onclick="closeIssueDetails()">Close</button>
            </div>
        </div>
    </div>

    <!-- Close Issue -->
    <div class="modal-overlay" id="closeIssueConfirmModal">
        <div class="modal-box small-modal">
            <button class="modal-close" onclick="cancelIssueClose()">&times;</button>
            <div class="completion-confirm-content">
                <div class="completion-icon">
                    <i class="fas fa-check-circle"></i>
                </div>
                <h2>Mark as Closed?</h2>
                <p>Is this issue closed?</p>
            </div>
            <div class="modal-actions">
                <button class="btn-cancel" onclick="cancelIssueClose()">No</button>
                <button class="btn-complete-confirm" onclick="confirmIssueClose()">Yes, Closed</button>
            </div>
        </div>
    </div>

    <!-- Delete Issue  -->
    <div class="modal-overlay" id="deleteIssueModal">
        <div class="modal-box small-modal">
            <button class="modal-close" onclick="closeDeleteIssueModal()">&times;</button>
            <div class="delete-confirm-content">
                <div class="delete-icon">
                    <i class="fas fa-trash-alt"></i>
                </div>
                <h2>Delete Issue?</h2>
                <p>Are you sure you want to delete <strong id="deleteIssueName"></strong>?</p>
                <p class="warning-text">This action cannot be undone.</p>
            </div>
            <div class="modal-actions">
                <button class="btn-cancel" onclick="closeDeleteIssueModal()">Cancel</button>
                <button class="btn-delete-confirm" onclick="deleteIssue()">Yes, Delete</button>
            </div>
        </div>
    </div>


    <%@ include file="includes/logout.jsp" %>
    <%@ include file="includes/createModals.jsp" %>


    <script src="assets/js/dashboard.js"></script>
    <script src="assets/js/createModals.js"></script>
    <script src="assets/js/Logout.js"></script>


    <script>
        // Declare variables at the top
        let currentIssueId = null;
        let deleteIssueIdTemp = null;

        // Issue Close Functions
        function toggleIssueClose(issueId, isChecked) {
            const checkbox = document.querySelector(`input[data-issue-id="${issueId}"]`);
            if (checkbox && checkbox.disabled) return false;

            if (isChecked) {
                currentIssueId = issueId;
                document.getElementById('closeIssueConfirmModal').style.display = 'flex';
            } else {
                checkbox.checked = true;
                alert('Closed issues cannot be reopened.');
            }
        }

        function cancelIssueClose() {
            if (currentIssueId) {
                const checkbox = document.querySelector(`input[data-issue-id="${currentIssueId}"]`);
                if (checkbox) checkbox.checked = false;
            }
            document.getElementById('closeIssueConfirmModal').style.display = 'none';
            currentIssueId = null;
        }

        function confirmIssueClose() {
            if (currentIssueId) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'closeIssue';
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'issueId';
                input.value = currentIssueId;
                form.appendChild(input);
                document.body.appendChild(form);
                form.submit();
            }
        }

        // Issue  Functions
        function viewIssueDetails(issueId) {
            fetch('getIssueDetails?issueId=' + issueId)
                .then(response => response.json())
                .then(data => {
                    document.getElementById('modalIssueTitle').textContent = data.issueName || 'Issue Details';
                    document.getElementById('issueNameDetail').textContent = data.issueName || 'N/A';
                    document.getElementById('issueStatusDetail').textContent = data.status || 'N/A';
                    document.getElementById('issuePriorityDetail').textContent = data.priority || 'N/A';
                    document.getElementById('issueDescriptionDetail').textContent = data.description || 'No description';
                    document.getElementById('issueDetailsModal').style.display = 'flex';
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Failed to load issue details');
                });
        }

        function closeIssueDetails() {
            document.getElementById('issueDetailsModal').style.display = 'none';
        }

        // Delete Issue Functions
        function confirmDeleteIssue(issueId, issueName) {
            deleteIssueIdTemp = issueId;
            document.getElementById('deleteIssueName').textContent = issueName;
            document.getElementById('deleteIssueModal').style.display = 'flex';
        }

        function deleteIssue() {
            if (deleteIssueIdTemp) {
                window.location.href = 'deleteIssue?issueId=' + deleteIssueIdTemp;
            }
        }

        function closeDeleteIssueModal() {
            document.getElementById('deleteIssueModal').style.display = 'none';
            deleteIssueIdTemp = null;
        }

        // Open Issue Form  createModals.jsp
        function openIssueForm() {
            const modal = document.getElementById('issueFormModal');
            if (modal) {
                modal.style.display = 'flex';
            } else {
                console.error('Issue form modal not found');
            }
        }

        // Close modals on outside click
        window.addEventListener('click', function(event) {
            if (event.target.classList.contains('modal-overlay')) {
                event.target.style.display = 'none';
            }
        });


    </script>
</body>
</html>
