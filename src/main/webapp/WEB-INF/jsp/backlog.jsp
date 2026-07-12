<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="tasks" value="${tasks}" />
<c:set var="readers" value="${readers}" />
<c:set var="sprint" value="${sprint}" />
<c:set var="createMessage" value="${createtask}" />
<c:set var="editMessage" value="${edittask}" />
<c:set var="deleteMessage" value="${deletetask}" />

<c:set var="sprintId" value="${param.sprintId}" />

<c:set var="totalTasks" value="${empty tasks ? 0 : fn:length(tasks)}" />
<c:set var="pendingCount" value="0" />
<c:set var="progressCount" value="0" />
<c:set var="completedCount" value="0" />

<c:forEach var="t" items="${tasks}">
    <c:choose>
        <c:when test="${t.status == 'COMPLETED'}">
            <c:set var="completedCount" value="${completedCount + 1}" />
        </c:when>
        <c:when test="${t.status == 'IN_PROGRESS' || t.status == 'IN_REVIEW'}">
            <c:set var="progressCount" value="${progressCount + 1}" />
        </c:when>
        <c:otherwise>
            <c:set var="pendingCount" value="${pendingCount + 1}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

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
        <a href="${pageContext.request.contextPath}/workspace/sprint?sprintId=${sprintId}">
            <c:out value="${not empty sprint ? sprint.name : 'Sprint'}" />
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
                <strong><c:out value="${not empty sprint ? sprint.name : 'este Sprint'}" /></strong>.
            </p>
        </div>

        <div class="header-actions">
            <a class="btn-dashboard" href="${pageContext.request.contextPath}/workspace/dashboard?sprintId=${sprintId}">Ver dashboard</a>
            <button class="btn-new-task" onclick="openCreateModal()">
                <span class="btn-new-task-icon">+</span>
                Nueva tarea
            </button>
        </div>
    </div>

    <c:if test="${not empty createMessage}">
        <c:choose>
            <c:when test="${createMessage == 'success'}">
                <div class="banner success">La tarea fue creada correctamente.</div>
            </c:when>
            <c:when test="${createMessage == 'fail'}">
                <div class="banner error">No fue posible crear la tarea.</div>
            </c:when>
            <c:otherwise>
                <div class="banner error">${createMessage}</div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${not empty editMessage}">
        <c:choose>
            <c:when test="${editMessage == 'success'}">
                <div class="banner success">La tarea fue actualizada correctamente.</div>
            </c:when>
            <c:when test="${editMessage == 'fail'}">
                <div class="banner error">No fue posible actualizar la tarea.</div>
            </c:when>
            <c:otherwise>
                <div class="banner error">${editMessage}</div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${not empty deleteMessage}">
        <c:choose>
            <c:when test="${deleteMessage == 'success'}">
                <div class="banner success">La tarea fue eliminada correctamente.</div>
            </c:when>
            <c:when test="${deleteMessage == 'fail'}">
                <div class="banner error">No fue posible eliminar la tarea.</div>
            </c:when>
            <c:otherwise>
                <div class="banner error">${deleteMessage}</div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <section class="backlog-stats">
        <div class="stat-card">
            <span class="stat-label">Total de tareas</span>
            <span class="stat-value">${totalTasks}</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Pendientes</span>
            <span class="stat-value stat-pending">${pendingCount}</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">En progreso</span>
            <span class="stat-value stat-progress">${progressCount}</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Completadas</span>
            <span class="stat-value stat-completed">${completedCount}</span>
        </div>
    </section>

    <section class="backlog-panel">

        <div class="toolbar">
            <div class="search-field">
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
                        <th class="col-task">Tarea</th>
                        <th class="col-status">Estado</th>
                        <th class="col-assignees">Responsables</th>
                        <th class="col-actions">Acciones</th>
                    </tr>
                </thead>
                <tbody id="taskTableBody">

                <c:forEach var="task" items="${tasks}">

                    <c:choose>
                        <c:when test="${task.status == 'IN_PROGRESS'}">
                            <c:set var="statusClass" value="progress" />
                            <c:set var="statusLabel" value="En progreso" />
                            <c:set var="statusValue" value="IN_PROGRESS" />
                        </c:when>
                        <c:when test="${task.status == 'IN_REVIEW'}">
                            <c:set var="statusClass" value="review" />
                            <c:set var="statusLabel" value="En revisión" />
                            <c:set var="statusValue" value="IN_REVIEW" />
                        </c:when>
                        <c:when test="${task.status == 'COMPLETED'}">
                            <c:set var="statusClass" value="completed" />
                            <c:set var="statusLabel" value="Completada" />
                            <c:set var="statusValue" value="COMPLETED" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="statusClass" value="pending" />
                            <c:set var="statusLabel" value="Pendiente" />
                            <c:set var="statusValue" value="PENDING" />
                        </c:otherwise>
                    </c:choose>

                    <c:set var="searchBlob" value="${fn:toLowerCase(task.title)} ${fn:toLowerCase(task.description)}" />
                    <c:forEach var="su" items="${task.assignedUsers}">
                        <c:set var="searchBlob" value="${searchBlob} ${fn:toLowerCase(su.userName)}" />
                    </c:forEach>
                    <c:set var="searchBlob" value="${fn:replace(searchBlob, '\"', '')}" />

                    <tr class="task-row" data-status="${statusValue}" data-search="${searchBlob}">

                        <td class="col-task">
                            <div class="task-title"><c:out value="${task.title}" /></div>
                            <div class="task-description"><c:out value="${task.description}" /></div>
                        </td>

                        <td class="col-status">
                            <span class="status ${statusClass}">${statusLabel}</span>
                        </td>

                        <td class="col-assignees">
                            <c:choose>
                                <c:when test="${not empty task.assignedUsers}">
                                    <div class="assignee-list">
                                        <c:forEach var="assigned" items="${task.assignedUsers}">
                                            <span class="assignee-chip" title="${fn:escapeXml(assigned.userName)}">
                                                <span class="assignee-avatar">${fn:toUpperCase(fn:substring(assigned.userName, 0, 1))}</span>
                                                <c:out value="${assigned.userName}" />
                                            </span>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <span class="unassigned">Sin asignar</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td class="col-actions">
                            <button class="icon-btn edit"
                                    title="Editar tarea"
                                    onclick="openEditModal(
                                        '${task.id}',
                                        '${fn:replace(task.title, "'", "\\'")}',
                                        '${fn:replace(task.description, "'", "\\'")}'
                                    )">
                                Editar
                            </button>
                            <button class="icon-btn delete"
                                    title="Eliminar tarea"
                                    onclick="openDeleteModal('${task.id}')">
                                Eliminar
                            </button>
                        </td>

                    </tr>

                </c:forEach>

                </tbody>
            </table>

            <c:if test="${empty tasks}">
                <div class="empty-state">
                    <h3>No existen tareas para este Sprint</h3>
                    <p>Crea la primera tarea para empezar a planear el Sprint Backlog.</p>
                    <button class="btn-new-task" onclick="openCreateModal()">
                        <span class="btn-new-task-icon">+</span>
                        Nueva tarea
                    </button>
                </div>
            </c:if>

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
            <p class="modal-subtitle">Añade una tarea al Sprint Backlog de <c:out value="${not empty sprint ? sprint.name : 'este Sprint'}" />.</p>
        </div>

        <form action="${pageContext.request.contextPath}/workspace/createtask" method="post">
            <input type="hidden" name="sprintid" value="${sprintId}">

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
                    <c:if test="${not empty sprint and not empty sprint.creator}">
                        <c:set var="creatorUser" value="${sprint.creator}" />
                        <label class="assignee-option">
                            <input type="checkbox" name="assignedUserNames" value="${creatorUser.userName}">
                            <span class="assignee-option-avatar">${fn:toUpperCase(fn:substring(creatorUser.userName, 0, 1))}</span>
                            <span class="assignee-option-name"><c:out value="${creatorUser.userName}" /></span>
                        </label>
                    </c:if>

                    <c:if test="${not empty readers}">
                        <c:forEach var="reader" items="${readers}">
                            <label class="assignee-option">
                                <input type="checkbox" name="assignedUserNames" value="${reader.userName}">
                                <span class="assignee-option-avatar">${fn:toUpperCase(fn:substring(reader.userName, 0, 1))}</span>
                                <span class="assignee-option-name"><c:out value="${reader.userName}" /></span>
                            </label>
                        </c:forEach>
                    </c:if>

                    <c:if test="${empty sprint and empty readers}">
                        <p class="assignee-empty">Este Sprint todavía no tiene usuarios para asignar.</p>
                    </c:if>
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
            <input type="hidden" name="sprintid" value="${sprintId}">
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
            <input type="hidden" name="sprintid" value="${sprintId}">
            <input type="hidden" id="deleteTaskId" name="taskId">

            <div class="modal-buttons">
                <button type="button" class="btn-cancel" onclick="closeDeleteModal()">Cancelar</button>
                <button type="submit" class="btn-save btn-danger">Eliminar</button>
            </div>
        </form>
    </div>
</div>
<script src="${pageContext.request.contextPath}/scripts/bannerMessage.js"></script>
<script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
<script src="${pageContext.request.contextPath}/scripts/backlog.js"></script>

</body>
</html>
