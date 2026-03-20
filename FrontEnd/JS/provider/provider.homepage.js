document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    console.log("Token from localStorage on page load:", token);

    if (!token) {
        console.error("No token found!");
        return;
    }

    loadProvider(token);
});

function loadProvider(token) {
    $.ajax({
        url: "http://localhost:8080/api/v1/provider/findProvider",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            console.log("Provider Data:", response);

            const provider = response.data;

            // ✅ Update heading
            document.querySelector("h1").innerText =
                "Welcome " + provider.name;

            // ✅ (Recommended) Save correct user
            localStorage.setItem("user", JSON.stringify(provider));

            // ✅ Update sidebar name if already loaded
            const nameElement = document.querySelector("#username");
            if (nameElement) {
                nameElement.innerText = provider.name;
            }
        },
        error: function (xhr, status, error) {
            console.error("Error fetching provider:", error);
            console.error("Status:", xhr.status);
            console.error("Response:", xhr.responseText);
        }
    });
}
function openAvailabilityModal() {
    document.getElementById("availabilityModal").style.display = "flex";
}

function closeAvailabilityModal() {
    document.getElementById("availabilityModal").style.display = "none";
}

function saveAvailability() {
    const status = document.getElementById("availabilitySelect").value;
    alert("Availability changed to: " + status);
    closeAvailabilityModal();
}

