<%-- includes/logout.jsp --%>
<!-- Logout Confirmation Modal - Red Version -->
<div class="modal-overlay" id="logoutConfirmModal">
    <div class="modal-box small-modal">
        <button class="modal-close" onclick="cancelLogout()">&times;</button>
        <div class="completion-confirm-content">
            <!-- RED ICON -->
            <div class="completion-icon logout-icon-red">
                <i class="fas fa-sign-out-alt"></i>
            </div>
            <h2>Logout?</h2>
            <p>Are you sure you want to logout?</p>
        </div>
        <div class="modal-actions">
            <button class="btn-cancel" onclick="cancelLogout()">No</button>
            <!-- RED BUTTON -->
            <button class="btn-logout-confirm" onclick="proceedLogout()">Yes, Logout</button>
        </div>
    </div>
</div>
