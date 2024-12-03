document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('addButton').addEventListener('click', addLink);
    document.getElementById('deleteButton').addEventListener('click', deleteSelectedLinks);
    document.getElementById('logoutButton').addEventListener('click', logout);

    fetchLinks();
});

function fetchLinks() {
    const linksList = document.getElementById('linksList');

    fetch('/api/edit/links')
        .then(response => response.json())
        .then(data => {
            linksList.innerHTML = '';
            data.forEach(link => {
                const listItem = document.createElement('li');
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.value = link.title;

                const linkElement = document.createElement('a');
                linkElement.href = link.url;
                linkElement.textContent = link.title;
                linkElement.target = '_blank';

                listItem.appendChild(checkbox);
                listItem.appendChild(linkElement);
                linksList.appendChild(listItem);
            });
        })
        .catch(() => {
            linksList.innerHTML = '<li>Ошибка при получении ссылок</li>';
        });
}

function addLink() {
    const title = document.getElementById('newTitle');
    const url = document.getElementById('newUrl');

    if (!title.value || !url.value) {
        alert('Пожалуйста, заполните все поля.');
        return;
    }

    fetch('/api/edit/links', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title: title.value, url: url.value }),
    })
        .then(response => {
            if (response.ok) {
                fetchLinks();
                title.value = '';
                url.value = '';
            } else {
                alert('Ошибка при добавлении ссылки.');
            }
        })
        .catch(() => {
            alert('Произошла ошибка при добавлении ссылки.');
        });
}

function deleteSelectedLinks() {
    const checkboxes = document.querySelectorAll('#linksList input[type="checkbox"]:checked');
    const linkIds = Array.from(checkboxes).map(checkbox => checkbox.value);

    if (linkIds.length === 0)
        return;

    fetch('/api/edit/links', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ titles: linkIds }),
    })
        .then(response => {
            if (response.ok) {
                fetchLinks();
            } else {
                alert('Ошибка при удалении ссылок.');
            }
        });
}

function logout() {
    fetch(`/api/user/logout?backlink=${encodeURIComponent(window.location.pathname)}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.redirected) {
                window.location.pathname = response.url;
            } else {
                alert('Ошибка при выходе из системы.');
            }
        })
        .catch(() => {
            alert('Произошла ошибка при выходе из системы.');
        });
}