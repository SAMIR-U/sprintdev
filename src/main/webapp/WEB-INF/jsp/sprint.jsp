<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.Task" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.User" %>

<%

    Sprint sprint = (Sprint) session.getAttribute("sprint");
    User currentUser = (User) session.getAttribute("user");


    String addReaderMsg = (String) session.getAttribute("addreader");
    session.removeAttribute("addreader");
    String createTaskMsg = (String) session.getAttribute("createtask");
    session.removeAttribute("createtask");


    String statusLabel = "En preparación";
    String statusClass = "preparacion";
    if (sprint != null) {
        switch (sprint.getStatus()) {
            case ACTIVE:
                statusLabel = "Activo";
                statusClass = "activo";
                break;
            case CLOSED:
                statusLabel = "Cerrado";
                statusClass = "cerrado";
                break;
            default:
                statusLabel = "En preparación";
                statusClass = "preparacion";
        }
    }


    int pending = 0, inProgress = 0, inReview = 0, completed = 0;
    if (sprint != null && sprint.getTasks() != null) {
        for (Task task : sprint.getTasks()) {
            switch (task.getStatus()) {
                case PENDING: pending++; break;
                case IN_PROGRESS: inProgress++; break;
                case IN_REVIEW: inReview++; break;
                case COMPLETED: completed++; break;
            }
        }
    }
    int totalTasks = pending + inProgress + inReview + completed;

    boolean isCreator = sprint != null && currentUser != null
            && sprint.getCreator().getId() == currentUser.getId();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= sprint != null ? sprint.getName() : "Sprint" %> | SprintDev</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link
        href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
        rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/topbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/workspace.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sprint.css">
