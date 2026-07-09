const addSprint = document.getElementById("addSprintMenu");

function openAddSprintForm() {
    addSprint.style.display = "flex";
}

function closeAddSprintForm() {
    addSprint.style.display = "none";
}

// Cierra el modal si haces click en el fondo oscuro (fuera del contenido)
window.onclick = function (event) {
    if (event.target === addSprint) {
        closeAddSprintForm();
    }
};