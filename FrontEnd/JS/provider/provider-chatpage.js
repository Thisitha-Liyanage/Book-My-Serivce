const token = localStorage.getItem("token");
console.log("Token:", token);

$(document).ready(function () {

    if (!token) {
        alert("Please login first!");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    const chatBox = $("#chatBox");
    const customerListContainer = $(".chat-user-list");

    let chatsData = []; // store all chats
    let selectedCustomer = null; // currently selected customer
    let providerId = null; // store logged-in providerId

    // -------------------- Load all chats --------------------
    function loadChats() {
        $.ajax({
            url: "http://localhost:8080/api/v1/provider/getMassages",
            method: "GET",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                chatsData = response.data;
                console.log("Chats Data:", response);

                customerListContainer.empty();
                chatBox.empty();

                if (!chatsData || chatsData.length === 0) {
                    customerListContainer.append("<p>No chats yet.</p>");
                    return;
                }

                // Get providerId from first chat (all chats belong to this provider)
                providerId = chatsData[0].providerId;

                // Get unique customers
                const uniqueCustomers = {};
                chatsData.forEach(chat => {
                    if (!uniqueCustomers[chat.customerName]) {
                        uniqueCustomers[chat.customerName] = chat;
                    }
                });

                // Display customer list
                $.each(uniqueCustomers, function (customerName, chat) {
                    const chatUserDiv = $(`
                        <div class="chat-user" data-customer="${customerName}">
                            <h4>${chat.customerName}</h4>
                            <p>${chat.message}</p>
                        </div>
                    `);
                    customerListContainer.append(chatUserDiv);
                });

                // Select first customer by default or refresh current selection
                if (!selectedCustomer) {
                    $(".chat-user").first().click();
                } else {
                    $(`.chat-user[data-customer="${selectedCustomer}"]`).click();
                }
            },
            error: function (err) {
                console.error("Failed to load chats:", err);
            }
        });
    }

    // -------------------- Click on customer --------------------
    $(document).on("click", ".chat-user", function () {
        const customerName = $(this).data("customer");
        selectedCustomer = customerName;

        $(".chat-user").removeClass("active");
        $(this).addClass("active");

        const customerChats = chatsData.filter(c => c.customerName === customerName);
        if (customerChats.length === 0) {
            chatBox.empty();
            return;
        }

        // Show customer details
        const firstChat = customerChats[0];
        $("#chatCustomerName").text(firstChat.customerName);
        $("#chatService").text("Service: " + firstChat.serviceName);
        $("#chatPhone").text("Phone: " + firstChat.phoneNumber);
        $("#chatCity").text("City: " + firstChat.city);
        $("#chatDate").text("Booking Date: " + firstChat.bookingDate);

        // Load messages with correct sent/received classes
        chatBox.empty();
        customerChats.forEach(chat => {
            // provider messages -> sent, customer messages -> received
            let msgClass = chat.senderRole === "CUSTOMER" && chat.reciverRole === "PROVIDER" ? "received" : "sent";
            console.log(msgClass);
            const msgDiv = $(`
        <div class="message ${msgClass}">
            <p>${chat.message}</p>
            <span>${new Date().toLocaleTimeString()}</span>
        </div>
    `);
            chatBox.append(msgDiv);
        });

        chatBox.scrollTop(chatBox[0].scrollHeight);
    });

    // -------------------- Send Message --------------------
    $("#sendBtn").click(function () {
        const msg = $("#messageInput").val().trim();
        if (!msg || !selectedCustomer) return;

        const chatForCustomer = chatsData.find(c => c.customerName === selectedCustomer);
        if (!chatForCustomer) return;

        const data = {
            massage: msg,
            receiverId: chatForCustomer.customerId,
            bookingId: chatForCustomer.bookingId
        };

        $.ajax({
            url: "http://localhost:8080/api/v1/provider/sendMassage",
            method: "POST",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: JSON.stringify(data),
            success: function () {
                $("#messageInput").val("");
                loadChats(); // reload messages
            },
            error: function (err) {
                console.error("Failed to send message:", err);
            }
        });
    });

    // -------------------- Initial load --------------------
    loadChats();

    // -------------------- Auto-refresh messages --------------------
    setInterval(() => {
        if (selectedCustomer) {
            loadChats();
        }
    }, 5000);

});