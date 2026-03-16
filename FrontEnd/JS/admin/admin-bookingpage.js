$(document).ready(function () {
    const token = localStorage.getItem("token");

    if (!token) {
        console.error("No auth token found!");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    const headers = {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json",
        "Accept": "application/json"
    };

    loadBookings();

    // Load all bookings
    function loadBookings() {
        $.ajax({
            url: "http://localhost:8080/api/v1/admin/getAllBookings",
            method: "GET",
            headers: headers,
            success: function (response) {
                const tableBody = $("#bookingTable");
                tableBody.empty();

                if (response && response.data) {
                    response.data.forEach(function (booking) {
                        let row = `
                            <tr>
                                <td>${booking.id}</td>
                                <td>${booking.serviceId}</td>
                                <td>${booking.userId}</td>
                                <td>${booking.bookingDate}</td>
                                <td>${booking.bookingTime}</td>
                                <td>${booking.status}</td>
                            </tr>
                        `;
                        tableBody.append(row);
                    });
                }
            },
            error: function (xhr) {
                console.error("Error loading bookings:", xhr.responseText);
            }
        });
    }

    // Search booking by ID and show in modal
    function searchBookingById(id) {
        $.ajax({
            url: "http://localhost:8080/api/v1/admin/findBooking/" + id,
            method: "GET",
            headers: headers,
            success: function (response) {
                if (response && response.data) {
                    const booking = response.data;

                    const modalContent = `
                        <p><strong>Booking ID:</strong> ${booking.id}</p>
                        <p><strong>Service ID:</strong> ${booking.serviceId}</p>
                        <p><strong>User ID:</strong> ${booking.userId}</p>
                        <p><strong>Booking Date:</strong> ${booking.bookingDate}</p>
                        <p><strong>Booking Time:</strong> ${booking.bookingTime}</p>
                        <p><strong>Status:</strong> ${booking.status}</p>
                    `;
                    $("#bookingDetails").html(modalContent);

                    // Show the modal
                    $("#bookingModal").css("display", "block");
                }
            },
            error: function (xhr) {
                console.error("Booking not found:", xhr.responseText);
                alert("Booking not found!");
            }
        });
    }

    // Handle search input Enter key
    $("#searchInput").keypress(function (e) {
        if (e.which === 13) { 
            const bookingId = $(this).val().trim();
            if (bookingId === "") {
                loadBookings();
            } else {
                searchBookingById(bookingId);
            }
        }
    });

    // Modal close button
    $(".close-btn").click(function () {
        $("#bookingModal").css("display", "none");
    });

    // Close modal if clicked outside content
    $(window).click(function (event) {
        if (event.target.id === "bookingModal") {
            $("#bookingModal").css("display", "none");
        }
    });
});