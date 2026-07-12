<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.TaskStatus" %>
<%
    Sprint sprint = (Sprint) session.getAttribute("sprint");
    int sprintId = Integer.parseInt(request.getParameter("sprintId"));
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | SprintDev</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/topbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <jsp:include page="topbar.jsp"/>
    <main class="dashboard-page" id="dashboard" data-context-path="${pageContext.request.contextPath}" data-sprint-id="<%= sprintId %>" data-version="<%= sprint != null ? sprint.getVersion() : 0 %>">
        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/workspace">Mis sprints</a>
            <span class="crumb-separator">/</span>
            <a href="${pageContext.request.contextPath}/workspace/sprint?sprintId=<%= sprintId %>"><%= sprint != null ? sprint.getName() : "Sprint" %></a>
            <span class="crumb-separator">/</span>
            <span class="crumb-current">Dashboard</span>
        </nav>
        <div class="page-header">
            <div>
                <p class="page-eyebrow">SprintDev</p>
                <h1 class="page-title">Dashboard</h1>
                <p class="page-description">Visualiza y modifica el estado de tus tareas en <strong><%= sprint != null ? sprint.getName() : "este Sprint" %></strong>.</p>
            </div>
            <a href="${pageContext.request.contextPath}/workspace/backlog?sprintId=<%= sprintId %>" class="a-go-to-backlog">Ir a Sprint Backlog</a>
        </div>
        <div id="dashboardMessage" aria-live="polite"></div>
        <p class="board-help">Arrastra una tarea a su siguiente estado permitido.</p>
        <section class="board" id="board" aria-label="Tablero de tareas">
            <% TaskStatus[] statuses = {TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.IN_REVIEW, TaskStatus.COMPLETED};
               String[] labels = {"Por realizar", "En progreso", "En revisión", "Completadas"};
               String[] classes = {"pending", "progress", "review", "completed"};
               for (int i = 0; i < statuses.length; i++) { %>
                <section class="column column-<%= classes[i] %>" data-status="<%= statuses[i] %>">
                    <header class="column-head"><h2><%= labels[i] %></h2><span class="task-count">0</span></header>
                    <div class="drop-zone" data-list></div>
                </section>
            <% } %>
        </section>
        <template id="task-template">
            <article class="task" draggable="true"><span class="task-grip" aria-hidden="true">⠿</span><div class="task-content"><h3></h3><div class="task-assignees"></div></div></article>
        </template>
    </main>
    <script src="${pageContext.request.contextPath}/scripts/bannerMessage.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/dashboard.js"></script>
</body>
</html>
