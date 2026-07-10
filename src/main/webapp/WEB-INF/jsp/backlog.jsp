<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.Task"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.User"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.TaskStatus"%>

<%
    List<Task> tasks = (List<Task>) session.getAttribute("tasks");
    List<User> readers = (List<User>) session.getAttribute("readers");

    String createMessage = (String) session.getAttribute("createtask");
    String editMessage = (String) session.getAttribute("edittask");
    String deleteMessage = (String) session.getAttribute("deletetask");
    Sprint sprint = (Sprint) session.getAttribute("sprint");

    session.removeAttribute("createtask");
    session.removeAttribute("edittask");
    session.removeAttribute("deletetask");

    int sprintId = Integer.parseInt(request.getParameter("sprintId"));

    int totalTasks = tasks != null ? tasks.size() : 0;
    int pendingCount = 0;
    int progressCount = 0;
    int completedCount = 0;

    if (tasks != null) {
        for (Task t : tasks) {
            TaskStatus st = t.getStatus();
            if (st == TaskStatus.COMPLETED) {
                completedCount++;
            } else if (st == TaskStatus.IN_PROGRESS || st == TaskStatus.IN_REVIEW) {
                progressCount++;
            } else {
                pendingCount++;
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Backlog | SprintDev</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/topbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/backlog.css">
</head>
<body>

<jsp:include page="topbar.jsp"/>

<main class="backlog-page">

    <nav class="breadcrumb">
        <a href="${pageContext.request.contextPath}/workspace">Mis sprints</a>
        <span class="crumb-separator">/</span>
        <a href="${pageContext.request.contextPath}/workspace/sprint?sprintId=<%= sprintId %>">
            <%= sprint != null ? sprint.getName() : "Sprint" %>
        </a>
        <span class="crumb-separator">/</span>
        <span class="crumb-current">Backlog</span>
    </nav>

    <div class="page-header">
        <div>
            <p class="page-eyebrow">SprintDev</p>
            <h1 class="page-title">Sprint Backlog</h1>
            <p class="page-description">
                Planifica, edita y da seguimiento a las tareas de
                <strong><%= sprint != null ? sprint.getName() : "este Sprint" %></strong>.
            </p>
        </div>

        <button class="btn-new-task" onclick="openCreateModal()">
            <span class="btn-new-task-icon">+</span>
            Nueva tarea
        </button>
    </div>

    <% if ("success".equals(createMessage)) { %>
        <div class="banner success">La tarea fue creada correctamente.</div>
    <% } else if (createMessage != null) { %>
        <div class="banner error">
            <%= "fail".equals(createMessage) ? "No fue posible crear la tarea." : createMessage %>
        </div>
    <% } %>
    <% if ("success".equals(editMessage)) { %>
        <div class="banner success">La tarea fue actualizada correctamente.</div>
    <% } else if (editMessage != null) { %>
        <div class="banner error">
            <%= "fail".equals(editMessage) ? "No fue posible actualizar la tarea." : editMessage %>
        </div>
    <% } %>
    <% if ("success".equals(deleteMessage)) { %>
        <div class="banner success">La tarea fue eliminada correctamente.</div>
    <% } else if (deleteMessage != null) { %>
        <div class="banner error">
            <%= "fail".equals(deleteMessage) ? "No fue posible eliminar la tarea." : deleteMessage %>
        </div>
    <% } %>

    <section class="backlog-stats">
        <div class="stat-card">
            <span class="stat-label">Total de tareas</span>
            <span class="stat-value"><%= totalTasks %></span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Pendientes</span>
            <span class="stat-value stat-pending"><%= pendingCount %></span>
        </div>
        <div class="stat-card">
            <span class="stat-label">En progreso</span>
            <span class="stat-value stat-progress"><%= progressCount %></span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Completadas</span>
            <span class="stat-value stat-completed"><%= completedCount %></span>
        </div>
    </section>

    <section class="backlog-panel">

        <div class="toolbar">
            <div class="search-field">
                <span class="search-icon">&#128269;</span>
                <input type="text" id="searchTask" placeholder="Buscar por título, descripción o responsable...">
            </div>

            <div class="status-filter">
                <button type="button" class="filter-chip active" data-status="all">Todas</button>
                <button type="button" class="filter-chip" data-status="PENDING">Pendiente</button>
                <button type="button" class="filter-chip" data-status="IN_PROGRESS">En progreso</button>
                <button type="button" class="filter-chip" data-status="IN_REVIEW">En revisión</button>
                <button type="button" class="filter-chip" data-status="COMPLETED">Completada</button>
            </div>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th class="col-id">ID</th>
                        <th class="col-task">Tarea</th>
                        <th class="col-status">Estado</th>
                        <th class="col-assignees">Responsables</th>
                        <th class="col-actions">Acciones</th>
                    </tr>
                </thead>
                <tbody id="taskTableBody">
                <%
                    if (tasks != null && !tasks.isEmpty()) {
                        for (Task task : tasks) {

                            TaskStatus status = task.getStatus();
                            String statusClass = "pending";
                            String statusLabel = "Pendiente";
                            String statusValue = "PENDING";

                            if (status == TaskStatus.IN_PROGRESS) {
                                statusClass = "progress";
                                statusLabel = "En progreso";
                                statusValue = "IN_PROGRESS";
                            } else if (status == TaskStatus.IN_REVIEW) {
                                statusClass = "review";
                                statusLabel = "En revisión";
                                statusValue = "IN_REVIEW";
                            } else if (status == TaskStatus.COMPLETED) {
                                statusClass = "completed";
                                statusLabel = "Completada";
                                statusValue = "COMPLETED";
                            }

                            StringBuilder searchBlob = new StringBuilder();
                            searchBlob.append(task.getTitle()).append(" ").append(task.getDescription());
                            if (task.getAssignedUsers() != null) {
                                for (User u : task.getAssignedUsers()) {
                                    searchBlob.append(" ").append(u.getUserName());
                                }
                            }
                %>

                <tr class="task-row" data-status="<%= statusValue %>" data-search="<%= searchBlob.toString().toLowerCase().replace("\"", "") %>">

                    <td class="col-id"><span class="task-id">#<%= task.getId() %></span></td>

                    <td class="col-task">
                        <div class="task-title"><%= task.getTitle() %></div>
                        <div class="task-description"><%= task.getDescription() %></div>
                    </td>

                    <td class="col-status">
                        <span class="status <%= statusClass %>"><%= statusLabel %></span>
                    </td>

                    <td class="col-assignees">
                        <% if (task.getAssignedUsers() != null && !task.getAssignedUsers().isEmpty()) { %>
                            <div class="assignee-list">
                                <% for (User assigned : task.getAssignedUsers()) { %>
                                    <span class="assignee-chip" title="<%= assigned.getUserName() %>">
                                        <span class="assignee-avatar"><%= assigned.getUserName().substring(0, 1).toUpperCase() %></span>
                                        <%= assigned.getUserName() %>
                                    </span>
                                <% } %>
                            </div>
                        <% } else { %>
                            <span class="unassigned">Sin asignar</span>
                        <% } %>
                    </td>

                    <td class="col-actions">
                        <button class="icon-btn edit"
                                title="Editar tarea"
                                onclick="openEditModal(
                                    '<%= task.getId() %>',
                                    '<%= task.getTitle().replace("'", "\\'") %>',
                                    '<%= task.getDescription().replace("'", "\\'") %>'
                                )">
                            Editar
                        </button>
                        <button class="icon-btn delete"
                                title="Eliminar tarea"
                                onclick="openDeleteModal('<%= task.getId() %>')">
                            Eliminar
                        </button>
                    </td>

                </tr>

                <% } } %>
                </tbody>
            </table>

            <% if (tasks == null || tasks.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">&#128203;</div>
                    <h3>No existen tareas para este Sprint</h3>
                    <p>Crea la primera tarea para empezar a planear el Sprint Backlog.</p>
                    <button class="btn-new-task" onclick="openCreateModal()">
                        <span class="btn-new-task-icon">+</span>
                        Nueva tarea
                    </button>
                </div>
            <% } %>

            <div class="no-results" id="noResults">
                <p>No se encontraron tareas que coincidan con la búsqueda.</p>
            </div>
        </div>

    </section>

</main>

<div class="modal" id="createModal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Nueva tarea</h2>
            <p class="modal-subtitle">Añade una tarea al Sprint Backlog de <%= sprint != null ? sprint.getName() : "este Sprint" %>.</p>
        </div>

        <form action="${pageContext.request.contextPath}/workspace/createtask" method="post">
            <input type="hidden" name="sprintid" value="<%= sprintId %>">

            <div class="field">
                <label for="title">Título</label>
                <input type="text" id="title" name="title" placeholder="Ej. Diseñar la vista de login" required>
            </div>

            <div class="field">
                <label for="description">Descripción</label>
                <textarea id="description" name="description" placeholder="Describe el trabajo a realizar..." required></textarea>
            </div>

            <div class="field">
                <label>Responsables</label>
                <div class="assignee-picker">
                    <% if (sprint != null && sprint.getCreator() != null) {
                        User creatorUser = sprint.getCreator();
                    %>
                        <label class="assignee-option">
                            <input type="checkbox" name="assignedUserNames" value="<%= creatorUser.getUserName() %>">
                            <span class="assignee-option-avatar"><%= creatorUser.getUserName().substring(0, 1).toUpperCase() %></span>
                            <span class="assignee-option-name"><%= creatorUser.getUserName() %> (Creador)</span>
                        </label>
                    <% } %>

                    <% if (readers != null && !readers.isEmpty()) { %>
                        <% for (User reader : readers) { %>
                            <label class="assignee-option">
                                <input type="checkbox" name="assignedUserNames" value="<%= reader.getUserName() %>">
                                <span class="assignee-option-avatar"><%= reader.getUserName().substring(0, 1).toUpperCase() %></span>
                                <span class="assignee-option-name"><%= reader.getUserName() %></span>
                            </label>
                        <% } %>
                    <% } %>

                    <% if (sprint == null && (readers == null || readers.isEmpty())) { %>
                        <p class="assignee-empty">Este Sprint todavía no tiene usuarios para asignar.</p>
                    <% } %>
                </div>
            </div>

            <div class="modal-buttons">
                <button type="button" class="btn-cancel" onclick="closeCreateModal()">Cancelar</button>
                <button type="submit" class="btn-save">Crear tarea</button>
            </div>
        </form>
    </div>
</div>

<div class="modal" id="editModal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Editar tarea</h2>
            <p class="modal-subtitle">Actualiza el título y la descripción de la tarea.</p>
        </div>

        <form action="${pageContext.request.contextPath}/workspace/updatetaks" method="post">
            <input type="hidden" name="sprintid" value="<%= sprintId %>">
            <input type="hidden" id="editTaskId" name="taskId">

            <div class="field">
                <label for="editTitle">Título</label>
                <input type="text" id="editTitle" name="title" required>
            </div>

            <div class="field">
                <label for="editDescription">Descripción</label>
                <textarea id="editDescription" name="description" required></textarea>
            </div>

            <div class="modal-buttons">
                <button type="button" class="btn-cancel" onclick="closeEditModal()">Cancelar</button>
                <button type="submit" class="btn-save">Guardar cambios</button>
            </div>
        </form>
    </div>
</div>

<div class="modal" id="deleteModal">
    <div class="modal-content modal-content-small">
        <div class="modal-header">
            <h2>Eliminar tarea</h2>
        </div>

        <p class="modal-warning">¿Está seguro de eliminar esta tarea? Esta acción no se puede deshacer.</p>

        <form action="${pageContext.request.contextPath}/workspace/deletetaks" method="post">
            <input type="hidden" name="sprintid" value="<%= sprintId %>">
            <input type="hidden" id="deleteTaskId" name="taskId">

            <div class="modal-buttons">
                <button type="button" class="btn-cancel" onclick="closeDeleteModal()">Cancelar</button>
                <button type="submit" class="btn-save btn-danger">Eliminar</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
<script src="${pageContext.request.contextPath}/scripts/backlog.js"></script>

</body>
</html>
