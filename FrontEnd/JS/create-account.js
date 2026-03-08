document.getElementById("signup-form")
.addEventListener("submit", function(e){
    e.preventDefault();
    createAccount();
});

function createAccount(){

    let username = document.getElementById("ca-username");
    let password = document.getElementById("ca-password");
    let confirmPassword = document.getElementById("ca-confirmPass");
    let email = document.getElementById("ca-email");
    let phone = document.getElementById("ca-phone");
    let village = document.getElementById("ca-village");
    let province = document.getElementById("ca-province");
    let district = document.getElementById("ca-district");

    let role = document.querySelector('input[name="role"]:checked');

    let errorDiv = document.getElementById("error-popup");

    let isValid = true;

    let inputs = [
        username, password, confirmPassword,
        email, phone, village, province, district
    ];

    inputs.forEach(input => {
        if (input.value.trim() === "") {
            input.style.border = "2px solid red";
            isValid = false;
        } else {
            input.style.border = "";
        }
    });

    if (password.value !== confirmPassword.value) {
        password.style.border = "2px solid red";
        confirmPassword.style.border = "2px solid red";
        errorDiv.innerText = "Passwords do not match!";
        errorDiv.style.display = "block";
        isValid = false;
    }

    if (!isValid) return;

    let user = {
        name: username.value.trim(),
        password: password.value.trim(),
        email: email.value.trim(),
        phone: phone.value.trim(),
        village: village.value.trim(),
        province: province.value.trim(),
        city: district.value.trim(),
        role: role.value
    };

    $.ajax({
    url: "http://localhost:8080/api/v1/auth/signup",
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(user),

    success: function (response) {

        console.log("Register Success:", response);

        errorDiv.innerText = "Account Created Successfully! Please Sign In.";
        errorDiv.style.color = "green";
        errorDiv.style.display = "block";

        
        document.getElementById("signup-form").reset();

        showLogin();  

        setTimeout(() => {
            errorDiv.style.display = "none";
            errorDiv.style.color = "red";
        }, 3000);
    },

    error: function (xhr) {

        console.log("Register Error:", xhr.responseJSON);

        if (xhr.responseJSON) {

            let response = xhr.responseJSON;

            if (typeof response.data === "object") {
                let messages = Object.values(response.data).join(", ");
                errorDiv.innerText = messages;
            } else if (response.data) {
                errorDiv.innerText = response.data;
            } else {
                errorDiv.innerText = response.message;
            }

        } else {
            errorDiv.innerText = "Registration failed. Try again.";
        }

        errorDiv.style.color = "red";
        errorDiv.style.display = "block";

        setTimeout(() => {
            errorDiv.style.display = "none";
        }, 3000);
    }
});

}