</head>
<body>
    <jsp:include page="topbar.jsp" />

    <% if (sprint == null) { %>
        <main class="workspace">
            <div class="empty">
                <h2>No se encontró el Sprint</h2>
                <p>Vuelve a Mis Sprints e inténtalo de nuevo.</p>
            </div>
        </main>
    <% } else { %>

    <main class="sprint-page">


        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/workspace">Mis sprints</a>
            <span class="crumb-separator">/</span>
            <span class="crumb-current"><%= sprint.getName() %></span>
        </nav>

        <% if ("success".equals(addReaderMsg)) { %>
            <div class="banner success">Lector agregado correctamente.</div>
        <% } %>
        <% if ("fail".equals(addReaderMsg)) { %>
            <div class="banner error">No fue posible agregar el lector.</div>
        <% } %>
        <% if ("success".equals(createTaskMsg)) { %>
            <div class="banner success">Tarea creada correctamente.</div>
        <% } %>
        <% if ("fail".equals(createTaskMsg)) { %>
            <div class="banner error">No fue posible crear la tarea.</div>
        <% } %>


        <section class="sprint-hero">
            <div class="sprint-hero-info">
                <div class="sprint-hero-title-row">
                    <h1 class="page-title"><%= sprint.getName() %></h1>
                    <span class="sprint-status <%= statusClass %>"><%= statusLabel %></span>
                </div>
                <p class="sprint-goal"><%= sprint.getGoal() %></p>
                <div class="sprint-dates">
                    <strong>Inicio:</strong> <%= sprint.getStartDate() %>
                    &nbsp;&nbsp;
                    <strong>Fin:</strong> <%= sprint.getEndDate() %>
                </div>
            </div>

            <div class="sprint-actions">
                <a class="sprint-nav-btn"
                   href="${pageContext.request.contextPath}/sprint/backlog">
                    Ver Backlog
                </a>
                <a class="sprint-nav-btn disabled" title="Próximamente">
                    Ver Dashboard
                </a>
                <button class="sprint-nav-btn primary" onclick="openAddTaskForm()">
                    + Añadir tarea
                </button>
            </div>
        </section>


        <section class="sprint-content">

            <div class="sprint-summary">
                <h2 class="section-title">Resumen de tareas</h2>
                <% if (totalTasks == 0) { %>
                    <div class="empty small">
                        <h3>Aún no hay tareas</h3>
                        <p>Usa "+ Añadir tarea" o ve al Backlog para crear la primera.</p>
                    </div>
                <% } else { %>
                    <div class="summary-grid">
                        <div class="summary-card">
                            <span class="summary-number"><%= pending %></span>
                            <span class="summary-label">Pendientes</span>
                        </div>
                        <div class="summary-card">
                            <span class="summary-number"><%= inProgress %></span>
                            <span class="summary-label">En progreso</span>
                        </div>
                        <div class="summary-card">
                            <span class="summary-number"><%= inReview %></span>
                            <span class="summary-label">En revisión</span>
                        </div>
                        <div class="summary-card">
                            <span class="summary-number"><%= completed %></span>
                            <span class="summary-label">Completadas</span>
                        </div>
                    </div>
                <% } %>
            </div>


            <aside class="sprint-team">
                <div class="sprint-team-header">
                    <h2 class="section-title">Equipo</h2>
                    <button class="team-add-btn" onclick="openAddReaderForm()" title="Agregar lector">
                        +
                    </button>
                </div>

                <p class="team-subtitle">Creador</p>
                <div class="team-member creator">
                    <div class="team-avatar">
                        <%= sprint.getCreator().getUserName().substring(0, 1).toUpperCase() %>
                    </div>
                    <div class="team-member-info">
                        <strong><%= sprint.getCreator().getUserName() %></strong>
                        <span>Creador</span>
                    </div>
                </div>

                <p class="team-subtitle">
                    Lectores
                    <a class="team-link"
                       href="${pageContext.request.contextPath}/sprint/readers?sprintId=<%= sprint.getSprintId() %>">
                        Ver todos
                    </a>
                </p>
                <% if (sprint.getReaders() == null || sprint.getReaders().isEmpty()) { %>
                    <p class="team-empty">Todavía no hay lectores en este Sprint.</p>
                <% } else { %>
                    <% for (User reader : sprint.getReaders()) { %>
                        <div class="team-member">
                            <div class="team-avatar reader">
                                <%= reader.getUserName().substring(0, 1).toUpperCase() %>
                            </div>
                            <div class="team-member-info">
                                <strong><%= reader.getUserName() %></strong>
                                <span>Lector</span>
                            </div>
                        </div>
                    <% } %>
                <% } %>
            </aside>
        </section>
    </main>


    <div class="addSprintMenu" id="addTaskMenu">
        <div class="addSprintMenu-content">
            <h2>Añadir tarea</h2>
            <form action="${pageContext.request.contextPath}/sprint/createtask" method="post">
                <input type="hidden" name="sprintid" value="<%= sprint.getSprintId() %>">
                <div class="field">
                    <label for="title">Título</label>
                    <input id="title" type="text" name="title" required>
                </div>
                <div class="field">
                    <label for="description">Descripción</label>
                    <textarea id="description" name="description" required></textarea>
                </div>
                <div class="addSprint-buttons">
                    <button type="button" class="btn-cancel" onclick="closeAddTaskForm()">
                        Cancelar
                    </button>
                    <button type="submit" class="btn-save">
                        Guardar tarea
                    </button>
                </div>
            </form>
        </div>
    </div>


    <div class="addSprintMenu" id="addReaderMenu">
        <div class="addSprintMenu-content">
            <h2>Agregar lector</h2>
            <form action="${pageContext.request.contextPath}/sprint/addreader" method="post">
                <input type="hidden" name="sprintId" value="<%= sprint.getSprintId() %>">
                <div class="field">
                    <label for="readerName">Nombre de usuario</label>
                    <input id="readerName" type="text" name="readerName"
                           placeholder="Escribe el usuario a agregar" required>
                </div>
                <div class="addSprint-buttons">
                    <button type="button" class="btn-cancel" onclick="closeAddReaderForm()">
                        Cancelar
                    </button>
                    <button type="submit" class="btn-save">
                        Agregar lector
                    </button>
                </div>
            </form>
        </div>
    </div>

    <% } %>

    <script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/sprint.js"></script>
</body>
</html>
