let isCreateMode = true; // true means Sign-Up side is visible

function toggleCreate() {
    const container = document.querySelector('.container');

    // Toggle the container class
    const isActive = container.classList.toggle('active');

    // Update overlay texts and button
    updateOverlay(isActive);
}

function showLogin() {
    const container = document.querySelector('.container');

    // Make sure login side is visible
    container.classList.remove('active');

    // Update overlay
    updateOverlay(false);
}

function updateOverlay(isActive) {
    const title = document.getElementById('ca-title');
    const text = document.getElementById('ca-text');
    const btn = document.getElementById('create-acc-btn');

    if (isActive) {
        title.textContent = 'Sign In';
        text.textContent = 'Already have an account? Sign in to continue..';
        btn.textContent = 'Sign In'; 
        isCreateMode = false;
    } else {
        title.textContent = 'Welcome';
        text.textContent =
            'Create your account to access our full range of services and book the right service with just a few clicks.';
        btn.textContent = 'Create Account';
        isCreateMode = true;
    }
}
