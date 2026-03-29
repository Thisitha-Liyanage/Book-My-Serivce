$(document).ready(function () {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Unauthorized! Please login.");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    loadCustomer();

    function loadCustomer() {
        $.ajax({
            url: "http://localhost:8080/api/v1/customer/foundCustomer", 
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            success: function (response) {
                console.log("Customer Data:", response);
                
            localStorage.setItem("user", JSON.stringify(response.data));

            },
            error: function (xhr) {
                console.error("Error fetching customer:", xhr.responseText);

                if (xhr.status === 403 || xhr.status === 401) {
                    alert("Session expired. Please login again.");
                    localStorage.removeItem("token");
                    window.location.href = "/FrontEnd/index.html";
                }
            }
        });
    }

});