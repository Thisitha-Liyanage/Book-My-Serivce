let currentRow = null;
let confirmCallback = null;

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
        headers: { Authorization: "Bearer " + token },
        success: function (response) {
            const provider = response.data;
            console.log("Provider Data:", provider);

            document.querySelector("h1").innerText = "Welcome " + provider.name;
            localStorage.setItem("user", JSON.stringify(provider));

            const nameElement = document.querySelector("#username");
            if (nameElement) nameElement.innerText = provider.name;

            // Load services table
            loadServices(provider.id, token);

            // Optional: dashboard info
            loadBookingOverview(provider.id, token);
            loadServiceCount(provider.id, token);
            loadTotalBookings(provider.id, token);
        },
        error: function (xhr) {
            console.error("Error fetching provider:", xhr.responseText);
        },
    });
}

// ================= LOAD SERVICES =================
function loadServices(providerId, token) {
    $.ajax({
        url: `http://localhost:8080/api/v1/provider/getServices/${providerId}`,
        method: "GET",
        headers: { Authorization: "Bearer " + token },
        success: function (response) {
            console.log("Service Response:", response);

            const services = response.data;

            if (!Array.isArray(services)) {
                console.error("Services is not an array:", services);
                return;
            }

            const table = $("#serviceTable");
            table.empty();

            services.forEach((service) => {
                let availability;

                if (service.availability === "AVAILABLE") {
                    availability = "Active";
                } else {
                    availability = "Inactive";
                }

                const row = `
    <tr>
        <td>${service.id}</td>
        <td>${service.title}</td>
        <td>${service.category}</td>
        <td>${service.price}</td>
        <td>${service.description}</td>
        <td>
            <span class="status status-available">${availability}</span>
        </td>
        <td>
            <div class="action-buttons">
                <button class="btn edit-btn" onclick="openUpdateModal(this)">Update</button>
                <button class="btn delete-btn">Delete</button>
            </div>
        </td>
    </tr>
    `;
                table.append(row);
            });
        },
        error: function (xhr) {
            console.error("Error loading services:", xhr.responseText);
        },
    });
}

