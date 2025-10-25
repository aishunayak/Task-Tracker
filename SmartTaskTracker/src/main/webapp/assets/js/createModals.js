// ==========================================
// CREATE MODAL FUNCTIONS
// ==========================================

function openCreateModal() {
    console.log('Opening create modal');
    document.getElementById('createModal').style.display = 'flex';
}

function closeCreateModal() {
    console.log('Closing create modal');
    document.getElementById('createModal').style.display = 'none';
}

function openTaskForm() {
    console.log('Opening task form');
    closeCreateModal();
    document.getElementById('taskFormModal').style.display = 'flex';
}

function closeTaskForm() {
    console.log('Closing task form');
    document.getElementById('taskFormModal').style.display = 'none';
    const form = document.getElementById('createTaskForm');
    if (form) form.reset();
    document.getElementById('memberSection').style.display = 'none';
    document.getElementById('membersList').innerHTML = '';
}

function openIssueForm() {
    console.log('Opening issue form');
    closeCreateModal();
    document.getElementById('issueFormModal').style.display = 'flex';
}

function closeIssueForm() {
    console.log('Closing issue form');
    document.getElementById('issueFormModal').style.display = 'none';
    const form = document.getElementById('createIssueForm');
    if (form) form.reset();
}

// ==========================================
// TASK ASSIGNMENT HANDLING
// ==========================================

function handleAssignmentChange() {
    const assignmentType = document.getElementById('assignmentType').value;
    const memberSection = document.getElementById('memberSection');
    const memberSectionTitle = document.getElementById('memberSectionTitle');

    if (assignmentType === 'INDIVIDUAL') {
        memberSection.style.display = 'block';
        memberSectionTitle.textContent = 'Add Member';
        document.getElementById('membersList').innerHTML = '';
        addMemberRow();
    } else if (assignmentType === 'GROUP') {
        memberSection.style.display = 'block';
        memberSectionTitle.textContent = 'Add Group Members';
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

// Close modals when clicking outside
window.addEventListener('click', function(event) {
    if (event.target.classList.contains('modal-overlay')) {
        event.target.style.display = 'none';
    }
});

// Initialize
document.addEventListener('DOMContentLoaded', function() {
    const taskForm = document.getElementById('createTaskForm');
    if (taskForm) {
        taskForm.addEventListener('submit', function(e) {
            updateMemberEmails();
        });
    }
    console.log('Create modals initialized');
});
