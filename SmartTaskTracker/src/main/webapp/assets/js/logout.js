

/**
 * Show logout confirmation modal
 */
function confirmLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'flex';
}

/**
 * Cancel logout and hide modal
 */
function cancelLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'none';
}

/**
 * Proceed with logout
 */
function proceedLogout() {
    window.location.href = 'logout';
}

// Close modal on outside click
window.addEventListener('click', function(event) {
    const modal = document.getElementById('logoutConfirmModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});
