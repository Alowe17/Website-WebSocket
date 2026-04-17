document.getElementById("login-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const response = await fetch("/website-websocket/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: document.getElementById("login").value,
            password: document.getElementById("password").value
        })
    });

    if (response.ok) {
        window.location.href = "/website-websocket/index";
    } else {
        const error = await response.json();
        const container = document.getElementById('container-error');
        container.innerHTML = "";
        container.textContent = error;
    }
});