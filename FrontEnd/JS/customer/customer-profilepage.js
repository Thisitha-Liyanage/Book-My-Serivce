$(document).ready(function () {

    const user = JSON.parse(localStorage.getItem("user"));

    if (!user) {
        alert("No user data found!");
        window.location.href = "/FrontEnd/index.html";
        return;
    }

    // Set data to UI
    $("#customerName").text(user.name || "-");
    $("#customerEmail").text(user.email || "-");
    $("#customerPhone").text(user.phone || "-");
    $("#customerProvince").text(user.province || "-");
    $("#customerCity").text(user.city || "-");
    $("#customerVillage").text(user.village || "-");

});