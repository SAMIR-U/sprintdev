<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint" %>

<%
    List<Sprint> sprints = (List<Sprint>) session.getAttribute("sprints");
    String sprintMessage = (String) session.getAttribute("createsprint");
    session.removeAttribute("createsprint");
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Mis Sprints | SprintDev</title>

    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/sidebar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/mainmenu.css">

</head>

<body>

<div class="layout">

    <jsp:include page="sidebar.jsp"/>

    <main class="content">

        <header class="topbar">

            <div>

                <p class="page-subtitle">
                    SprintDev
                </p>

                <h1 class="page-title">
                    Mis Sprints
                </h1>

            </div>

            <button class="btn-new"
                    onclick="document.getElementById('modal').style.display='flex'">

                + Nuevo Sprint

            </button>

        </header>

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

        <section class="sprint-grid">

            <% if (sprints != null && !sprints.isEmpty()) { %>

                <% for (Sprint sprint : sprints) { %>

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

                        <span class="sprint-status preparacion">

                            En preparación

                        </span>

                    </article>

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

    </main>

</div>

<div class="modal" id="modal">

    <div class="modal-content">

        <h2>

            Crear Sprint

        </h2>

        <form action="${pageContext.request.contextPath}/createsprint"
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

            <div class="modal-buttons">

                <button
                        type="button"
                        class="btn-cancel"
                        onclick="closeModal()">

                    Cancelar

                </button>

                <button
                        type="submit"
                        class="btn-save">

                    Guardar Sprint

                </button>

            </div>

        </form>

    </div>

</div>

<script>

const modal = document.getElementById("modal");

function openModal() {
    modal.style.display = "flex";
}

function closeModal() {
    modal.style.display = "none";
}

window.onclick = function(event) {

    if (event.target === modal) {

        closeModal();

    }

};

document.querySelector(".btn-new").addEventListener("click", openModal);

</script>

</body>

</html>