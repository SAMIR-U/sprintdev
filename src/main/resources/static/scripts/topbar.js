const toggle = document.getElementById('userMenuToggle');
const dropdown = document.getElementById('userDropdown');

if (toggle && dropdown) {
    toggle.addEventListener('click', function (e) {
        e.stopPropagation();
        dropdown.classList.toggle('show');
    });

    document.addEventListener('click', function (e) {
        if (!toggle.contains(e.target)) {
            dropdown.classList.remove('show');
        }
    });
}