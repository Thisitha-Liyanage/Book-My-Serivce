document.addEventListener("DOMContentLoaded", () => {
    const sidebarContainer = document.getElementById("sidebar");

    if (!sidebarContainer) {
        console.error("Sidebar container not found!");
        return;
    }

    const userString = localStorage.getItem("user");
    let user = null;

    if (userString) {
        try {
            user = JSON.parse(userString);
        } catch (err) {
            console.error("Failed to parse user from localStorage", err);
        }
    }

    fetch("/FrontEnd/components/admin-sidebar.html")
        .then(res => res.text())
        .then(html => {
            sidebarContainer.innerHTML = html;

            // Highlight current page
            const currentPage = window.location.pathname.split("/").pop();
            const navLinks = sidebarContainer.querySelectorAll(".nav-item");

            navLinks.forEach(link => {
                const linkPage = link.getAttribute("href").split("/").pop();
                if (linkPage === currentPage) link.classList.add("active");

                link.addEventListener("click", () => {
                    navLinks.forEach(l => l.classList.remove("active"));
                    link.classList.add("active");
                });
            });

            // Set admin name
            if (user && user.name) {
                const nameElement = sidebarContainer.querySelector(".user-name");
                if (nameElement) nameElement.innerText = user.name;
            } else {
                console.warn("No user found in localStorage");
            }

            // Logout button
            const logoutBtn = sidebarContainer.querySelector("#logout-btn");
            if (logoutBtn) {
                logoutBtn.addEventListener("click", e => {
                    e.preventDefault();
                    localStorage.clear();
                    window.location.href = "../index.html";
                });
            }
        })
        .catch(err => console.error("Admin sidebar load failed", err));
});