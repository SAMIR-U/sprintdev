document.addEventListener("DOMContentLoaded", () => {
    const dashboard = document.getElementById("dashboard");
    if (!dashboard) return;

    const contextPath = dashboard.dataset.contextPath;
    const sprintId = dashboard.dataset.sprintId;
    let version = Number(dashboard.dataset.version || 0);
    let draggingTask = null;
    let requestInProgress = false;

    const board = document.getElementById("board");
    const template = document.getElementById("task-template");

    function showMessage(message, type = "error") {
        const container = document.getElementById("dashboardMessage");
        container.replaceChildren();
        const banner = document.createElement("div");
        banner.className = `banner ${type}`;
        banner.textContent = message;
        container.appendChild(banner);
        setTimeout(() => banner.remove(), 4000);
    }

    function createTask(task) {
        const card = template.content.firstElementChild.cloneNode(true);
        card.dataset.taskId = task.id;
        card.dataset.status = task.status;
        card.querySelector("h3").textContent = task.title;
        const assignees = card.querySelector(".task-assignees");

        if (task.assignedUsers && task.assignedUsers.length) {
            task.assignedUsers.forEach(user => {
                const chip = document.createElement("span");
                chip.className = "task-assignee";
                chip.title = user.userName;
                const avatar = document.createElement("span");
                avatar.className = "assignee-avatar";
                avatar.textContent = user.userName.charAt(0).toUpperCase();
                const name = document.createElement("span");
                name.textContent = user.userName;
                chip.append(avatar, name);
                assignees.appendChild(chip);
            });
        } else {
            const empty = document.createElement("span");
            empty.className = "unassigned";
            empty.textContent = "Sin asignar";
            assignees.replaceWith(empty);
        }
        bindTask(card);
        return card;
    }

    function renderBoard(sprint) {
        version = sprint.version;
        dashboard.dataset.version = version;
        board.querySelectorAll(".drop-zone").forEach(zone => zone.replaceChildren());
        sprint.tasks.forEach(task => {
            const zone = board.querySelector(`.column[data-status="${task.status}"] [data-list]`);
            if (zone) zone.appendChild(createTask(task));
        });
        updateCounts();
    }

    function updateCounts() {
        board.querySelectorAll(".column").forEach(column => {
            column.querySelector(".task-count").textContent = column.querySelectorAll(".task").length;
        });
    }

    async function validateVersion(force = false) {
        if (requestInProgress || draggingTask) return;
        requestInProgress = true;
        try {
            const requestedVersion = force ? -1 : version;
            const response = await fetch(`${contextPath}/api/sprint/version?sprintId=${encodeURIComponent(sprintId)}&version=${encodeURIComponent(requestedVersion)}`, {credentials: "same-origin"});
            if (response.status === 204) return;
            if (response.ok) {
                renderBoard(await response.json());
                return;
            }
            if (response.status === 401) window.location.assign(`${contextPath}/login`);
        } catch (_) {
            // El siguiente ciclo de validación volverá a intentarlo.
        } finally {
            requestInProgress = false;
        }
    }

    async function changeStatus(task, targetStatus, sourceZone) {
        const previousStatus = task.dataset.status;
        const targetZone = board.querySelector(`.column[data-status="${targetStatus}"] [data-list]`);
        if (!targetZone || previousStatus === targetStatus) return;

        targetZone.appendChild(task);
        updateCounts();
        requestInProgress = true;
        let refreshAfterUpdate = false;
        try {
            const body = new URLSearchParams({taskId: task.dataset.taskId, taskStatus: targetStatus});
            const response = await fetch(`${contextPath}/api/task/editstatus`, {
                method: "POST", credentials: "same-origin",
                headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"}, body
            });
            if (!response.ok) {
                const message = (await response.text()).trim() || "No fue posible actualizar el estado de la tarea.";
                sourceZone.appendChild(task);
                updateCounts();
                showMessage(message);
                if (response.status === 401) window.location.assign(`${contextPath}/login`);
                return;
            }
            task.dataset.status = targetStatus;
            refreshAfterUpdate = true;
        } catch (_) {
            sourceZone.appendChild(task);
            updateCounts();
            showMessage("No fue posible actualizar el estado de la tarea.");
        } finally {
            requestInProgress = false;
        }
        if (refreshAfterUpdate) await validateVersion();
    }

    function bindTask(task) {
        task.addEventListener("dragstart", event => {
            draggingTask = task;
            event.dataTransfer.effectAllowed = "move";
            event.dataTransfer.setData("text/plain", task.dataset.taskId);
            task.classList.add("dragging");
        });
        task.addEventListener("dragend", () => {
            task.classList.remove("dragging");
            board.querySelectorAll(".drop-zone").forEach(zone => zone.classList.remove("drag-over"));
            draggingTask = null;
        });
    }

    board.querySelectorAll(".task").forEach(bindTask);
    board.querySelectorAll(".drop-zone").forEach(zone => {
        zone.addEventListener("dragover", event => {
            event.preventDefault();
            if (draggingTask) zone.classList.add("drag-over");
        });
        zone.addEventListener("dragleave", () => zone.classList.remove("drag-over"));
        zone.addEventListener("drop", event => {
            event.preventDefault();
            zone.classList.remove("drag-over");
            if (draggingTask) changeStatus(draggingTask, zone.closest(".column").dataset.status, draggingTask.parentElement);
        });
    });
    updateCounts();
    validateVersion(true);
    window.setInterval(validateVersion, 5000);
});
