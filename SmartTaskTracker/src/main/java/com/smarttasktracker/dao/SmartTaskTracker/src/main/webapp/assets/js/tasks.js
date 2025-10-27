let currentTaskId = null;
let currentDeleteTaskId = null;
let pendingCompletionTaskId = null;
let pendingCompletionCheckbox = null;

// View Task Details
function viewTaskDetails(taskId) {
    document.getElementById('modalTaskTitle').textContent = 'Loading...';
    document.getElementById('taskDetailsModal').style.display = 'flex';

    fetch('getTaskDetails?taskId=' + taskId)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('modalTaskTitle').textContent = data.task.title;
                document.getElementById('taskDescription').textContent = data.task.description || 'No description';
                document.getElementById('taskCreator').textContent = data.task.creatorName;
                document.getElementById('taskAssignmentType').innerHTML =
                    '<span class="assignment-badge assignment-' + data.task.assignmentType.toLowerCase() + '">' +
                    data.task.assignmentType + '</span>';
                document.getElementById('taskStatus').innerHTML =
                    '<span class="status-badge status-' + data.task.status.toLowerCase() + '">' +
                    data.task.status + '</span>';
                document.getElementById('taskPriority').innerHTML =
                    '<span class="priority-badge priority-' + data.task.priority.toLowerCase() + '">' +
                    data.task.priority + '</span>';
                document.getElementById('taskStartDate').textContent = data.task.startDate || 'N/A';
                document.getElementById('taskEndDate').textContent = data.task.endDate || 'N/A';

                if (data.task.filePath && data.task.filePath !== '') {
                    document.getElementById('fileAttachmentRow').style.display = 'flex';
                    document.getElementById('taskFileLink').href = data.task.filePath;
                    document.getElementById('taskFileName').textContent = data.task.fileName || 'Download File';
                } else {
                    document.getElementById('fileAttachmentRow').style.display = 'none';
                }
            } else {
                alert('Error loading task details: ' + data.message);
                closeTaskDetails();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to load task details');
            closeTaskDetails();
        });
}

function closeTaskDetails() {
    document.getElementById('taskDetailsModal').style.display = 'none';
}


function openEditModal(taskId) {
    currentTaskId = taskId;
    document.getElementById('editTaskModal').style.display = 'flex';

    // Fetch task details and populate form
    fetch('getTaskDetails?taskId=' + taskId)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('editTaskId').value = data.task.id;
                document.getElementById('editTitle').value = data.task.title;
                document.getElementById('editDescription').value = data.task.description || '';
                document.getElementById('editPriority').value = data.task.priority;
                document.getElementById('editStatus').value = data.task.status;

                if (data.task.startDate && data.task.startDate !== 'N/A') {
                    document.getElementById('editStartDate').value = formatDateTimeLocal(data.task.startDate);
                }

                if (data.task.endDate && data.task.endDate !== 'N/A') {
                    document.getElementById('editEndDate').value = formatDateTimeLocal(data.task.endDate);
                }
            } else {
                alert('Error loading task details: ' + data.message);
                closeEditModal();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to load task details');
            closeEditModal();
        });
}

function closeEditModal() {
    document.getElementById('editTaskModal').style.display = 'none';
    document.getElementById('editTaskForm').reset();
    currentTaskId = null;
}

function formatDateTimeLocal(dateString) {
    // dateString format: "22 Oct 2025 10:30"
    const months = {
        'Jan': '01', 'Feb': '02', 'Mar': '03', 'Apr': '04', 'May': '05', 'Jun': '06',
        'Jul': '07', 'Aug': '08', 'Sep': '09', 'Oct': '10', 'Nov': '11', 'Dec': '12'
    };

    const parts = dateString.split(' ');
    if (parts.length < 4) return '';

    const day = parts[0].padStart(2, '0');
    const month = months[parts[1]];
    const year = parts[2];
    const time = parts[3];

    return year + '-' + month + '-' + day + 'T' + time;
}

function confirmDelete(taskId, taskName) {
    currentDeleteTaskId = taskId;
    document.getElementById('deleteTaskName').textContent = taskName;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

function closeDeleteModal() {
    document.getElementById('deleteConfirmModal').style.display = 'none';
    currentDeleteTaskId = null;
}

// Delete Task
function deleteTask() {
    if (!currentDeleteTaskId) return;

    fetch('deleteTask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'taskId=' + currentDeleteTaskId
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            closeDeleteModal();
            // Reload page with success message
            window.location.href = 'tasks?success=' + encodeURIComponent(data.message);
        } else {
            alert('Error: ' + data.message);
            closeDeleteModal();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to delete task');
        closeDeleteModal();
    });
}

// Toggle Completion
function toggleCompletion(taskId, isChecked) {
    if (isChecked) {
        pendingCompletionTaskId = taskId;
        pendingCompletionCheckbox = event.target;
        document.getElementById('completionConfirmModal').style.display = 'flex';
    } else {

    }
}
function closeCompletionModal() {
    document.getElementById('completionConfirmModal').style.display = 'none';
    if (pendingCompletionCheckbox) {
        pendingCompletionCheckbox.checked = false;
    }
    pendingCompletionTaskId = null;
    pendingCompletionCheckbox = null;
}

function cancelCompletion() {
    if (pendingCompletionCheckbox) {
        pendingCompletionCheckbox.checked = false;
    }
    closeCompletionModal();
}

