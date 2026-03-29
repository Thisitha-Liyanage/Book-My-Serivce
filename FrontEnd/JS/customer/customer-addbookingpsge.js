$(document).ready(function () {
    const token = localStorage.getItem("token");

    // get serviceId from URL
    const params = new URLSearchParams(window.location.search);
    const serviceId = params.get("serviceId");

    if (!serviceId) {
        alert("No service selected!");
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/v1/customer/getServiceAndProviderDetails/${serviceId}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            const service = response.data;
            console.log(service);
            localStorage.setItem("selectedService", service)

            $("#serviceTitle").val(service.title);
            $("#providerName").val(service.providerName);
            $("#category").val(service.category);
            $("#city").val(service.providerVillage);
            $("#price").val("Rs. " + service.price);
        },
        error: function (err) {
            console.error("Error loading service details:", err);
        }
    });
});


$(document).on("click", "#bookBtn", function () {

    const dateInput = $("#bookingDate");
    const timeInput = $("#bookingTime");

    const date = dateInput.val();
    const time = timeInput.val();

    let isValid = true;

    // reset borders
    dateInput.css("border", "");
    timeInput.css("border", "");

    if (!date) {
        dateInput.css("border", "2px solid red");
        isValid = false;
    }
    if (!time) {
        timeInput.css("border", "2px solid red");
        isValid = false;
    }

    if (!isValid) {
        alert("Please select date and time!");
        return;
    }

    const params = new URLSearchParams(window.location.search);
    const serviceId = params.get("serviceId");

    if (!serviceId) {
        alert("Service or user not found!");
        return;
    }

    const user = JSON.parse(localStorage.getItem("user"));
    const token = localStorage.getItem("token");

    const bookingData = {
        serviceId: parseInt(serviceId),
        bookingDate: date,
        bookingTime: time,
        status: "PENDING"
    };
    console.log(bookingData);

    $.ajax({
        url: "http://localhost:8080/api/v1/customer/bookService",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(bookingData),
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            alert("Booking successful! 🎉  , Wait for provider to accept your boooking");
            window.location.href = "/FrontEnd/pages/customer/customer-history-page.html";
        },
        error: function (xhr) {
            console.error("Booking failed:", xhr.responseText);
            if (xhr.status === 401 || xhr.status === 403) {
                alert("Session expired. Please login again.");
                localStorage.removeItem("token");
                window.location.href = "/FrontEnd/index.html";
            } else {
                alert("Failed to book service. Please check your date/time or try again.");
            }
        }
    });
});