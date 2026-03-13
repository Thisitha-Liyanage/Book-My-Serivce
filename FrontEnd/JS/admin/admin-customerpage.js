$(document).ready(function () {
    loadAllCustomers();
});

const token = localStorage.getItem("token");

function loadAllCustomers() {

    $.ajax({
        url: "http://localhost:8080/api/v1/admin/getAllCustomers",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json",
            "Accept": "application/json"
        },

        success: function (response) {

            console.log("Response:", response);

            let tableBody = $("#customerTable");
            tableBody.empty();

            response.data.forEach(function (customer) {

                let row = `
                    <tr>
                        <td>${customer.name || ""}</td>
                        <td>${customer.email || ""}</td>
                        <td>${customer.phone || ""}</td>
                        <td>${customer.province || ""}</td>
                        <td>${customer.city || ""}</td>
                        <td>${customer.village || ""}</td>
                        <td>
                            <button class="delete-btn"
                                onclick="confirmDelete('${customer.email}')">
                                <i class="fa fa-trash"></i> Delete
                            </button>
                        </td>
                    </tr>
                `;

                tableBody.append(row);
            });
        },

        error: function (error) {
            console.error("Error loading customers:", error);
            alert("Failed to load customers");
        }
    });

}

function confirmDelete(email) {

    let confirmAction = confirm("Are you sure you want to delete this customer?");

    if (confirmAction) {
        deleteCustomer(email);
    }
}

function deleteCustomer(email) {

    $.ajax({
        url: "http://localhost:8080/api/v1/admin/deleteCustomer/" + email,
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json",
            "Accept": "application/json"
        },

        success: function (response) {
            alert("Customer deleted successfully!");
            loadAllCustomers(); 
            console.log(response);
        },

        error: function (error) {
            console.error("Delete failed:", error);
            alert("Failed to delete customer");
        }
    });

}

$("#searchInput").on("keypress", function(e) {
    if(e.which === 13){
        let email = $(this).val().trim();
        if(email){
            searchCustomerByEmail(email);
        }
    }
});

let errorDiv = document.getElementById("error-popup")
function searchCustomerByEmail(email){

    $.ajax({
        url: "http://localhost:8080/api/v1/admin/getCustomerByEmail/" + encodeURIComponent(email),
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json",
            "Accept": "application/json"
        },

        success: function(response){
            console.log("Customer found:", response);
            showCustomerPopup(response.data);
        },

        error: function(error){
            console.error("Search failed:", error);
            alert("Customer not found")
        }
    });
}

function showCustomerPopup(customer){
    let detailsHtml = `
        <p><strong>Name:</strong> ${customer.name || ""}</p>
        <p><strong>Email:</strong> ${customer.email || ""}</p>
        <p><strong>Phone:</strong> ${customer.phone || ""}</p>
        <p><strong>Province:</strong> ${customer.province || ""}</p>
        <p><strong>District:</strong> ${customer.city || ""}</p>
        <p><strong>Village:</strong> ${customer.village || ""}</p>
        <button class="delete-btn modal-delete">
                            <i class="fa fa-trash"></i> Delete Customer
                        </button>
    `;
    $("#customerDetails").html(detailsHtml);
    $("#customerModal").fadeIn();

    $("#deleteFromModal").off("click").on("click", function(){
        confirmDelete(customer.email);
        $("#customerModal").fadeOut();
    });
}

$(document).on("click", ".close-btn", function(){
    $("#customerModal").fadeOut();
});


$(window).on("click", function(e){
    if(e.target.id === "customerModal"){
        $("#customerModal").fadeOut();
    }
});