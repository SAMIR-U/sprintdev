    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.List" %>
    <%@ page import="edu.uptc.swi.sprintdev.domain.Sprint" %>

    <%
        List<Sprint> sprints = (List<Sprint>) session.getAttribute("sprints");
        String sprintMessage = (String) session.getAttribute("createsprint");
        session.removeAttribute("createsprint");
    %>

    <!DOCTYPE html>
    <html lang = "es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Mis Sprints | SprintDev</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link
            href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
            rel="stylesheet">
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/workspace.css">
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/topbar.css">
    </head>
    <body>
        <jsp:include page="topbar.jsp" />
        <main class= "workspace">
            <section class = "workspace-header">
                <div class = "workspace-header-context">
                      <h2> workspace </h2>
                      <p class="page-subtitle"> SprintDev </p>
                      <h1 class= "page-title"> Mis sprints </h1>
                </div>
                <div class = "workspace-addSprint" >
                    <button class="addSprintBtn" onclick="openAddSprintForm()"> + Nuevo Sprint </button>
                </div>
            </section>
             <% if (sprintMessage != null) {
                    if ("success".equals(sprintMessage)) { %>
                        <div class="banner success">
                            Sprint creado correctamente.
                        </div>
                    <% } else if ("fail".equals(sprintMessage)) { %>
                        <div class="banner error">
                            No fue posible crear el Sprint.
                        </div>
                    <% } else { %>
                        <div class="banner error">
                            <%= sprintMessage %>
                        </div>
                    <% }
                } %>
            <section class = "sprint-grid">
                <% if (sprints != null && !sprints.isEmpty()) { %>
                    <% for (Sprint sprint : sprints) { %>
                    <a href="${pageContext.request.contextPath}/workspace/sprint?sprintId=<%= sprint.getSprintId() %>"
                    class = "sprint-card-link">
                    <article class="sprint-card">
                                            <h3>
                                                <%= sprint.getName() %>
                                            </h3>
                                            <p>
                                                <%= sprint.getGoal() %>
                                            </p>
                                            <div class="sprint-dates">
                                                <strong>Inicio:</strong>
                                                <%= sprint.getStartDate() %>
                                                <br>
                                                <strong>Fin:</strong>
                                                <%= sprint.getEndDate() %>
                                            </div>
                                           <span class="sprint-status">
                                             <%@ page import="edu.uptc.swi.sprintdev.domain.SprintStatus" %>

                                            <%
                                              SprintStatus status = sprint.getStatus();

                                              if (status == SprintStatus.CREATED) {
                                            %>
                                                  <span class="preparacion">En preparación</span>
                                            <%
                                              } else if (status == SprintStatus.ACTIVE) {
                                            %>
                                                  <span class="activo">Activo</span>
                                            <%
                                              } else if (status == SprintStatus.CLOSED) {
                                            %>
                                                  <span class="cerrado">Cerrado</span>
                                            <%
                                                 }
                                            %>
                                           </span>
                                        </article>
                    </a>
                    <% } %>
                <% } else { %>
                    <div class="empty">
                        <h2>
                            No hay Sprints
                        </h2>
                        <p>
                            Crea tu primer Sprint para comenzar.
                        </p>
                    </div>
                <% } %>
            </section>
           <div class="addSprintMenu" id="addSprintMenu">
               <div class="addSprintMenu-content">
                   <h2>Crear Sprint</h2>
                   <form action="${pageContext.request.contextPath}/workspace/createsprint"
                         method="post">
                       <div class="field">
                           <label for="name">
                               Nombre
                           </label>
                           <input
                                   id="name"
                                   type="text"
                                   name="name"
                                   required>
                       </div>
                       <div class="field">
                           <label for="goal">
                               Objetivo
                           </label>
                           <textarea
                                   id="goal"
                                   name="goal"
                                   required></textarea>
                       </div>
                       <div class="field">
                           <label for="startDate">
                               Fecha de inicio
                           </label>
                           <input
                                   id="startDate"
                                   type="date"
                                   name="startDate"
                                   required>
                       </div>

                       <div class="field">
                           <label for="endDate">
                               Fecha de finalización
                           </label>
                           <input
                                   id="endDate"
                                   type="date"
                                   name="endDate"
                                   required>
                       </div>
                       <div class="addSprint-buttons">
                           <button type="button" class="btn-cancel" onclick="closeAddSprintForm()">
                               Cancelar
                           </button>
                           <button type="submit" class="btn-save">
                               Guardar Sprint
                           </button>
                       </div>
                   </form>
               </div>
           </div>
        </main>
         <script src="${pageContext.request.contextPath}/scripts/bannerMessage.js"></script>
        <script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
        <script src="${pageContext.request.contextPath}/scripts/workspace.js"></script>
    </body>
    </html>