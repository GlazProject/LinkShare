document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('addButton').addEventListener('click', saveLink);
    document.getElementById('deleteButton').addEventListener('click', deleteSelectedLinks);
    document.getElementById('logoutButton').addEventListener('click', () => logOut("edit"));

    fetchLinks();
    fetchUserName(document.getElementById('userName'))
});

function fetchLinks() {
    const linksList = document.getElementById('linksList');

    fetch('/api/edit/links')
        .then(response => response.json())
        .then(data => {
            linksList.innerHTML = '';
            data.forEach(link => linksList.appendChild(getLinksListItem(link)));
        })
        .catch(() => {
            linksList.innerHTML = '<p>Ошибка при получении ссылок</p>';
        });
}

function getLinksListItem(link){
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
    return listItem
}

function saveLink() {
    const title = document.getElementById('newTitle');
    const url = document.getElementById('newUrl');

    if (!title.value || !url.value) {
        alert('Необходимо заполнить имя ссылки и url.');
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
                alert('Не удалось успешно сохранить ссылку. Повторите попытку');
            }
        })
        .catch(() => {
            alert('Не удалось успешно сохранить ссылку. Повторите попытку');
        });
}

function deleteSelectedLinks() {
    const checkboxes = document.querySelectorAll('#linksList input[type="checkbox"]:checked');
    const linkTitles = Array.from(checkboxes).map(checkbox => checkbox.value);

    if (linkTitles.length === 0)
        return;

    fetch('/api/edit/links', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ titles: linkTitles }),
    })
        .then(response => {
            if (response.ok) {
                fetchLinks();
            } else {
                alert('Ошибка при удалении ссылок.');
            }
        });
}