function confirmCompletion() {
    if (!pendingCompletionTaskId) return;

    fetch('completeTask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'taskId=' + pendingCompletionTaskId
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            closeCompletionModal();
            // Reload page with success message
            window.location.href = 'tasks?success=' + encodeURIComponent(data.message);
        } else {
            alert('Error: ' + data.message);
            if (pendingCompletionCheckbox) {
                pendingCompletionCheckbox.checked = false;
            }
            closeCompletionModal();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to mark task as completed');
        if (pendingCompletionCheckbox) {
            pendingCompletionCheckbox.checked = false;
        }
        closeCompletionModal();
    });
}

window.addEventListener('click', function(event) {
    const taskDetailsModal = document.getElementById('taskDetailsModal');
    const editTaskModal = document.getElementById('editTaskModal');
    const deleteConfirmModal = document.getElementById('deleteConfirmModal');
    const completionConfirmModal = document.getElementById('completionConfirmModal');

    if (event.target === taskDetailsModal) {
        closeTaskDetails();
    }

    if (event.target === editTaskModal) {
        closeEditModal();
    }

    if (event.target === deleteConfirmModal) {
        closeDeleteModal();
    }

    if (event.target === completionConfirmModal) {
        closeCompletionModal();
    }
});

// Auto-hide alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.style.display = 'none';
            }, 300);
        }, 5000);
    });
});


function openCreateTaskModal() {
    document.getElementById('createTaskModal').style.display = 'flex';
}

function closeCreateTaskModal() {
    document.getElementById('createTaskModal').style.display = 'none';
    document.getElementById('createTaskForm').reset();
}

window.addEventListener('click', function(event) {
    const createTaskModal = document.getElementById('createTaskModal');
    const taskDetailsModal = document.getElementById('taskDetailsModal');
    const editTaskModal = document.getElementById('editTaskModal');
    const deleteConfirmModal = document.getElementById('deleteConfirmModal');
    const completionConfirmModal = document.getElementById('completionConfirmModal');

    if (event.target === createTaskModal) {
        closeCreateTaskModal();
    }

    if (event.target === taskDetailsModal) {
        closeTaskDetails();
    }

    if (event.target === editTaskModal) {
        closeEditModal();
    }

    if (event.target === deleteConfirmModal) {
        closeDeleteModal();
    }

    if (event.target === completionConfirmModal) {
        closeCompletionModal();
    }
});

// Prevent unchecking completed tasks
function toggleCompletion(taskId, isChecked) {
    const checkbox = document.querySelector(`input[data-task-id="${taskId}"]`);

    // If checkbox is disabled (already completed), prevent any action
    if (checkbox && checkbox.disabled) {
        return false;
    }

    if (isChecked) {
        // Show confirmation modal for completion
        currentTaskId = taskId;
        document.getElementById('completionConfirmModal').style.display = 'flex';
    } else {
        // Prevent unchecking
        checkbox.checked = true;
        alert('Completed tasks cannot be marked as incomplete.');
    }
}

function confirmCompletion() {
    if (currentTaskId) {
        // Submit completion
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'completeTask';

        const taskIdInput = document.createElement('input');
        taskIdInput.type = 'hidden';
        taskIdInput.name = 'taskId';
        taskIdInput.value = currentTaskId;

        form.appendChild(taskIdInput);
        document.body.appendChild(form);
        form.submit();
    }
}

function cancelCompletion() {
    // Uncheck the checkbox
    if (currentTaskId) {
        const checkbox = document.querySelector(`input[data-task-id="${currentTaskId}"]`);
        if (checkbox) {
            checkbox.checked = false;
        }
    }
    closeCompletionModal();
}

function openEditModal(taskId) {
    const row = document.querySelector(`tr:has(input[data-task-id="${taskId}"])`);
    if (row && row.classList.contains('completed-task')) {
        alert('Completed tasks cannot be edited.');
        return;
    }
}

function confirmDelete(taskId, taskName) {
    const row = document.querySelector(`tr:has(input[data-task-id="${taskId}"])`);
    if (row && row.classList.contains('completed-task')) {
        alert('Completed tasks cannot be deleted.');
        return;
    }
}
function confirmLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'flex';
}

function cancelLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'none';
}

function proceedLogout() {
    window.location.href = 'logout';
}
////// today add
$(document).ready(function() {
    let users = [];

    // Fetch users from database
    function loadUsers() {
        $.ajax({
            url: 'getUsers',
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                users = data;
                populateUserDropdowns();
            },
            error: function() {
                console.error('Failed to load users');
            }
        });
    }

    // Populate both dropdowns with users
    function populateUserDropdowns() {
        let individualOptions = '<option value="">Select User</option>';
        let groupOptions = '<option value="">Select Users</option>';

        users.forEach(function(user) {
            let displayName = user.firstName && user.lastName
                ? user.firstName + ' ' + user.lastName + ' (' + user.username + ')'
                : user.username;
            let option = '<option value="' + user.userId + '">' + displayName + '</option>';
            individualOptions += option;
            groupOptions += option;
        });

        $('#individualUser').html(individualOptions);
        $('#groupUsers').html(groupOptions);
    }

    // Handle assignment type change
    $('#assignmentType').on('change', function() {
        let selectedType = $(this).val();

        // Hide all sections
        $('#individualSection, #groupSection').hide();

        // Show relevant section
        if (selectedType === 'INDIVIDUAL') {
            $('#individualSection').show();
        } else if (selectedType === 'GROUP') {
            $('#groupSection').show();
        }
    });

    // Validate group selection (max 5 users)
    $('#groupUsers').on('change', function() {
        let selected = $(this).val();
        if (selected && selected.length > 5) {
            alert('You can select maximum 5 users for group assignment');
            // Remove last selected option
            selected.pop();
            $(this).val(selected);
        }
    });

    // Load users on page load
    loadUsers();
});
