$(document).ready(function () {

    loadAllCustomers();

});

function loadAllCustomers() {

    $.ajax({
        url: "http://localhost:8080/api/v1/admin/get-all-customers",
        method: "GET",
        success: function (response) {
            let tableBody = $("#customerTable");
            tableBody.empty();

            response.forEach(function (customer) {

                let row = `
                    <tr>
                        <td>${customer.id}</td>
                        <td>${customer.username}</td>
                        <td>${customer.email}</td>
                        <td>${customer.phone}</td>
                        <td>${customer.province}</td>
                        <td>${customer.district}</td>
                        <td>${customer.village}</td>
                        <td>
                            <button class="delete-btn" 
                                onclick="confirmDelete(${customer.id})">
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

function confirmDelete(id) {

    let confirmAction = confirm("Do you really want to delete this customer?");

    if (confirmAction) {
        deleteCustomer(id);
    }
}

function deleteCustomer(id){
    console.log(id)
     $.ajax({
        url: `http://localhost:8080/api/v1/admin/delete-customer/${id}`,
        method: "DELETE",
        success: function (response) {
            alert("Customer deleted successfully!");
            loadAllCustomers(); 
        },
        error: function (error) {
            console.error("Failed to delete customer:", error);
            alert("Failed to delete customer");
        }
    });
}