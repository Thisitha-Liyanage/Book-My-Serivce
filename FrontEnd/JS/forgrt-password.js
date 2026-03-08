function openForgot() {
    document.getElementById('forgot-modal').classList.add('active');
}

function closeForgot() {
    document.getElementById('forgot-modal').classList.remove('active');
}

/* Close when clicking outside */
window.addEventListener('click', function (e) {
    const modal = document.getElementById('forgot-modal');
    if (e.target === modal) {
        closeForgot();
    }
});
