$(document).ready(function () {
    console.log("========================")
    const token = localStorage.getItem("token");
    console.log("===================" + token)
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

    $.ajax({
        url: "http://localhost:8080/api/v1/admin/foundAdmin",
        method: "GET",
        headers: headers,
        success: function(response) {
            console.log("Admin data:", response);

            if (response && response.data) {
                const userDto = response.data;

                localStorage.setItem("user", JSON.stringify(userDto));

                console.log("Saved UserDto to localStorage:", userDto);
            } else {
                console.warn("No user data found in response");
            }
        },
        error: function(xhr) {
            console.error("Error:", xhr.responseText);
        }
    });

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


    // Get Customer Count
    $.ajax({
        url: "http://localhost:8080/api/v1/admin/getCustomerCount",
        method: "GET",
        headers: headers,
        success: function(response) {
            console.log("Customer Count:", response);

            if (response && response.data !== undefined) {
                $("#customer-count").text(response.data);
            }
        },
        error: function(xhr) {
            console.error("Customer count error:", xhr.responseText);
        }
    });


    // Get Provider Count
    $.ajax({
        url: "http://localhost:8080/api/v1/admin/getProviderCount",
        method: "GET",
        headers: headers,
        success: function(response) {
            console.log("Provider Count:", response);

            if (response && response.data !== undefined) {
                $("#provider-count").text(response.data);
            }
        },
        error: function(xhr) {
            console.error("Provider count error:", xhr.responseText);
        }
    });

});



});