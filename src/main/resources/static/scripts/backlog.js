function openCreateModal() {
    document.getElementById("createModal").style.display = "flex";
}

function closeCreateModal() {
    document.getElementById("createModal").style.display = "none";
}

function openEditModal(taskId, title, description) {
    document.getElementById("editTaskId").value = taskId;
    document.getElementById("editTitle").value = title;
    document.getElementById("editDescription").value = description;

    document.getElementById("editModal").style.display = "flex";
}

function closeEditModal() {
    document.getElementById("editModal").style.display = "none";
}

function openDeleteModal(taskId) {
    document.getElementById("deleteTaskId").value = taskId;
    document.getElementById("deleteModal").style.display = "flex";
}

function closeDeleteModal() {
    document.getElementById("deleteModal").style.display = "none";
}

window.onclick = function(event) {

    const createModal = document.getElementById("createModal");
    const editModal = document.getElementById("editModal");
    const deleteModal = document.getElementById("deleteModal");

    if (event.target === createModal) {
        closeCreateModal();
    }

    if (event.target === editModal) {
        closeEditModal();
    }

    if (event.target === deleteModal) {
        closeDeleteModal();
    }
};