document.addEventListener("DOMContentLoaded", () => {
    const sidebarContainer = document.getElementById("sidebar");

    if (!sidebarContainer) {
        console.error("Sidebar container not found!");
        return;
    }

    fetch("/FrontEnd/components/sidebar.html")
        .then(res => res.text())
        .then(html => {
            sidebarContainer.innerHTML = html;

            // ⏳ Small timeout to ensure elements are rendered
            setTimeout(() => {

                // ================= SET ACTIVE LINK =================
                const currentPage = window.location.pathname.split("/").pop();
                const navLinks = document.querySelectorAll(".nav-item");

                navLinks.forEach(link => {
                    const linkPage = link.getAttribute("href")?.split("/").pop();

                    if (linkPage === currentPage) {
                        link.classList.add("active");
                    }

                    link.addEventListener("click", () => {
                        navLinks.forEach(l => l.classList.remove("active"));
                        link.classList.add("active");
                    });
                });

                // ================= LOAD USER FROM LOCALSTORAGE =================
                const userData = localStorage.getItem("user");

                if (userData) {
                    const user = JSON.parse(userData);

                    const userNameElement = document.querySelector(".user-name");

                    if (userNameElement) {
                        // Change according to your DTO field (name/email)
                        userNameElement.textContent = user.name || user.email || "User";
                    }
                } else {
                    console.warn("No user found in localStorage");
                }

            }, 100); // 100ms delay

        })
        .catch(err => console.error("Sidebar load failed", err));
});