$(document).ready(function () {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Unauthorized! Please login.");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    loadCustomer();

    function loadCustomer() {
        $.ajax({
            url: "http://localhost:8080/api/v1/customer/foundCustomer", 
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            success: function (response) {
                console.log("Customer Data:", response);
                
            localStorage.setItem("user", JSON.stringify(response.data));

            },
            error: function (xhr) {
                console.error("Error fetching customer:", xhr.responseText);

                if (xhr.status === 403 || xhr.status === 401) {
                    alert("Session expired. Please login again.");
                    localStorage.removeItem("token");
                    window.location.href = "/FrontEnd/index.html";
                }
            }
        });
    }


     loadTopServices();

    function loadTopServices() {
        $.ajax({
            url: "http://localhost:8080/api/v1/customer/getTopRatedServices", // your backend endpoint
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                const services = response.data; // List<TopRatedServiceDto>
                const container = $("#card-container");
                container.empty();

                if (!services || services.length === 0) {
                    container.html("<p>No top services found.</p>");
                    return;
                }

                services.forEach(s => {
                    const card = `
                        <div class="card">
                            <div class="card-header">
                                <span class="star">⭐</span>
                                <h4>${s.avgRating.toFixed(1)}</h4>
                            </div>
                            <div class="details">
                                <p><strong>Title:</strong> ${s.title}</p>
                                <p><strong>Provider:</strong> ${s.providerName}</p>
                                <p><strong>District:</strong> ${s.providerVillage}</p>
                                <p class="description"><strong>Description:</strong> ${s.description}</p>
                                <p><strong>Price:</strong> Rs. ${s.price}</p>
                            </div>
                        </div>
                    `;
                    container.append(card);
                });
            },
            error: function (xhr) {
                console.error("Error fetching top services:", xhr.responseText);
                container.html("<p>Failed to load top services.</p>");
            }
        });
    }

});