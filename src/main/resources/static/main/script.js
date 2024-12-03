document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('view-btn').addEventListener('click', function() {
        window.location.href = '/tv';
    });

    document.getElementById('edit-btn').addEventListener('click', function() {
        window.location.href = '/edit';
    });
});