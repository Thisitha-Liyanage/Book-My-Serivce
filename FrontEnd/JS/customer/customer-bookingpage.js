$(document).ready(function () {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first!");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    loadBookings();

    function loadBookings() {
        $.ajax({
            url: "http://localhost:8080/api/v1/customer/getNotCompletedBookings",
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                const bookings = response.data;
                const container = $(".booking-list");

                container.empty();

                if (!bookings || bookings.length === 0) {
                    container.append("<p>No bookings found.</p>");
                    return;
                }

                bookings.forEach(booking => {

                    $.ajax({
                        url: `http://localhost:8080/api/v1/customer/getServiceAndProviderDetails/${booking.serviceId}`,
                        method: "GET",
                        headers: {
                            "Authorization": "Bearer " + token
                        },
                        success: function (res) {
                            const data = res.data; // contains service + provider
                            const card = `
                                <div class="booking-card">
                                    <div class="booking-info">
                                        <h2>${data.title}</h2>
                                        <p class="provider">${data.providerName}</p>
                                        <p>Booking Date : <strong>${booking.bookingDate}</strong></p>
                                        <p>Booking Time : <strong>${booking.bookingTime}</strong></p>
                                    </div>

                                    <div class="booking-actions">
                                        <span class="status ${booking.status.toLowerCase()}">
                                            ${booking.status}
                                        </span>
                                        <div class="buttons">
                                            <button class="cancel">Cancel</button>
                                        </div>
                                    </div>
                                </div>
                            `;
                            container.append(card);
                        },
                        error: function (xhr) {
                            console.error("Error fetching service & provider:", xhr.responseText);
                        }
                    });

                });
            },
            error: function (xhr) {
                console.error("Error loading bookings:", xhr.responseText);

                if (xhr.status === 401 || xhr.status === 403) {
                    alert("Session expired. Please login again.");
                    localStorage.removeItem("token");
                    window.location.href = "/FrontEnd/index.html";
                }
            }
        });
    }

});
    // // ❌ Cancel button (optional)
    // $(document).on("click", ".cancel", function () {
    //     const bookingId = $(this).data("id");

    //     if (!confirm("Are you sure you want to cancel this booking?")) return;

    //     $.ajax({
    //         url: `http://localhost:8080/api/v1/customer/cancelBooking/${bookingId}`,
    //         method: "PUT",
    //         headers: {
    //             "Authorization": "Bearer " + token
    //         },
    //         success: function () {
    //             alert("Booking cancelled!");
    //             location.reload();
    //         },
    //         error: function (err) {
    //             console.error("Cancel failed:", err.responseText);
    //         }
    //     });
    // });

// });