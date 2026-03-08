document.addEventListener("DOMContentLoaded", () => {
    const sidebarContainer = document.getElementById("sidebar");

    if (!sidebarContainer) {
        console.error("Sidebar container not found!");
        return;
    }

    fetch("/FrontEnd/components/providersidebar.html")
        .then(res => res.text())
        .then(html => {
            sidebarContainer.innerHTML = html;

            const currentPage = window.location.pathname.split("/").pop();
            const navLinks = document.querySelectorAll(".nav-item");

            navLinks.forEach(link => {
                const linkPage = link.getAttribute("href").split("/").pop();

                if (linkPage === currentPage) {
                    link.classList.add("active");
                }

                link.addEventListener("click", () => {
                    navLinks.forEach(l => l.classList.remove("active"));
                    link.classList.add("active");
                });
            });
        })
        .catch(err => console.error("Admin sidebar load failed", err));
});
