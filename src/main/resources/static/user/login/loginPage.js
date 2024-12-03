document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('sendCodeButton').addEventListener('click', () => {
        const accessCode = document.getElementById('accessCode').value;
        const params = new URL(window.location.href).searchParams;
        login(accessCode, params.get("backlink"))
    });
});

function login(code, backlink){
    fetch(`/api/user/login?code=${code}&backlink=${encodeURIComponent(backlink)}`, {
        method: 'GET'
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                alert('Ошибка входа. Проверьте код доступа.');
            }
        })
        .catch(error => {
            console.error('Ошибка входа:', error);
        });
}