// ================= UPDATE MODAL =================
function openUpdateModal(button) {
    currentRow = button.closest("tr");
    const cells = currentRow.getElementsByTagName("td");

    document.getElementById("updateId").value = cells[0].innerText;
    document.getElementById("updateName").value = cells[1].innerText;
    document.getElementById("updateCategory").value = cells[2].innerText;
    document.getElementById("updatePrice").value = cells[3].innerText;
    document.getElementById("updateDescription").value = cells[4].innerText;

    document.getElementById("updateModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("updateModal").style.display = "none";
}

// ================= CONFIRM UPDATE =================
function confirmSave() {
    openConfirmModal(
        "Update Service",
        "Are you sure you want to update this service?",
        saveUpdate,
    );
}

function saveUpdate() {
    const token = localStorage.getItem("token");
    const provider = JSON.parse(localStorage.getItem("user"));

    const updatedService = {
        id: parseInt(document.getElementById("updateId").value),
        title: document.getElementById("updateName").value,
        category: document.getElementById("updateCategory").value, // enum constant
        price: parseFloat(document.getElementById("updatePrice").value),
        description: document.getElementById("updateDescription").value,
        availability: document.getElementById("updateStatus").value, // AVAILABLE / UNAVAILABLE
        userId: parseInt(provider.id),
    };

    console.log("Updating service:", updatedService);

    $.ajax({
        url: "http://localhost:8080/api/v1/provider/updateService",
        method: "PUT",
        contentType: "application/json",
        headers: {
            Authorization: "Bearer " + token,
        },
        data: JSON.stringify(updatedService),

        success: function (response) {
            console.log("Service updated:", response);
            alert("Service updated successfully!");

            closeModal();

            // 🔄 reload table from DB
            loadServices(provider.id, token);
        },

        error: function (xhr) {
            console.error("Update error:", xhr.responseText);
            alert("Failed to update service");
        },
    });
}

// List of categories (must match the DB display names exactly)
const categories = [
    { value: "HOME_AND_TECHNICAL_SERVICE", label: "Home and Technical Service" },
    { value: "VEHICLE_SERVICE", label: "Vehicle Service" },
    { value: "IT_AND_DIGITAL_SERVICE", label: "IT and Digital Service" },
    { value: "BEAUTY_AND_PERSONAL_CARE", label: "Beauty and Personal Care" },
    { value: "HEALTH_AND_WELLNESS", label: "Health and Wellness" },
    { value: "FOOD_SERVICE", label: "Food Service" },
    { value: "EVENT_AND_MEDIA", label: "Event and Media" },
    { value: "PROFESSIONAL_SERVICE", label: "Professional Service" },
];

function populateCategoryDropdown() {
    const addCategorySelect = document.getElementById("addCategory");
    const updateCategorySelect = document.getElementById("updateCategory");

    categories.forEach((cat) => {
        const addOption = document.createElement("option");
        addOption.value = cat.value;
        addOption.text = cat.label;
        addCategorySelect.appendChild(addOption);

        const updateOption = document.createElement("option");
        updateOption.value = cat.value; // send enum value to backend
        updateOption.text = cat.label; // show friendly name
        updateCategorySelect.appendChild(updateOption);
    });
}

// Call this after the page loads
document.addEventListener("DOMContentLoaded", populateCategoryDropdown);
// ================= CONFIRM MODAL =================
function openConfirmModal(title, message, callback) {
    document.getElementById("confirmTitle").innerText = title;
    document.getElementById("confirmMessage").innerText = message;
    document.getElementById("confirmModal").style.display = "flex";

    confirmCallback = callback;
}

function closeConfirmModal() {
    document.getElementById("confirmModal").style.display = "none";
    confirmCallback = null;
}

document.getElementById("confirmYes").addEventListener("click", function () {
    if (confirmCallback) confirmCallback();
    closeConfirmModal();
});

// ================= DELETE =================
document.getElementById("serviceTable").addEventListener("click", function (e) {
    const deleteBtn = e.target.closest(".delete-btn");

    if (deleteBtn) {
        const row = deleteBtn.closest("tr");

        openConfirmModal(
            "Delete Service",
            "Are you sure you want to delete this service?",
            function () {
                // ================= DELETE =================
                document
                    .getElementById("serviceTable")
                    .addEventListener("click", function (e) {
                        const deleteBtn = e.target.closest(".delete-btn");

                        if (deleteBtn) {
                            const row = deleteBtn.closest("tr");
                            const serviceId = row.cells[0].innerText; // assuming ID is in first column
                            const token = localStorage.getItem("token");

                            openConfirmModal(
                                "Delete Service",
                                "Are you sure you want to delete this service?",
                                function () {
                                    $.ajax({
                                        url: `http://localhost:8080/api/v1/provider/deleteService/${serviceId}`,
                                        method: "DELETE",
                                        headers: {
                                            Authorization: "Bearer " + token,
                                        },
                                        success: function (response) {
                                            console.log("Service deleted:", response);
                                            alert("Service deleted successfully!");
                                            row.remove();
                                        },
                                        error: function (xhr) {
                                            console.error(
                                                "Error deleting service:",
                                                xhr.responseText,
                                            );
                                            alert("Failed to delete service!");
                                        },
                                    });
                                },
                            );
                        }
                    });
            },
        );
    }
});

// ================= ADD MODAL =================
function openAddModal() {
    document.getElementById("addModal").style.display = "flex";
}

function closeAddModal() {
    document.getElementById("addModal").style.display = "none";
}

// ================= ADD SERVICE =================
function confirmAddService() {
    openConfirmModal(
        "Add Service",
        "Are you sure you want to add this service?",
        addService,
    );
}

function addService() {
    const token = localStorage.getItem("token");
    const provider = JSON.parse(localStorage.getItem("user"));

    const serviceData = {
        title: document.getElementById("addName").value,
        category: document.getElementById("addCategory").value,
        price: document.getElementById("addPrice").value,
        description: document.getElementById("addDescription").value,
        availability: document.getElementById("addStatus").value,
        providerId: provider.id,
    };

    console.log(serviceData);
    $.ajax({
        url: "http://localhost:8080/api/v1/provider/saveService",
        method: "POST",
        contentType: "application/json",
        headers: {
            Authorization: "Bearer " + token,
        },
        data: JSON.stringify(serviceData),

        success: function (response) {
            console.log("Service saved:", response);
            alert("Service added successfully!");

            closeAddModal();

            // 🔄 reload services from DB
            loadServices(provider.id, token);
        },

        error: function (xhr) {
            console.error("Error saving service:", xhr.responseText);

            if (xhr.status === 409) {
                alert("Duplicate service!");
            } else {
                alert("Failed to save service");
            }
        },
    });
}

// Search by service ID
$("#searchInput").on("keypress", function (e) {
    if (e.which === 13) {
        // Enter key
        const serviceId = $(this).val().trim();
        if (serviceId) {
            fetchServiceById(serviceId);
        }
    }
});

function fetchServiceById(serviceId) {
    const token = localStorage.getItem("token");
    if (!token) return;

    $.ajax({
        url: `http://localhost:8080/api/v1/provider/findServiceByID/${serviceId}`,
        method: "GET",
        headers: { Authorization: "Bearer " + token },
        success: function (response) {
            const service = response.data;
            console.log(response);
            if (!service) {
                alert("Service not found!");
                return;
            }
            openServiceModalWithData(service);
        },
        error: function (xhr) {
            console.error("Error fetching service:", xhr.responseText);
            alert("Service not found!");
        },
    });
}

function openServiceModalWithData(service, row = null) {
    // Fill modal data
    document.getElementById("detailId").innerText = service.id;
    document.getElementById("detailName").innerText = service.title;
    document.getElementById("detailCategory").innerText = service.category;
    document.getElementById("detailPrice").innerText = service.price;
    document.getElementById("detailDescription").innerText = service.description;
    document.getElementById("detailStatus").innerText =
        service.status || "Active";

    // Store the row if this modal was opened from the table
    currentRow = row;

    // Update button
    const modalUpdateBtn = document.getElementById("modalUpdateBtn");
    if (modalUpdateBtn) {
        modalUpdateBtn.onclick = function () {
            document.getElementById("updateId").value = service.id;
            document.getElementById("updateName").value = service.title;
            document.getElementById("updateCategory").value = service.category;
            document.getElementById("updatePrice").value = service.price;
            document.getElementById("updateDescription").value = service.description;
            document.getElementById("updateStatus").value =
                service.status || "Active";

            closeServiceModal();
            openUpdateModal(document.createElement("button")); // dummy button
        };
    }

    // Delete button
    const modalDeleteBtn = document.getElementById("modalDeleteBtn");
    if (modalDeleteBtn) {
        modalDeleteBtn.onclick = function () {
            if (!currentRow) {
                alert("Cannot delete service from search result");
                return;
            }

            openConfirmModal(
                "Delete Service",
                "Are you sure you want to delete this service?",
                function () {
                    currentRow.remove();
                    closeServiceModal();
                },
            );
        };
    }

    // Show the modal
    document.getElementById("serviceModal").classList.add("show");
}
// ================= SERVICE MODAL =================
function closeServiceModal() {
    const modal = document.getElementById("serviceModal");
    if (modal) {
        modal.classList.remove("show");
    }
}

// After document is ready
document.addEventListener("DOMContentLoaded", function () {
    const closeBtn = document.querySelector("#serviceModal .close-btn");
    if (closeBtn) {
        closeBtn.addEventListener("click", closeServiceModal);
    }
});
