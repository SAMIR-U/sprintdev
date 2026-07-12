<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.Task"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint"%>
<%@ page import="edu.uptc.swi.sprintdev.domain.TaskStatus"%>

<%
    Sprint sprint = (Sprint) session.getAttribute("sprint");
    List<Task> tasks = sprint.getTasks();
    int sprintId = Integer.parseInt(request.getParameter("sprintId"));
%>
<!DOCTYPE html>
<html lang ="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard | SprintDev</title>
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
                      rel="stylesheet">

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/topbar.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>
    <body>
        <jsp:include page="topbar.jsp"/>
        <main class = "dashboard-page">
              <nav class="breadcrumb">
                    <a href="${pageContext.request.contextPath}/workspace">Mis sprints</a>
                    <span class="crumb-separator">/</span>
                    <a href="${pageContext.request.contextPath}/workspace/sprint?sprintId=<%= sprintId %>">
                        <%= sprint != null ? sprint.getName() : "Sprint" %>
                    </a>
                    <span class="crumb-separator">/</span>
                    <span class="crumb-current">Dashboard</span>
                </nav>
               <div class="page-header">
                       <div>
                           <p class="page-eyebrow">SprintDev</p>
                           <h1 class="page-title">Dashboard</h1>
                           <p class="page-description">
                               Visualiza / modifica el estado de tus tareas en
                               <strong><%= sprint != null ? sprint.getName() : "este Sprint" %></strong>.
                           </p>
                       </div>
                        <a href ="${pageContext.request.contextPath}/workspace/backlog?sprintId=<%= sprint.getSprintId() %>"
                        class = "a-go-to-backlog"> Ir a SprintBacklog </a>
               </div>
               <section class = "board" id = "board">

               </section>

        </main>
        <script src="${pageContext.request.contextPath}/scripts/bannerMessage.js"></script>
        <script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
    </body>
</html>