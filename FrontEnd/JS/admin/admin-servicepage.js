const token = localStorage.getItem("token");

const headers = {
    "Authorization": "Bearer " + token,
    "Content-Type": "application/json",
    "Accept": "application/json"
};

$(document).ready(function () {

     $("#providerModal").hide();
     
    if (!token) {
        console.error("No auth token found!");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    loadServices();

    // SEARCH SERVICE BY ENTER
    $("#searchInput").keydown(function (e) {

        if (e.key === "Enter") {

            let serviceId = $(this).val().trim();

            if (serviceId === "") {
                alert("Enter Service ID");
                return;
            }

            $.ajax({
                url: "http://localhost:8080/api/v1/admin/getServiceByID/" + serviceId,
                method: "GET",
                headers: headers,

                success: function (response) {

                    if (!response.data) {
                        alert("Service not found");
                        return;
                    }

                    let s = response.data;

                    $("#providerDetails").html(`
                        <p><b>ID:</b> ${s.id}</p>
                        <p><b>Category:</b> ${s.category}</p>
                        <p><b>Title:</b> ${s.title}</p>
                        <p><b>Provider ID:</b> ${s.userId}</p>
                        <p><b>Price:</b> LKR ${s.price}</p>
                        <p><b>Description:</b> ${s.description}</p>
                        <button class="delete-btn" data-id="${s.id}">
                            <i class="fa-solid fa-trash"></i> Delete Service
                        </button>
                    `);

                    $("#providerModal").fadeIn();

                },

                error: function () {
                    alert("Service not found!");
                }

            });

        }

    });


    // CLOSE MODAL BUTTON
    $(".close-btn").click(function () {
        $("#providerModal").fadeOut();
    });


    // CLOSE MODAL WHEN CLICK OUTSIDE
    $(window).click(function (e) {

        if ($(e.target).is("#providerModal")) {
            $("#providerModal").fadeOut();
        }

    });

});


// LOAD ALL SERVICES
function loadServices(){

    $.ajax({
        url: "http://localhost:8080/api/v1/admin/getAllServices",
        method: "GET",
        headers: headers,

        success: function (response) {

            let tableBody = $("#serviceTable");
            tableBody.empty();

            if (response.data && response.data.length > 0) {

                response.data.forEach(service => {

                    let row = `
                        <tr>
                            <td>${service.id}</td>
                            <td>${service.category}</td>
                            <td>${service.title}</td>
                            <td>${service.userId}</td>
                            <td>${service.price}</td>
                            <td>${service.description}</td>
                            <td>Active</td>
                            <td>
                                <button class="delete-btn" data-id="${service.id}">
                                    <i class="fa-solid fa-trash"></i> Delete
                                </button>
                            </td>
                        </tr>
                    `;

                    tableBody.append(row);

                });

            } else {

                tableBody.append("<tr><td colspan='8'>No services found</td></tr>");

            }

        }

    });

}

$(document).on("click", ".delete-btn", function () {

    let serviceId = $(this).data("id");

    let confirmDelete = confirm("Are you sure you want to delete this service?");

    if (!confirmDelete) {
        return;
    }

    $.ajax({
        url: "http://localhost:8080/api/v1/admin/deleteService/" + serviceId,
        method: "DELETE",
        headers: headers,

        success: function (response) {

            alert("Service deleted successfully!");

            $("#providerModal").fadeOut();

            loadServices(); // reload table

        },

        error: function () {
            alert("Failed to delete service!");
        }

    });

});