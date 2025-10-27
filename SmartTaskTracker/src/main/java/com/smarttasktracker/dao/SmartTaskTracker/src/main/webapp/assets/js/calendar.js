
// Get current state from JSP
let currentView = 'day';
let currentYear = new Date().getFullYear();
let currentMonth = new Date().getMonth() + 1;
let currentDay = new Date().getDate();

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    initializeCalendar();
});

function initializeCalendar() {
    // Get view from active button
    const activeViewBtn = document.querySelector('.btn-view.active');
    if (activeViewBtn) {
        currentView = activeViewBtn.textContent.trim().toLowerCase();
    }

    // Get date from display
    const dateDisplay = document.getElementById('currentDateDisplay');
    if (dateDisplay) {
        extractDateFromDisplay(dateDisplay.textContent);
    }

    console.log('Calendar initialized:', {
        view: currentView,
        date: `${currentYear}-${currentMonth}-${currentDay}`
    });
}

function extractDateFromDisplay(dateText) {
    const yearMatch = dateText.match(/\d{4}/);
    if (yearMatch) {
        currentYear = parseInt(yearMatch[0]);
    }

    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                   'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    months.forEach((month, index) => {
        if (dateText.includes(month)) {
            currentMonth = index + 1;
        }
    });

    const dayMatch = dateText.match(/^(\d{1,2})\s/);
    if (dayMatch) {
        currentDay = parseInt(dayMatch[1]);
    }
}

//navigation functn
function navigate(direction) {
    let year = currentYear;
    let month = currentMonth;
    let day = currentDay;

    if (direction === 'prev') {
        if (currentView === 'day') {
            day--;
            if (day < 1) {
                month--;
                if (month < 1) {
                    month = 12;
                    year--;
                }
                day = getDaysInMonth(year, month);
            }
        } else if (currentView === 'week') {
            day -= 7;
            while (day < 1) {
                month--;
                if (month < 1) {
                    month = 12;
                    year--;
                }
                day += getDaysInMonth(year, month);
            }
        } else if (currentView === 'month') {
            month--;
            if (month < 1) {
                month = 12;
                year--;
            }
        }
    } else if (direction === 'next') {
        if (currentView === 'day') {
            day++;
            let daysInMonth = getDaysInMonth(year, month);
            if (day > daysInMonth) {
                day = 1;
                month++;
                if (month > 12) {
                    month = 1;
                    year++;
                }
            }
        } else if (currentView === 'week') {
            day += 7;
            let daysInMonth = getDaysInMonth(year, month);
            while (day > daysInMonth) {
                day -= daysInMonth;
                month++;
                if (month > 12) {
                    month = 1;
                    year++;
                }
                daysInMonth = getDaysInMonth(year, month);
            }
        } else if (currentView === 'month') {
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }
    }

    window.location.href = `calendar?view=${currentView}&year=${year}&month=${month}&day=${day}`;
}

function getDaysInMonth(year, month) {
    return new Date(year, month, 0).getDate();
}

function goToToday() {
    window.location.href = `calendar?view=${currentView}`;
}

function changeView(view) {
    window.location.href = `calendar?view=${view}`;
}

