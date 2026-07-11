<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.SprintStatus" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.User" %>
<%@ page import="java.time.temporal.ChronoUnit" %>

<%

    Sprint sprint = (Sprint) session.getAttribute("sprint");
    User currentUser = (User) session.getAttribute("user");

    String addReaderMsg = (String) session.getAttribute("addreader");
    session.removeAttribute("addreader");

    String activateMsg = (String) session.getAttribute("activesprint");
    session.removeAttribute("activesprint");

    String closeMsg = (String) session.getAttribute("closesprint");
    session.removeAttribute("closesprint");

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

    long durationDays = 0;
    if (sprint != null) {
        durationDays = ChronoUnit.DAYS.between(sprint.getStartDate(), sprint.getEndDate());
    }


    boolean isCreator = sprint != null && currentUser != null
            && sprint.getCreator().getId() == currentUser.getId();

    boolean hasTasks = sprint != null && sprint.getTasks() != null && !sprint.getTasks().isEmpty();


    String existingReadersCsv = "";
    if (sprint != null) {
        StringBuilder csv = new StringBuilder(sprint.getCreator().getUserName());
        for (User reader : sprint.getReaders()) {
            csv.append(",").append(reader.getUserName());
        }
        existingReadersCsv = csv.toString();
    }
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

        <h1 class="sprint-section-title">Sprint</h1>

        <%if(addReaderMsg!=null) {
            if ("success".equals(addReaderMsg)) { %>
                <div class="banner success">Lector agregado correctamente.</div>
            <% }else if ("fail".equals(addReaderMsg)) { %>
                <div class="banner error">No fue posible agregar el lector.</div>
            <% } else{%>
                <div class="banner error"><%=addReaderMsg%></div>
            <%}
        }%>
        
        <% if ("success".equals(activateMsg)) { %>
            <div class="banner success">Sprint activado correctamente.</div>
        <% } %>
        <% if ("fail".equals(activateMsg)) { %>
            <div class="banner error">No fue posible activar el Sprint. Verifica que tenga al menos una tarea.</div>
        <% } %>

        <% if ("success".equals(closeMsg)) { %>
            <div class="banner success">Sprint cerrado correctamente.</div>
        <% } %>
        <% if ("fail".equals(closeMsg)) { %>
            <div class="banner error">No fue posible cerrar el Sprint. Verifica que todas las tareas estén terminadas.</div>
        <% } %>


        <section class="sprint-hero">
            <div class="sprint-hero-info">
                <div class="sprint-hero-title-row">
                    <h1 class="page-title"><%= sprint.getName() %></h1>
                    <span class="sprint-status <%= statusClass %>"><%= statusLabel %></span>
                </div>
            </div>


            <div class="sprint-actions">
                <a class="sprint-nav-btn"
                   href="${pageContext.request.contextPath}/workspace/backlog?sprintId=<%= sprint.getSprintId() %>">
                    Ver Backlog
                </a>
                <a class="sprint-nav-btn disabled" title="Próximamente">
                    Ver Dashboard
                </a>

                <% if (isCreator && sprint.getStatus() == SprintStatus.CREATED) { %>
                    <form class="sprint-status-form"
                          action="${pageContext.request.contextPath}/workspace/sprint/active"
                          method="post"
                          onsubmit="return confirm('¿Activar este Sprint? Una vez activo podrás mover tareas en el tablero.');">
                        <input type="hidden" name="sprintId" value="<%= sprint.getSprintId() %>">
                        <button type="submit" class="sprint-nav-btn primary"
                                <%= hasTasks ? "" : "disabled title=\"Agrega al menos una tarea al Sprint Backlog para poder activarlo\"" %>>
                            Activar Sprint
                        </button>
                    </form>
                <% } %>

                <% if (isCreator && sprint.getStatus() == SprintStatus.ACTIVE) { %>
                    <form class="sprint-status-form"
                          action="${pageContext.request.contextPath}/workspace/sprint/close"
                          method="post"
                          onsubmit="return confirm('¿Cerrar este Sprint? Solo podrás hacerlo si todas las tareas están terminadas, y esta acción no se puede deshacer.');">
                        <input type="hidden" name="sprintId" value="<%= sprint.getSprintId() %>">
                        <button type="submit" class="sprint-nav-btn danger">
                            Cerrar Sprint
                        </button>
                    </form>
                <% } %>
            </div>
        </section>


        <section class="sprint-content">

            <div class="sprint-summary">
                <h2 class="section-title">Detalles del Sprint</h2>

                <div class="sprint-objective">
                    <span class="sprint-objective-label">Objetivo</span>
                    <p><%= sprint.getGoal() %></p>
                </div>

                <div class="sprint-stats-grid">
                    <div class="stat-card">
                        <span class="stat-label">Inicio</span>
                        <span class="stat-value"><%= sprint.getStartDate() %></span>
                    </div>
                    <div class="stat-card">
                        <span class="stat-label">Fin</span>
                        <span class="stat-value"><%= sprint.getEndDate() %></span>
                    </div>
                    <div class="stat-card">
                        <span class="stat-label">Duración</span>
                        <span class="stat-value"><%= durationDays %> días</span>
                    </div>
                    <div class="stat-card">
                        <span class="stat-label">Estado</span>
                        <span class="stat-value"><%= statusLabel %></span>
                    </div>
                </div>
            </div>


            <aside class="sprint-team">
                <div class="sprint-team-header">
                    <h2 class="section-title">Equipo</h2>
                    <% if (isCreator) { %>
                        <button class="team-add-btn" onclick="openAddReaderForm()" title="Agregar lector">
                            +
                        </button>
                    <% } %>
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

                <p class="team-subtitle">Lectores</p>
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

    <% if (isCreator) { %>

    <div class="addSprintMenu" id="addReaderMenu"
         data-context-path="${pageContext.request.contextPath}"
         data-existing-readers="<%= existingReadersCsv %>">
        <div class="addSprintMenu-content">
            <h2>Agregar lector</h2>
            <form id="addReaderForm" action="${pageContext.request.contextPath}/workspace/addreader" method="post">
                <input type="hidden" name="sprintId" value="<%= sprint.getSprintId() %>">
                <input type="hidden" id="readerName" name="readerName" required>
                <div class="field reader-search-field">
                    <label for="readerSearch">Nombre de usuario</label>
                    <input id="readerSearch" type="text" autocomplete="off"
                           placeholder="Escribe para buscar un usuario...">
                    <div id="readerResults" class="reader-results"></div>
                </div>
                <div class="addSprint-buttons">
                    <button type="button" class="btn-cancel" onclick="closeAddReaderForm()">
                        Cancelar
                    </button>
                    <button type="submit" id="addReaderSubmit" class="btn-save" disabled>
                        Agregar lector
                    </button>
                </div>
            </form>
        </div>
    </div>
    <% } %>

    <% } %>

    <script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/sprint.js"></script>
</body>
</html>
