const addTaskMenu = document.getElementById("addTaskMenu");
const addReaderMenu = document.getElementById("addReaderMenu");

function openAddTaskForm() {
    addTaskMenu.style.display = "flex";
}

function closeAddTaskForm() {
    addTaskMenu.style.display = "none";
}

function openAddReaderForm() {
    addReaderMenu.style.display = "flex";
}

function closeAddReaderForm() {
    addReaderMenu.style.display = "none";
}

window.onclick = function (event) {
    if (event.target === addTaskMenu) {
        closeAddTaskForm();
    }
    if (event.target === addReaderMenu) {
        closeAddReaderForm();
    }
};