//task detail modal
function viewTaskDetails(taskId) {
    fetch(`getTaskDetails?taskId=${taskId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('taskDetailTitle').textContent = data.task.title;
                document.getElementById('taskDetailDescription').textContent =
                    data.task.description || 'No description provided';
                document.getElementById('taskDetailCreator').textContent = data.task.creatorName;

                document.getElementById('taskDetailStatus').innerHTML =
                    `<span class="status-badge status-${data.task.status.toLowerCase()}">${data.task.status}</span>`;

                document.getElementById('taskDetailPriority').innerHTML =
                    `<span class="priority-badge priority-${data.task.priority.toLowerCase()}">${data.task.priority}</span>`;

                document.getElementById('taskDetailStartDate').textContent = data.task.startDate;
                document.getElementById('taskDetailStartTime').textContent = data.task.startTime;
                document.getElementById('taskDetailEndDate').textContent = data.task.endDate;
                document.getElementById('taskDetailEndTime').textContent = data.task.endTime;

                if (data.task.filePath && data.task.filePath !== 'null') {
                    document.getElementById('taskFileRow').style.display = 'flex';
                    document.getElementById('taskDetailFile').href = data.task.filePath;
                } else {
                    document.getElementById('taskFileRow').style.display = 'none';
                }

                document.getElementById('taskDetailModal').style.display = 'flex';
            } else {
                alert('Failed to load task details');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error loading task details');
        });
}

function closeTaskDetailModal() {
    document.getElementById('taskDetailModal').style.display = 'none';
}

//create btn
function openCreateModal() {
    document.getElementById('createModal').style.display = 'flex';
}

function closeCreateModal() {
    document.getElementById('createModal').style.display = 'none';
}

// tab switching
function showTabContent(type) {
    const tabs = document.querySelectorAll('.tab-btn');
    tabs.forEach(tab => {
        if (tab.textContent.toLowerCase().includes(type)) {
            tab.classList.add('active');
        } else {
            tab.classList.remove('active');
        }
    });
    console.log('Showing:', type);
}


// Close modals on outside click
window.addEventListener('click', function(event) {
    if (event.target.classList.contains('modal-overlay')) {
        event.target.style.display = 'none';
    }
});

// Close modals with Escape key
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape' || event.key === 'Esc') {
        closeTaskDetailModal();
        closeCreateModal();
    }
});

console.log('%c Calendar JS Loaded ', 'background: #2d7a8e; color: white; padding: 5px 10px; border-radius: 4px;');
// ==========================================
// CREATE MODAL FUNCTIONS
// ==========================================

function openCreateModal() {
    document.getElementById('createModal').style.display = 'flex';
}

function closeCreateModal() {
    document.getElementById('createModal').style.display = 'none';
}

function openTaskForm() {
    closeCreateModal();
    document.getElementById('taskFormModal').style.display = 'flex';
}

function closeTaskForm() {
    document.getElementById('taskFormModal').style.display = 'none';
    document.getElementById('createTaskForm').reset();
    // Reset member section
    document.getElementById('memberSection').style.display = 'none';
    document.getElementById('membersList').innerHTML = '';
}

function openIssueForm() {
    closeCreateModal();
    document.getElementById('issueFormModal').style.display = 'flex';
}

function closeIssueForm() {
    document.getElementById('issueFormModal').style.display = 'none';
    document.getElementById('createIssueForm').reset();
}

//task asignment handle
function handleAssignmentChange() {
    const assignmentType = document.getElementById('assignmentType').value;
    const memberSection = document.getElementById('memberSection');
    const memberSectionTitle = document.getElementById('memberSectionTitle');

    if (assignmentType === 'INDIVIDUAL') {
        memberSection.style.display = 'block';
        memberSectionTitle.textContent = 'Add Member';
        // Clear and add one row
        document.getElementById('membersList').innerHTML = '';
        addMemberRow();
    } else if (assignmentType === 'GROUP') {
        memberSection.style.display = 'block';
        memberSectionTitle.textContent = 'Add Group Members';
        // Clear and add one row
        document.getElementById('membersList').innerHTML = '';
        addMemberRow();
    } else {
        memberSection.style.display = 'none';
        document.getElementById('membersList').innerHTML = '';
    }
}

let memberCount = 0;

function addMemberRow() {
    memberCount++;
    const membersList = document.getElementById('membersList');

    const memberRow = document.createElement('div');
    memberRow.className = 'member-row';
    memberRow.id = `member-row-${memberCount}`;

    memberRow.innerHTML = `
        <input type="email"
               class="member-email-input"
               id="member-${memberCount}"
               placeholder="Enter member email"
               onchange="updateMemberEmails()">
        <button type="button" class="btn-remove-member" onclick="removeMemberRow(${memberCount})">
            <i class="fas fa-times"></i>
        </button>
    `;

    membersList.appendChild(memberRow);
}

function removeMemberRow(id) {
    const row = document.getElementById(`member-row-${id}`);
    if (row) {
        row.remove();
        updateMemberEmails();
    }
}

function updateMemberEmails() {
    const emails = [];
    const inputs = document.querySelectorAll('.member-email-input');

    inputs.forEach(input => {
        if (input.value.trim() !== '') {
            emails.push(input.value.trim());
        }
    });

    document.getElementById('memberEmails').value = emails.join(',');
}

// Form submission handling
document.getElementById('createTaskForm').addEventListener('submit', function(e) {
    updateMemberEmails(); // Make sure member emails are updated before submit
});
function confirmLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'flex';
}

function cancelLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'none';
}

function proceedLogout() {
    window.location.href = 'logout';
}
