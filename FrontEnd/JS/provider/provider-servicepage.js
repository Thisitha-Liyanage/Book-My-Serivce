
    let currentRow = null;

    function openUpdateModal(button) {
        currentRow = button.closest("tr");

        const cells = currentRow.getElementsByTagName("td");

        document.getElementById("updateId").value = cells[0].innerText;
        document.getElementById("updateName").value = cells[1].innerText;
        document.getElementById("updateCategory").value = cells[2].innerText;
        document.getElementById("updatePrice").value = cells[3].innerText;
        document.getElementById("updateDescription").value = cells[4].innerText;
        document.getElementById("updateDistrict").value = cells[5].innerText;

        const statusText = cells[6].innerText.trim();
        document.getElementById("updateStatus").value = statusText;

        document.getElementById("updateModal").style.display = "flex";
    }

    function closeModal() {
        document.getElementById("updateModal").style.display = "none";
    }

    function saveUpdate() {
        if (!currentRow) return;

        const cells = currentRow.getElementsByTagName("td");

        cells[1].innerText = document.getElementById("updateName").value;
        cells[2].innerText = document.getElementById("updateCategory").value;
        cells[3].innerText = document.getElementById("updatePrice").value;
        cells[4].innerText = document.getElementById("updateDescription").value;
        cells[5].innerText = document.getElementById("updateDistrict").value;

        const status = document.getElementById("updateStatus").value;

        if (status === "Active") {
            cells[6].innerHTML = '<span class="status status-available">Active</span>';
        } else {
            cells[6].innerHTML = '<span class="status status-unavailable">Inactive</span>';
        }

        closeModal();
    }

let confirmCallback = null;

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
    if (confirmCallback) {
        confirmCallback();
    }
    closeConfirmModal();
});

document.getElementById("serviceTable").addEventListener("click", function (e) {

    const deleteBtn = e.target.closest(".delete-btn");

    if (deleteBtn) {
        const row = deleteBtn.closest("tr");

        openConfirmModal(
            "Delete Service",
            "Are you sure you want to delete this service?",
            function () {
                row.remove();
            }
        );
    }

});

function openAddModal() {
    document.getElementById("addModal").style.display = "flex";
}

function closeAddModal() {
    document.getElementById("addModal").style.display = "none";
}

function confirmAddService() {
    openConfirmModal(
        "Add Service",
        "Are you sure you want to add this service?",
        addService
    );
}