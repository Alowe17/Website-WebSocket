document.getElementById("form-register").addEventListener("submit", async (e) => {
    e.preventDefault();

    const response = await fetch("/website-websocket/api/register", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        })
    });

    if (response.ok) {
        const container = document.getElementById('container-successfully');
        container.innerHTML = "";
        container.textContent = "Аккаунт успешно зарегистрирован!";
        container.style.color = "green";
    } else {
        const error = await response.json();
        const container = document.getElementById('container-error');
        container.innerHTML = "";
        container.textContent = error.message || JSON.stringify(error);
        container.style.color = "red";
    }
});
