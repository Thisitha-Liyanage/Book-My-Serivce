$(document).ready(function () {

    const token = localStorage.getItem("token");

    if (!token) {
        console.error("No token found!");
        return;
    }

    loadProvider(token);

});

// ================= LOAD PROVIDER =================
function loadProvider(token) {
    $.ajax({
        url: "http://localhost:8080/api/v1/provider/findProvider",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {

            const provider = response.data;
            console.log("Provider Data:", provider);

            document.querySelector("h1").innerText =
                "Welcome " + provider.name;

            localStorage.setItem("user", JSON.stringify(provider));

            const nameElement = document.querySelector("#username");
            if (nameElement) {
                nameElement.innerText = provider.name;
            }

            // ✅ CALL ALL DASHBOARD APIs
            loadBookingOverview(provider.id, token);
            loadServiceCount(provider.id, token);
            loadTotalBookings(provider.id, token);

        },
        error: function (xhr) {
            console.error("Error fetching provider:", xhr.responseText);
        }
    });
}

// ================= BOOKING OVERVIEW =================
function loadBookingOverview(providerId, token) {
    $.ajax({
        url: `http://localhost:8080/api/v1/provider/bookingOverview/${providerId}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {

            const data = response.data;

            document.getElementById("pending-count").innerText = data.pending || 0;
            document.getElementById("confirmed-count").innerText = data.accepted || 0;
            document.getElementById("completed-count").innerText = data.completed || 0;
            document.getElementById("cancelled-count").innerText = data.cancelled || 0;
        },
        error: function (xhr) {
            console.error("Error fetching overview:", xhr.responseText);
        }
    });
}

// ================= SERVICE COUNT =================
function loadServiceCount(providerId, token) {
    $.ajax({
        url: `http://localhost:8080/api/v1/provider/serviceCount/${providerId}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {

            const services = response.data;

            // 🔥 If backend returns LIST → use length
            const count = Array.isArray(services) ? services.length : services;

            document.getElementById("service-count").innerText = count;
        },
        error: function (xhr) {
            console.error("Error fetching service count:", xhr.responseText);
        }
    });
}

// ================= TOTAL BOOKINGS =================
function loadTotalBookings(providerId, token) {
    $.ajax({
        url: `http://localhost:8080/api/v1/provider/bookingCount/${providerId}`, 
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            console.log(response);

            // Use data directly
            const count = response.data || 0;

            document.getElementById("total-bookings").innerText = count;
        },
        error: function (xhr) {
            console.error("Error fetching bookings:", xhr.responseText);
        }
    });
}

// ================= AVAILABILITY MODAL =================
function openAvailabilityModal() {
    document.getElementById("availabilityModal").style.display = "flex";
}

function closeAvailabilityModal() {
    document.getElementById("availabilityModal").style.display = "none";
}

function saveAvailability() {
    const token = localStorage.getItem("token");
    const provider = JSON.parse(localStorage.getItem("user"));
    const status = document.getElementById("availabilitySelect").value.toUpperCase(); 
    console.log("New Availability:", status);

    $.ajax({
        url: `http://localhost:8080/api/v1/provider/availability/${provider.id}`,
        method: "PUT",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify({ availability: status }),
        success: function (response) {
            alert("Availability updated to: " + status);
            console.log("Updated provider availability:", response);
            closeAvailabilityModal();
        },
        error: function (xhr) {
            console.error("Error updating availability:", xhr.responseText);
            alert("Failed to update availability");
        }
    });
}
