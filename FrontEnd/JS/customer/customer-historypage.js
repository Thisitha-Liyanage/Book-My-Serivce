$(document).ready(function () {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Unauthorized! Please login.");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    // FIRST GET: list of BookingResponseDto
    $.ajax({
        url: "http://localhost:8080/api/v1/customer/bookingHistory", // replace with your endpoint
        method: "GET",
        headers: { "Authorization": "Bearer " + token },
        success: function (response) {
            console.log(response)
            const bookings = response.data;
            const container = $(".history-list");
            container.empty();

            // ✅ CHECK IF EMPTY
            if (!bookings || bookings.length === 0) {
                container.html(`
        <div class="empty-history">
            <h2>No Booking History</h2>
            <p>You haven’t made any bookings yet.</p>
        </div>
    `);
                return; // stop further execution
            }

            // Loop over each booking and make second GET
            bookings.forEach(booking => {
                $.ajax({
                    url: `http://localhost:8080/api/v1/customer/getServiceAndProviderDetails/${booking.serviceId}`, // second GET endpoint
                    method: "GET",
                    headers: { "Authorization": "Bearer " + token },
                    success: function (customerBookings) {
                        console.log(customerBookings)
                        const customerBooking = customerBookings.data;
                        console.log()
                        // customerBooking is CustomerBookingResponse
                        const card = `
                            <div class="history-card">
                                <div class="history-info">
                                    <h2>${customerBooking.title}</h2>
                                    <p class="provider">Provider : ${customerBooking.providerName}</p>
                                    <p>Date : <strong>${formatDate(booking.bookingDate)}</strong></p>
                                    <p>Time : <strong>${formatTime(booking.bookingTime)}</strong></p>
                                    <p class="price">Price : Rs. ${customerBooking.price}</p>
                                </div>
                                <div class="history-actions">
                                    <span class="status ${customerBooking.status.toLowerCase()}">${customerBooking.status}</span>
                                    <button class="submit-rating" data-service-id="${booking.serviceId}">
                                        Submit Rating
                                    </button>
                                    <button class="book-again" data-service-id="${booking.serviceId}">Book Again</button>
                                </div>
                            </div>
                        `;
                        container.append(card);
                    },
                    error: function (err) {
                        console.error("Error fetching customer booking:", err);
                    }
                });
            });
        },
        error: function (err) {
            console.error("Error fetching bookings:", err);
            alert("Failed to load your booking history.");
        }
    });

    // HELPER FUNCTIONS
    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return `${String(date.getDate()).padStart(2, '0')}-${String(date.getMonth() + 1).padStart(2, '0')}-${date.getFullYear()}`;
    }

    function formatTime(timeStr) {
        const [hourStr, min] = timeStr.split(":");
        let hour = parseInt(hourStr);
        const ampm = hour >= 12 ? "PM" : "AM";
        hour = hour % 12 || 12;
        return `${hour}.${min} ${ampm}`;
    }

    // Book again button
    $(document).on("click", ".book-again", function () {
        const serviceId = $(this).data("service-id");
        window.location.href = `/FrontEnd/Pages/customer/customer-addbookingpage.html?serviceId=${serviceId}`;
    });
});

$(document).on("click", ".submit-rating", function () {
    const serviceId = $(this).data("service-id");

    console.log("Redirect serviceId:", serviceId); // DEBUG

    window.location.href = `/FrontEnd/Pages/customer/customer-ratingpage.html?serviceId=${serviceId}`;
});