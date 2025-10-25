/**
 * ISSUES.JS - Issue Management JavaScript
 * Handles close toggle, view details, delete, and modals
 */

// Global variables to track current operations
let currentIssueId = null;
let deleteIssueIdTemp = null;

/**
 * Toggle Issue Close Button
 * Shows confirmation popup when user tries to close an issue
 */
function toggleIssueClose(issueId, isChecked) {
    const checkbox = document.querySelector(`input[data-issue-id="${issueId}"]`);

    // If checkbox is disabled (already closed), ignore the click
    if (checkbox && checkbox.disabled) {
        return false;
    }

    if (isChecked) {
        // User clicked to close - show confirmation popup
        currentIssueId = issueId;
        const modal = document.getElementById('closeIssueConfirmModal');
        if (modal) {
            modal.style.display = 'flex';
        } else {
            console.error('Close confirmation modal not found!');
        }
    } else {
        // User tried to uncheck - prevent it (irreversible)
        checkbox.checked = true;
        alert('Closed issues cannot be reopened.');
    }
}

/**
 * Confirm Issue Close
 * Submits form to close the issue permanently
 */
function confirmIssueClose() {
    if (currentIssueId) {
        // Create a hidden form to submit the close request
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

/**
 * Cancel Issue Close
 * Unchecks the toggle and hides the confirmation popup
 */
function cancelIssueClose() {
    if (currentIssueId) {
        const checkbox = document.querySelector(`input[data-issue-id="${currentIssueId}"]`);
        if (checkbox) {
            checkbox.checked = false;
        }
    }

    const modal = document.getElementById('closeIssueConfirmModal');
    if (modal) {
        modal.style.display = 'none';
    }

    currentIssueId = null;
}

/**
 * View Issue Details
 * Fetches and displays issue information in a modal
 */
function viewIssueDetails(issueId) {
    fetch('getIssueDetails?issueId=' + issueId)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch issue details');
            }
            return response.json();
        })
        .then(data => {
            // Update modal content with issue data
            document.getElementById('modalIssueTitle').textContent = data.issueName || 'Issue Details';
            document.getElementById('issueNameDetail').textContent = data.issueName || 'N/A';
            document.getElementById('issueStatusDetail').textContent = data.status || 'N/A';
            document.getElementById('issuePriorityDetail').textContent = data.priority || 'N/A';
            document.getElementById('issueDescriptionDetail').textContent = data.description || 'No description';

            // Show the details modal
            const modal = document.getElementById('issueDetailsModal');
            if (modal) {
                modal.style.display = 'flex';
            }
        })
        .catch(error => {
            console.error('Error fetching issue details:', error);
            alert('Failed to load issue details. Please try again.');
        });
}

/**
 * Close Issue Details Modal
 */
function closeIssueDetails() {
    const modal = document.getElementById('issueDetailsModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

/**
 * Edit Issue
 * Redirects to issue edit page
 */
function editIssue(issueId) {
    window.location.href = 'editIssue?issueId=' + issueId;
}

/**
 * Confirm Delete Issue
 * Shows delete confirmation popup
 */
function confirmDeleteIssue(issueId, issueName) {
    deleteIssueIdTemp = issueId;

    const nameElement = document.getElementById('deleteIssueName');
    if (nameElement) {
        nameElement.textContent = issueName;
    }

    const modal = document.getElementById('deleteIssueModal');
    if (modal) {
        modal.style.display = 'flex';
    }
}

/**
 * Delete Issue
 * Executes the delete operation
 */
function deleteIssue() {
    if (deleteIssueIdTemp) {
        window.location.href = 'deleteIssue?issueId=' + deleteIssueIdTemp;
    }
}

/**
 * Close Delete Confirmation Modal
 */
function closeDeleteIssueModal() {
    const modal = document.getElementById('deleteIssueModal');
    if (modal) {
        modal.style.display = 'none';
    }
    deleteIssueIdTemp = null;
}

/**
 * Close modals when clicking outside (on overlay)
 */
window.addEventListener('click', function(event) {
    if (event.target.classList.contains('modal-overlay')) {
        event.target.style.display = 'none';

        // Reset state based on which modal was closed
        if (event.target.id === 'closeIssueConfirmModal') {
            cancelIssueClose();
        }

        if (event.target.id === 'deleteIssueModal') {
            closeDeleteIssueModal();
        }
    }
});

/**
 * Initialize on page load
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Issues page JavaScript loaded successfully');
});
/**
 * View Issue Details
 * Fetches and displays issue information in a modal
 */
function viewIssueDetails(issueId) {
    fetch('getIssueDetails?issueId=' + issueId)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch issue details');
            }
            return response.json();
        })
        .then(data => {
            // Update modal content with issue data
            document.getElementById('modalIssueTitle').textContent = data.issueName || 'Issue Details';
            document.getElementById('issueNameDetail').textContent = data.issueName || 'N/A';
            document.getElementById('issueStatusDetail').textContent = data.status || 'N/A';
            document.getElementById('issuePriorityDetail').textContent = data.priority || 'N/A';
            document.getElementById('issueDescriptionDetail').textContent = data.description || 'No description';

            // Show the details modal
            const modal = document.getElementById('issueDetailsModal');
            if (modal) {
                modal.style.display = 'flex';
            }
        })
        .catch(error => {
            console.error('Error fetching issue details:', error);
            alert('Failed to load issue details. Please try again.');
        });
}

/**
 * Close Issue Details Modal
 */
function closeIssueDetails() {
    const modal = document.getElementById('issueDetailsModal');
    if (modal) {
        modal.style.display = 'none';
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
