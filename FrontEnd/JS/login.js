document.getElementById("login-form")
.addEventListener("submit", function (e) {
    e.preventDefault();

    let email = document.getElementById("email");
    let password = document.getElementById("password");
    let errorDiv = document.getElementById("error-popup");

    let isValid = true;

    [email, password].forEach(input => {
        if (input.value.trim() === "") {
            input.style.border = "2px solid red";
            isValid = false;
        } else {
            input.style.border = "";
        }
    });

    if (!isValid) return;

    let login = {
        email: email.value.trim(),
        password: password.value.trim()
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(login),
        success: function (response) {

            console.log("Full Response:", response);

            if (response.code === 200) {

                let token = response.data.access_token;
                let role = response.data.role;
                console.log(token + "============")

                localStorage.setItem("token", token);
                alert("Login Success!");

                console.log("ROLE:", role);

                if (role === "CUSTOMER") {
                    window.location.href = "/FrontEnd/pages/customer/customer-home-page.html";
                } 
                else if (role === "PROVIDER") {
                    window.location.href = "/FrontEnd/pages/provider/provider-homepage.html";
                } 
                else if (role === "ADMIN") {
                    window.location.href = "/FrontEnd/pages/admin/admin-homepage.html";
                }
            }
        },

        error: function (xhr) {

            console.log("Error Response:", xhr.responseJSON);

            if (xhr.responseJSON) {

                let response = xhr.responseJSON;
                if (typeof response.data === "object") {

                    let messages = Object.values(response.data).join(", ");
                    errorDiv.innerText = messages;

                } 
                else if (response.data) {

                    errorDiv.innerText = response.data;

                } 
                else {

                    errorDiv.innerText = response.message;
                }

            } else {
                errorDiv.innerText = "Something went wrong. Please try again.";
            }

            errorDiv.style.display = "block";

            setTimeout(() => {
                errorDiv.style.display = "none";
            }, 3000);
        }
    });
});