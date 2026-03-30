$(document).ready(function () {

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
            success: function (response) {
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

                                    <button class="view-rating-btn" data-service-id="${service.id}">
                                        ⭐ View Ratings
                                    </button>
                                </div>

                            </div>
                        </div>
                    `;
                    container.append(card);
                });

                // Optional: handle book button click
                $(".book-btn").click(function () {
                    const serviceId = $(this).data("service-id");
                    console.log("Book clicked for service ID:", serviceId);
                    // redirect or open booking modal
                });
            },
            error: function (xhr) {
                console.error("Error loading services:", xhr.responseText);
                if (xhr.status === 401 || xhr.status === 403) {
                    alert("Session expired. Please login again.");
                    localStorage.removeItem("token");
                    window.location.href = "/FrontEnd/index.html";
                }
            }
        });
    }
    $(document).on("click", ".book-btn", function () {
        const serviceId = $(this).data("service-id");

        window.location.href = `/FrontEnd/Pages/customer/customer-addbookingpage.html?serviceId=${serviceId}`;
    });




    $(document).on("click", ".view-rating-btn", function () {
        const serviceId = $(this).data("service-id");
        const token = localStorage.getItem("token");

        $("#ratingModal").fadeIn();

        $.ajax({
            url: `http://localhost:8080/api/v1/customer/getRatingsByService/${serviceId}`,
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                const ratings = response.data;
                const container = $("#ratingList");
                console.log(ratings)
                container.empty();

                if (!ratings || ratings.length === 0) {
                    container.html("<p>No ratings yet.</p>");
                    return;
                }

                ratings.forEach(r => {
                    const stars = "⭐".repeat(r.rating);

                    const item = `
                    <div class="rating-item">
                        <div class="rating-stars">${stars}</div>
                        <p>${r.description || "No comment"}</p>
                        <small>By ${r.customerName || "User"}</small>
                    </div>
                `;
                    container.append(item);
                });
            },
            error: function (err) {
                console.error(err);
                $("#ratingList").html("<p>Failed to load ratings</p>");
            }
        });
    });

    $(document).on("click", ".close-modal", function () {
        $("#ratingModal").fadeOut();
    });

    // click outside to close
    $(window).click(function (e) {
        if ($(e.target).is("#ratingModal")) {
            $("#ratingModal").fadeOut();
        }
    });

});


$(document).on("click", "#filterBtn", function() {
    const selectedCategory = $("#categorySelect").val(); 
    const token = localStorage.getItem("token");
    console.log(selectedCategory);
    
    $.ajax({
        url: `http://localhost:8080/api/v1/customer/getServiceByCategory/${selectedCategory}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function(response) {
            const services = response.data;
            const container = $(".service-grid");
            container.empty();

            if (!services || services.length === 0) {
                container.append("<p>No services found for this category.</p>");
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
                                    <i class="fa-solid fa-calendar-check"></i> Book Now
                                </button>
                                <button class="view-rating-btn" data-service-id="${service.id}">
                                    ⭐ View Ratings
                                </button>
                            </div>
                        </div>
                    </div>
                `;
                container.append(card);
            });
        },
        error: function(xhr) {
            console.error("Error filtering services:", xhr.responseText);
            alert("Failed to load services. Please try again.");
        }
    });
});