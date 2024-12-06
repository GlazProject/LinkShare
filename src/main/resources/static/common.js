function fetchUserName(userNameElement){
    fetch('/api/user/id')
        .then(response => response.text())
        .then(data => {
            userNameElement.textContent = data;
        })
        .catch(() => {
            userNameElement.textContent = '...';
        });
}

function logout(from){
    const query = from === undefined ? "" : `?backlink=${from}`;
    fetch(`/api/user/logout${query}`)
        .then(response => {
            if (response.redirected)
                window.location.href = response.url;
        })
        .catch(() => {
            alert('Ошибка при выходе из системы.');
        });
}