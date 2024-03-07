function login(event) {
    fetch("/api/auth/login", {
      method: "post",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
      })
    })
    .then((response) => {
        if (response.ok) {
            location.href = "/";
        } else {
            response.json().then(json => {console.log(json);alert(`Error: ` + json.description)});
        }
    });
    event.preventDefault();
}