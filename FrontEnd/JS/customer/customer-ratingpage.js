$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const serviceId = urlParams.get("serviceId");

    console.log("Service ID:", serviceId);

    const token = localStorage.getItem("token");

    $.ajax({
        url: `http://localhost:8080/api/v1/customer/getServiceAndProviderDetails/${serviceId}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            console.log("Service Details:", response);

            const service = response.data;

            $("#serviceTitle").text(service.title);
            $("#providerName").text(service.providerName);

        },
        error: function (err) {
            console.error("Error loading service:", err);
            alert("Failed to load service details");
        }
    });
});

let selectedRating = 0;

// ⭐ Handle star click
$(document).on("click", ".star", function () {
    selectedRating = $(this).data("value");

    $(".star").removeClass("active");

    $(".star").each(function () {
        if ($(this).data("value") <= selectedRating) {
            $(this).addClass("active");
        }
    });
});

// 🚀 Submit rating
$("#submitRating").click(function () {

    const review = $("#review").val();

    if (selectedRating === 0) {
        alert("Please select a rating!");
        return;
    }

    const params = new URLSearchParams(window.location.search);
    const serviceId = params.get("serviceId");

    const token = localStorage.getItem("token");

    // ✅ FIX: parse user object
    const customer = JSON.parse(localStorage.getItem("user"));
    console.log(customer);

    const ratingData = {
        rating: selectedRating,
        description: review,
        serviceId: parseInt(serviceId),
    };

    console.log(ratingData);

    $.ajax({
        // ✅ FIX: dynamic URL
        url: `http://localhost:8080/api/v1/customer/addRating`,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(ratingData),
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function () {
            alert("Rating submitted successfully ⭐");
            window.location.href = "/FrontEnd/pages/customer/customer-home-page.html";
        },
        error: function (err) {
            console.error(err);
            alert("Failed to submit rating");
        }
    });
});