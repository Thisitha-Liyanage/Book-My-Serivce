$(document).ready(function () {

    const token = localStorage.getItem("token");
    const customer = JSON.parse(localStorage.getItem("user"));
    const bookingId = localStorage.getItem("bookingId");
    const providerId = localStorage.getItem("chatProviderId");

    console.log(bookingId);
    console.log(customer);
    console.log(providerId);

    const $chatBox = $("#chatBox");
    const $messageInput = $("#messageInput");

    // -------------------- Send Message --------------------
    function sendMessage() {
        const msg = $messageInput.val().trim();
        if (!msg) return;

        const data = {
            massage: msg,
            senderId: customer.id,
            receiverId: providerId,
            bookingId: parseInt(bookingId)
        };
        console.log(data)
        $.ajax({
            url: "http://localhost:8080/api/v1/customer/sendMassage",
            method: "POST",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: JSON.stringify(data),
            success: function () {
                $messageInput.val("").focus();
                loadFullChat(); // reload messages
            },
            error: function (err) {
                console.error(err);
            }
        });
    }

    // -------------------- Load Full Chat --------------------
    function loadFullChat() {
        $.ajax({
            url: `http://localhost:8080/api/v1/customer/getFullChat/${bookingId}`,
            method: "GET",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                const chats = response.data;
                $chatBox.empty();
                $.each(chats, function (index, chat) {
                    console.log(chat)
                    const isSent = chat.senderId === customer.id;
                    const msgClass = isSent ? "message sent" : "message received";

                    const $msgDiv = $(`
            <div class="${msgClass}">
                ${!isSent ? `<strong>${chat.senderName}</strong><br>` : ''}
                <p>${chat.massage}</p>
                <span>${new Date().toLocaleTimeString()}</span>
            </div>
        `);

                    $chatBox.append($msgDiv);
                });
                $chatBox.scrollTop($chatBox[0].scrollHeight);
            },
            error: function (err) {
                console.error(err);
            }
        });
    }

    // -------------------- Auto-refresh --------------------
    setInterval(function () {
        loadFullChat(); // reload full chat every 3 seconds
    }, 3000);

    // -------------------- Bind Enter Key --------------------
    $messageInput.keypress(function (e) {
        if (e.which === 13) { // Enter key
            sendMessage();
        }
    });

    // -------------------- Initial load --------------------
    loadFullChat();

    // Optional: Bind send button
    $("#sendBtn").click(function () {
        sendMessage();
    });

});