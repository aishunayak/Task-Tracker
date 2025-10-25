<!-- Create Task/Issue Selection  -->
<div class="modal-overlay" id="createModal">
    <div class="modal-box small-modal">
        <button class="modal-close" onclick="closeCreateModal()">&times;</button>
        <h2>What do you want to create?</h2>
        <div class="create-options">
            <button class="option-btn" onclick="openTaskForm()">
                <i class="fas fa-tasks"></i>
                <span>Task</span>
            </button>
            <button class="option-btn" onclick="openIssueForm()">
                <i class="fas fa-exclamation-triangle"></i>
                <span>Issue</span>
            </button>
        </div>
    </div>
</div>

<!-- Create Task Form  -->
<div class="modal-overlay" id="taskFormModal">
    <div class="modal-box large-modal">
        <button class="modal-close" onclick="closeTaskForm()">&times;</button>
        <h2>Create Task</h2>

        <form id="createTaskForm" action="createTask" method="post" enctype="multipart/form-data">
            <div class="form-row">
                <div class="form-group full-width">
                    <label>Title <span class="required">*</span></label>
                    <input type="text" name="title" required placeholder="Enter task title">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group full-width">
                    <label>Description</label>
                    <textarea name="description" rows="4" placeholder="Enter task description"></textarea>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Start Date & Time</label>
                    <input type="datetime-local" name="startDate">
                </div>
                <div class="form-group">
                    <label>End Date & Time</label>
                    <input type="datetime-local" name="endDate">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Priority</label>
                    <select name="priority">
                        <option value="LOW">Low</option>
                        <option value="MEDIUM" selected>Medium</option>
                        <option value="HIGH">High</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Assignment Type <span class="required">*</span></label>
                    <select name="assignmentType" id="assignmentType" onchange="handleAssignmentChange()" required>
                        <option value="">Select Type</option>
                        <option value="SELF">Self</option>
                        <option value="INDIVIDUAL">Individual</option>
                        <option value="GROUP">Group</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group full-width">
                    <label>Attach File</label>
                    <input type="file" name="file" accept=".pdf,.doc,.docx,.jpg,.jpeg,.png">
                </div>
            </div>

            <input type="hidden" name="memberEmails" id="memberEmails">

            <div id="memberSection" style="display: none;">
                <div class="section-divider"></div>
                <h3 id="memberSectionTitle">Add Member</h3>
                <div id="membersList"></div>
                <div class="add-member-btn-container">
                    <button type="button" class="btn-add-member" onclick="addMemberRow()">
                        <i class="fas fa-plus"></i> Add Member
                    </button>
                </div>
            </div>

            <div class="form-actions">
                <button type="button" class="btn-cancel" onclick="closeTaskForm()">Cancel</button>
                <button type="submit" class="btn-submit">Create Task</button>
            </div>
        </form>
    </div>
</div>

<!-- Create Issue -->
<div class="modal-overlay" id="issueFormModal">
    <div class="modal-box large-modal">
        <button class="modal-close" onclick="closeIssueForm()">&times;</button>
        <h2>Create Issue</h2>

        <form id="createIssueForm" action="createIssue" method="post">
            <div class="form-row">
                <div class="form-group full-width">
                    <label>Selected Task</label>
                    <select name="taskId">
                        <option value="">Select Task (Optional)</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group full-width">
                    <label>Issue Name <span class="required">*</span></label>
                    <input type="text" name="issueName" required placeholder="Enter issue name">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Assigned To</label>
                    <select name="assignedTo">
                        <option value="">Select Assigned</option>
                        <option value="Self">Self</option>
                        <option value="Individual">Individual</option>
                        <option value="Group">Group</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Set Priority</label>
                    <select name="priority">
                        <option value="LOW">Low</option>
                        <option value="MEDIUM" selected>Medium</option>
                        <option value="HIGH">High</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group full-width">
                    <label>Description</label>
                    <textarea name="description" rows="4" placeholder="Describe the issue..."></textarea>
                </div>
            </div>

            <div class="form-actions">
                <button type="button" class="btn-cancel" onclick="closeIssueForm()">Cancel</button>
                <button type="submit" class="btn-submit">Create Issue</button>
            </div>
        </form>
    </div>
</div>
