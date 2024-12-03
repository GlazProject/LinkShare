document.addEventListener('DOMContentLoaded', () => {
    const codeElement = document.getElementById('codeDisplay');
    fetchCode(codeElement);

    document.getElementById('applyButton').addEventListener('click', () => window.location.href = '/tv');
    document.getElementById('refreshButton').addEventListener('click', () => fetchCode(codeElement));
});

function fetchCode(codeElement) {
    fetch('/api/user/code/new')
        .then(response => response.json())
        .then(data => {
            codeElement.textContent = data.code;
        })
        .catch(_ => {
            codeElement.textContent = 'Ошибка при получении кода';
        });
}