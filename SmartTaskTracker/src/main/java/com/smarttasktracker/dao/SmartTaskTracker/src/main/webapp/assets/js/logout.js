

function confirmLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'flex';
}

function cancelLogout() {
    document.getElementById('logoutConfirmModal').style.display = 'none';
}


function proceedLogout() {
    window.location.href = 'logout';
}

window.addEventListener('click', function(event) {
    const modal = document.getElementById('logoutConfirmModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});
