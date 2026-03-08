document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    console.log("Token from localStorage on page load:", token);
});

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

