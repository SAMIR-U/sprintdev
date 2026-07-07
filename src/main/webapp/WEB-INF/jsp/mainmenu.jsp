<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint" %>

<%
    List<Sprint> sprints = (List<Sprint>) session.getAttribute("sprints");
    String sprintMessage = (String) session.getAttribute("sprintmessage");
    session.removeAttribute("sprintmessage");
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">

    <title>SprintDev</title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="preconnect" href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/mainmenu.css">

</head>

<body>

<div class="main-container">

    <div class="topbar">

        <div>

            <h1 class="page-title">
                Mis Sprints
            </h1>

            <p class="page-subtitle">
                Administra todos tus Sprint
            </p>

        </div>

        <button class="btn-new"
                onclick="document.getElementById('modal').style.display='flex'">

            + Nuevo Sprint

        </button>

    </div>

    <% if ("success".equals(sprintMessage)) { %>

    <div class="banner success">
        Sprint creado correctamente.
    </div>

    <% } %>

    <% if ("fail".equals(sprintMessage)) { %>

    <div class="banner error">
        No fue posible crear el Sprint.
    </div>

    <% } %>

    <div class="sprint-grid">

        <%
            if (sprints != null && !sprints.isEmpty()) {

                for (Sprint sprint : sprints) {
        %>

        <div class="sprint-card">

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

        </div>

        <%
                }
            } else {
        %>

        <div class="empty">

            <h2>

                No tienes Sprints

            </h2>

            <p>

                Haz clic en <b>Nuevo Sprint</b> para crear el primero.

            </p>

        </div>

        <%
            }
        %>

    </div>

</div>

<!-- =================== MODAL =================== -->

<div class="modal" id="modal">

    <div class="modal-content">

        <h2>

            Crear Sprint

        </h2>

        <form action="${pageContext.request.contextPath}/createsprint"
              method="post">

            <div class="field">

                <label>

                    Nombre

                </label>

                <input type="text"
                       name="name"
                       required>

            </div>

            <div class="field">

                <label>

                    Objetivo

                </label>

                <textarea name="goal"
                          required></textarea>

            </div>

            <div class="field">

                <label>

                    Fecha Inicio

                </label>

                <input type="date"
                       name="startDate"
                       required>

            </div>

            <div class="field">

                <label>

                    Fecha Fin

                </label>

                <input type="date"
                       name="endDate"
                       required>

            </div>

            <div class="modal-buttons">

                <button type="button"
                        class="btn-cancel"
                        onclick="document.getElementById('modal').style.display='none'">

                    Cancelar

                </button>

                <button type="submit"
                        class="btn-save">

                    Guardar

                </button>

            </div>

        </form>

    </div>

</div>

<script>

window.onclick = function(event){

    const modal = document.getElementById("modal");

    if(event.target === modal){

        modal.style.display = "none";

    }

}

</script>

</body>

</html>