function signup(event) {
    fetch("/api/auth/signup", {
      method: "post",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        phone: document.getElementById("phone").value,
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