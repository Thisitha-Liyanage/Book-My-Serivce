const admin = JSON.parse(localStorage.getItem("user"));

document.getElementById("adminName").textContent = admin.name;
document.getElementById("adminEmail").textContent = admin.email;
document.getElementById("adminPhone").textContent = admin.phone;
document.getElementById("adminProvince").textContent = admin.province;
document.getElementById("adminCity").textContent = admin.city;
document.getElementById("adminVillage").textContent = admin.village;
document.getElementById("adminRole").textContent = admin.role;
