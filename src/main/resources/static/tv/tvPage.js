document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('refreshButton').addEventListener('click', fetchLinks);
    document.getElementById('logoutButton').addEventListener('click', () => logOut());
    document.getElementById('getCodeButton').addEventListener('click', fetchCode);

    fetchLinks();
    fetchUserName(document.getElementById('userName'));
});

function fetchLinks() {
    const linksList = document.getElementById('linksList');
    fetch('/api/tv/links')
        .then(response => response.json())
        .then(data => {
            if (data.length === 1) {
                window.location.href = data[0].url;
                return;
            }
            linksList.innerHTML = '';
            data.forEach(link => linksList.appendChild(getLinksListItem(link)));
        })
        .catch(() => {
            linksList.innerHTML = '<li>Ошибка при получении ссылок</li>';
        });
}

function fetchCode() {
    fetch('/api/user/code')
        .then(response => response.json())
        .then(data => {
            alert(`Код для текущего пользователя: ${data.code}`)
        })
        .catch(() => {
            alert('Произошла ошибка при получении кода')
        });
}

function getLinksListItem(link) {
    const listItem = document.createElement('li');
    const linkElement = document.createElement('a');

    linkElement.href = link.url;
    linkElement.textContent = link.title;
    listItem.appendChild(linkElement);

    listItem.classList.add("link-item");
    listItem.addEventListener('click', event => {
        event.preventDefault();
        window.location.href = link.url;
    });

    return listItem;
}