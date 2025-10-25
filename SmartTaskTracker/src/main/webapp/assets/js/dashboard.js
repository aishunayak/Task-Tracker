function confirmLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'flex';
}

function cancelLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'none';
}

function proceedLogout() {
    window.location.href = 'logout';
}
