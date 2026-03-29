$(document).ready(function() {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first!");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    loadServices();

    function loadServices() {
        $.ajax({
            url: "http://localhost:8080/api/v1/customer/suggestedServices", // your backend endpoint
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function(response) {
                const services = response.data;
                const container = $(".service-grid");
                console.log(services)
                container.empty(); // clear existing static cards

                if (!services || services.length === 0) {
                    container.append("<p>No services found.</p>");
                    return;
                }

                services.forEach(service => {
                    const card = `
                        <div class="service-card">
                            <div class="card-inner">

                                <!-- FRONT -->
                                <div class="card-front">
                                    <h3>${service.title}</h3>
                                    <p><strong>Name:</strong> ${service.providerName}</p>
                                    <p><strong>District:</strong> ${service.providerVillage}</p>
                                    <p class="desc">${service.description}</p>
                                    <span class="price">Rs. ${service.price}</span>
                                </div>

                                <!-- BACK -->
                                <div class="card-back">
                                    <button class="book-btn" data-service-id="${service.id}">
                                        <i class="fa-solid fa-calendar-check"></i>
                                        Book Now
                                    </button>
                                </div>

                            </div>
                        </div>
                    `;
                    container.append(card);
                });

                // Optional: handle book button click
                $(".book-btn").click(function() {
                    const serviceId = $(this).data("service-id");
                    console.log("Book clicked for service ID:", serviceId);
                    // redirect or open booking modal
                });
            },
            error: function(xhr) {
                console.error("Error loading services:", xhr.responseText);
                if (xhr.status === 401 || xhr.status === 403) {
                    alert("Session expired. Please login again.");
                    localStorage.removeItem("token");
                    window.location.href = "/FrontEnd/index.html";
                }
            }
        });
    }

    // // Optional: search filter
    // $(".search-bar").on("input", function() {
    //     const query = $(this).val().toLowerCase();
    //     $(".service-card").each(function() {
    //         const title = $(this).find(".card-front h3").text().toLowerCase();
    //         const desc = $(this).find(".card-front .desc").text().toLowerCase();
    //         $(this).toggle(title.includes(query) || desc.includes(query));
    //     });
    // });

});