/* ==========================================================
   Modal: Nueva tarea
   ========================================================== */

function openCreateModal() {
    document.getElementById("createModal").style.display = "flex";
}

function closeCreateModal() {
    document.getElementById("createModal").style.display = "none";
}

/* ==========================================================
   Validación: al menos un responsable en Nueva tarea
   ========================================================== */

(function initCreateTaskValidation() {

    const createForm = document.querySelector("#createModal form");

    if (!createForm) {
        return;
    }

    createForm.addEventListener("submit", function (event) {

        const checkedAssignees = createForm.querySelectorAll(
            'input[name="assignedUserNames"]:checked'
        );

        if (checkedAssignees.length === 0) {
            event.preventDefault();
            alert("Selecciona al menos un responsable para la tarea.");
        }
    });

})();

/* ==========================================================
   Modal: Editar tarea
   ========================================================== */

function openEditModal(taskId, title, description) {
    document.getElementById("editTaskId").value = taskId;
    document.getElementById("editTitle").value = title;
    document.getElementById("editDescription").value = description;

    document.getElementById("editModal").style.display = "flex";
}

function closeEditModal() {
    document.getElementById("editModal").style.display = "none";
}

/* ==========================================================
   Modal: Eliminar tarea
   ========================================================== */

function openDeleteModal(taskId) {
    document.getElementById("deleteTaskId").value = taskId;
    document.getElementById("deleteModal").style.display = "flex";
}

function closeDeleteModal() {
    document.getElementById("deleteModal").style.display = "none";
}

/* Cierra cualquier modal abierto al hacer clic fuera de su contenido */
window.onclick = function (event) {

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

/* ==========================================================
   Filtro de tareas: búsqueda por texto + estado
   ========================================================== */

(function initBacklogFilters() {

    const searchInput = document.getElementById("searchTask");
    const filterChips = document.querySelectorAll(".filter-chip");
    const tableBody = document.getElementById("taskTableBody");
    const noResults = document.getElementById("noResults");

    if (!tableBody) {
        return;
    }

    let currentStatus = "all";
    let currentSearch = "";

    function applyFilters() {

        const rows = tableBody.querySelectorAll(".task-row");
        let visibleCount = 0;

        rows.forEach(function (row) {

            const matchesStatus =
                currentStatus === "all" || row.dataset.status === currentStatus;

            const matchesSearch =
                currentSearch === "" ||
                row.dataset.search.indexOf(currentSearch) !== -1;

            const isVisible = matchesStatus && matchesSearch;

            row.style.display = isVisible ? "" : "none";

            if (isVisible) {
                visibleCount++;
            }
        });

        if (noResults) {
            noResults.style.display = visibleCount === 0 && rows.length > 0 ? "block" : "none";
        }
    }

    if (searchInput) {
        searchInput.addEventListener("input", function () {
            currentSearch = searchInput.value.trim().toLowerCase();
            applyFilters();
        });
    }

    filterChips.forEach(function (chip) {
        chip.addEventListener("click", function () {
            filterChips.forEach(function (c) {
                c.classList.remove("active");
            });
            chip.classList.add("active");
            currentStatus = chip.dataset.status;
            applyFilters();
        });
    });

})();