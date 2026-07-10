<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.Task"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.User"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint"%>


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
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Backlog | SprintDev</title>

    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/topbar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/backlog.css">

</head>

<body>

<jsp:include page="topbar.jsp"/>

<main class="workspace">

    <section class="workspace-content">

<nav class="breadcrumb">

    <a href="${pageContext.request.contextPath}/workspace">
        Mis sprints
    </a>

    <span>/</span>

<span><%= sprint != null ? sprint.getName() : "Sprint" %></span>
    <span>/</span>

    <strong>Backlog</strong>

</nav>

<div class="page-header">

    <div>

        <h2 class="workspace-title">

            Backlog

        </h2>

        <p class="page-subtitle">

            SprintDev

        </p>

        <h1 class="page-title">

            Backlog

        </h1>

    </div>

    <button class="btn-primary"
            onclick="openCreateModal()">

        + Nueva tarea

    </button>

</div>

        <% if ("success".equals(createMessage)) { %>

            <div class="banner success">

                La tarea fue creada correctamente.

            </div>

        <% } %>

        <% if ("fail".equals(createMessage)) { %>

            <div class="banner error">

                No fue posible crear la tarea.

            </div>

        <% } %>

        <% if ("success".equals(editMessage)) { %>

            <div class="banner success">

                La tarea fue actualizada correctamente.

            </div>

        <% } %>

        <% if ("fail".equals(editMessage)) { %>

            <div class="banner error">

                No fue posible actualizar la tarea.

            </div>

        <% } %>

        <% if ("success".equals(deleteMessage)) { %>

            <div class="banner success">

                La tarea fue eliminada correctamente.

            </div>

        <% } %>

        <% if ("fail".equals(deleteMessage)) { %>

            <div class="banner error">

                No fue posible eliminar la tarea.

            </div>

        <% } %>

        <div class="toolbar">

            <input type="text"
                   id="searchTask"
                   placeholder="Buscar tarea...">

        </div>

        <div class="table-container">

            <table>

                <thead>

                    <tr>

                        <th>ID</th>

                        <th>Título</th>

                        <th>Descripción</th>

                        <th>Responsables</th>

                        <th>Acciones</th>

                    </tr>

                </thead>

                <tbody>
                    <%
    if (tasks != null && !tasks.isEmpty()) {

        for (Task task : tasks) {
%>

<tr>

    <td>
        <%= task.getId() %>
    </td>

    <td>
        <%= task.getTitle() %>
    </td>

    <td>
        <%= task.getDescription() %>
    </td>

    <td>

        <%
            if (task.getAssignedUsers() != null &&
                !task.getAssignedUsers().isEmpty()) {

                for (User assigned : task.getAssignedUsers()) {
        %>

            <div>

                <%= assigned.getUserName() %>

            </div>

        <%
                }

            } else {
        %>

            <span>

                Sin asignar

            </span>

        <%
            }
        %>

    </td>

    <td>

        <button
            class="btn-table"
            onclick="openEditModal(
                '<%= task.getId() %>',
                '<%= task.getTitle().replace("'", "\\'") %>',
                '<%= task.getDescription().replace("'", "\\'") %>'
            )">

            Editar

        </button>

        <button
            class="btn-table delete"
            onclick="openDeleteModal('<%= task.getId() %>')">

            Eliminar

        </button>

    </td>

</tr>

<%
        }

    } else {
%>

<tr>

    <td colspan="5"
        style="text-align:center;padding:35px;">

        No existen tareas para este Sprint.

    </td>

</tr>

<%
    }
%>

                </tbody>

            </table>

        </div>

        <div class="modal" id="createModal">

            <div class="modal-content">

                <h2>Nueva tarea</h2>

                <form action="${pageContext.request.contextPath}/workspace/createtask"
                      method="post">

                    <input type="hidden"
                           name="sprintid"
                           value="<%= sprintId %>">

                    <div class="field">

                        <label for="title">

                            Título

                        </label>

                        <input type="text"
                               id="title"
                               name="title"
                               required>

                    </div>

                    <div class="field">

                        <label for="description">

                            Descripción

                        </label>

                        <textarea id="description"
                                  name="description"
                                  required></textarea>

                    </div>

                    <div class="field">

                        <label>

                            Responsables

                        </label>

                        <select name="assignedUserNames"
                                multiple
                                size="6">

                            <% if(readers != null){ %>

                                <% for(User reader : readers){ %>

                                    <option value="<%= reader.getUserName() %>">

                                        <%= reader.getUserName() %>

                                    </option>

                                <% } %>

                            <% } %>

                        </select>

                    </div>

                    <div class="modal-buttons">

                        <button type="button"
                                class="btn-cancel"
                                onclick="closeCreateModal()">

                            Cancelar

                        </button>

                        <button type="submit"
                                class="btn-save">

                            Crear tarea

                        </button>

                    </div>

                </form>

            </div>

        </div>


        <div class="modal" id="editModal">

            <div class="modal-content">

                <h2>Editar tarea</h2>

                <form action="${pageContext.request.contextPath}/workspace/updatetaks"
                      method="post">

                    <input type="hidden"
                           name="sprintid"
                           value="<%= sprintId %>">

                    <input type="hidden"
                           id="editTaskId"
                           name="taskId">

                    <div class="field">

                        <label for="editTitle">

                            Título

                        </label>

                        <input type="text"
                               id="editTitle"
                               name="title"
                               required>

                    </div>

                    <div class="field">

                        <label for="editDescription">

                            Descripción

                        </label>

                        <textarea id="editDescription"
                                  name="description"
                                  required></textarea>

                    </div>

                    <div class="modal-buttons">

                        <button type="button"
                                class="btn-cancel"
                                onclick="closeEditModal()">

                            Cancelar

                        </button>

                        <button type="submit"
                                class="btn-save">

                            Guardar cambios

                        </button>

                    </div>

                </form>

            </div>

        </div>

        <div class="modal" id="deleteModal">

            <div class="modal-content">

                <h2>Eliminar tarea</h2>

                <p>

                    ¿Está seguro de eliminar esta tarea?

                </p>

                <form action="${pageContext.request.contextPath}/workspace/deletetaks"
                      method="post">

                    <input type="hidden"
                           name="sprintid"
                           value="<%= sprintId %>">

                    <input type="hidden"
                           id="deleteTaskId"
                           name="taskId">

                    <div class="modal-buttons">

                        <button type="button"
                                class="btn-cancel"
                                onclick="closeDeleteModal()">

                            Cancelar

                        </button>

                        <button type="submit"
                                class="btn-save">

                            Eliminar

                        </button>

                    </div>

                </form>

            </div>

        </div>

    </section>

</main>

<script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
<script src="${pageContext.request.contextPath}/scripts/backlog.js"></script>

</body>
</html>