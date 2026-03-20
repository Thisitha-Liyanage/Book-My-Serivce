$(document).ready(function () {

    const token = localStorage.getItem("token");

    if (!token) {
        console.error("No token found!");
        return;
    }

    loadProviderProfile(token);
});

function loadProviderProfile(token) {

    $.ajax({
        url: "http://localhost:8080/api/v1/provider/findProvider",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },

        success: function (res) {
            console.log("Provider Data:", res);

            const p = res.data;

            $("#providerName").text(p.name);
            $("#providerEmail").text(p.email);
            $("#providerPhone").text(p.phone);
            $("#providerProvince").text(p.province);
            $("#providerCity").text(p.city);
            $("#providerVillage").text(p.village);
            $("#providerRole").text(p.role);

            // ✅ Sync sidebar user
            localStorage.setItem("user", JSON.stringify(p));
        },

        error: function (err) {
            console.error("Error loading provider:", err);
        }
    });
}