const token = localStorage.getItem("token");
$(document).ready(function () {


    loadAllProviders();

    // Load all providers
    function loadAllProviders() {

        $.ajax({
            url: "http://localhost:8080/api/v1/admin/getAllProviders",
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },

            success: function (response) {

                $("#providerTable").empty();

                response.data.forEach(provider => {

                    let row = `
                        <tr>
                            <td>${provider.name}</td>
                            <td>${provider.email}</td>
                            <td>${provider.phone}</td>
                            <td>${provider.province}</td>
                            <td>${provider.city}</td>
                            <td>${provider.village}</td>
                            <td>
                                <button class="delete-btn" data-email="${provider.email}">
                                    <i class="fa fa-trash"></i> Delete
                                </button>
                            </td>
                        </tr>
                    `;

                    $("#providerTable").append(row);
                });

            },

            error: function (err) {
                console.log(err);
                alert("Failed to load providers");
            }
        });
    }


    // Delete provider from table
    $(document).on("click", ".delete-btn", function () {

        const email = $(this).data("email");

        if (!confirm("Are you sure you want to delete this provider?")) return;

        $.ajax({
            url: "http://localhost:8080/api/v1/admin/deleteProvider/" + encodeURIComponent(email),
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            },

            success: function () {
                alert("Provider deleted successfully");
                loadAllProviders();
                $("#providerModal").hide();
            },

            error: function (err) {
                console.log(err);
                alert("Delete failed");
            }

        });

    });


    // Search provider when Enter pressed
    $("#searchInput").keypress(function (e) {

        if (e.which === 13) {

            const email = $(this).val().trim();

            if (email === "") {
                alert("Please enter provider email");
                return;
            }

            $.ajax({
                url: "http://localhost:8080/api/v1/admin/getProviderByEmail/" + encodeURIComponent(email),
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                },

                success: function (response) {

                    const provider = response.data;

                    let details = `
                        <p><strong>Name:</strong> ${provider.name || ""}</p>
                        <p><strong>Email:</strong> ${provider.email || ""}</p>
                        <p><strong>Phone:</strong> ${provider.phone || ""}</p>
                        <p><strong>Province:</strong> ${provider.province || ""}</p>
                        <p><strong>City:</strong> ${provider.city || ""}</p>
                        <p><strong>Village:</strong> ${provider.village || ""}</p>

                        <button class="delete-btn modal-delete" data-email="${provider.email}">
                            <i class="fa fa-trash"></i> Delete Provider
                        </button>
                    `;

                    $("#providerDetails").html(details);
                    $("#providerModal").fadeIn();

                },

                error: function () {
                    alert("Provider not found");
                }
            });

        }

    });


    // Close modal button
    $(".close-btn").click(function () {
        $("#providerModal").fadeOut();
    });


    // Close modal when clicking outside
    $(window).click(function (e) {

        if (e.target.id === "providerModal") {
            $("#providerModal").fadeOut();
        }

    });

});