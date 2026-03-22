$(document).ready(function () {

    const token = localStorage.getItem("token");
    const modal = $("#customerModal"); // ✅ MATCH HTML
    const closeBtn = $(".close-btn");
    let currentBooking = null;

    if (!token) {
        alert("Unauthorized!");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    loadBookings();

    // ================= LOAD BOOKINGS =================
    function loadBookings() {
        $.ajax({
            url: "http://localhost:8080/api/v1/provider/getAllBookings",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (res) {
                console.log(res);
                if (res.code === 200 && Array.isArray(res.data)) {
                    renderTable(res.data);
                } else {
                    showNoResults();
                }
            },
            error: function (err) {
                console.error(err);
                showNoResults();
            }
        });
    }

    // ================= TABLE =================
    function renderTable(bookings) {
        const table = $("#bookingTable");
        table.empty();

        if (bookings.length === 0) {
            return table.append(`<tr><td colspan="6">No bookings</td></tr>`);
        }

        bookings.forEach(b => {
            const row = $(`
                <tr data-id="${b.id}">
                    <td>${b.id}</td>
                    <td>${b.userId}</td>
                    <td>${b.serviceId}</td>
                    <td>${b.bookingDate}</td>
                    <td>${formatTime(b.bookingTime)}</td>
                    <td>${b.status}</td>
                </tr>
            `);

            row.on("click", function () {
                getBookingDetails($(this).data("id"));
            });

            table.append(row);
        });
    }

    // ================= GET ONE =================
    function getBookingDetails(id) {
        $.ajax({
            url: `http://localhost:8080/api/v1/provider/getBookingById/${id}`,
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (res) {
                console.log(res);
                if (res.code === 200 && res.data) {
                    currentBooking = res.data;
                    openBookingModal(res.data);
                } else {
                    alert("Booking not found");
                }
            },
            error: function () {
                alert("Error fetching booking");
            }
        });
    }

    // ================= OPEN MODAL =================
    function openBookingModal(b) {
        const html = `
            <p><strong>ID:</strong> ${b.id}</p>
            <p><strong>Service ID:</strong> ${b.serviceId}</p>
            <p><strong>Customer ID:</strong> ${b.userId}</p>
            <p><strong>Date:</strong> ${b.bookingDate}</p>
            <p><strong>Time:</strong> ${formatTime(b.bookingTime)}</p>
            <p><strong>Status:</strong> ${b.status}</p>
        `;

        $("#customerDetails").html(html);
        modal.addClass("show");
    }

    // ================= CLOSE MODAL =================
    closeBtn.on("click", function () {
        modal.removeClass("show");
    });

    $(window).on("click", function (e) {
        if ($(e.target).is(modal)) {
            modal.removeClass("show");
        }
    });

    // ================= FORMAT TIME =================
    function formatTime(time) {
        if (!time) return "";
        const [h, m] = time.split(":");
        let hour = parseInt(h);
        const ampm = hour >= 12 ? "PM" : "AM";
        hour = hour % 12 || 12;
        return `${hour}:${m} ${ampm}`;
    }

    // ================= SEARCH =================
    $("#searchInput").on("keypress", function (e) {
        if (e.which === 13) {
            const id = $(this).val().trim();
            if (!id) return loadBookings();
            getBookingDetails(id);
        }
    });

    function showNoResults() {
        $("#bookingTable").html(`<tr><td colspan="6">No results</td></tr>`);
    }

    // ================= UPDATE STATUS =================
    $("#updateStatusBtn").on("click", function () {
        const newStatus = $("#bookingStatus").val();

        if (!newStatus) {
            alert("Select a status");
            return;
        }

        if (!currentBooking) {
            alert("No booking selected");
            return;
        }

        $.ajax({
            url: `http://localhost:8080/api/v1/provider/updateStatus/${currentBooking.id}`,
            method: "PUT",
            headers: { "Authorization": "Bearer " + token },
            contentType: "application/json",
            data: JSON.stringify({ status: newStatus }),
            success: function (res) {
                alert(res.message || "Booking status updated!");
                modal.removeClass("show");
                loadBookings();
            },
            error: function (err) {
                console.error(err);
                alert(err.responseJSON?.message || "Failed to update status");
            }
        });
    });

});