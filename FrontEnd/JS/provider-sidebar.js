document.addEventListener("DOMContentLoaded", () => {
    const sidebarContainer = document.getElementById("sidebar");

    const userString = localStorage.getItem("user");
    let user = null;
    console.log(userString);

    if (userString) {
        try {
            user = JSON.parse(userString);
        } catch (err) {
            console.error("Failed to parse user from localStorage", err);
        }
    }

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

            // ✅ Add timeout here
            setTimeout(() => {
                if (user && user.name) {
                    const nameElement = document.querySelector("#username");
                    if (nameElement) {
                        nameElement.innerText = user.name;
                    }
                } else {
                    console.warn("No user found in localStorage");
                }
            }, 100); // 100ms delay
        })
        .catch(err => console.error("Admin sidebar load failed", err));
});