<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

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

<jsp:include page="topbar.jsp"/>

<main class="workspace">
    ${sprintForm}
    <br>
    ${sprintForm.name}
    <section class="workspace-header">
        <div class="workspace-header-context">
            <h2>workspace</h2>
            <p class="page-subtitle">SprintDev</p>
            <h1 class="page-title">Mis sprints</h1>
        </div>

        <div class="workspace-addSprint">
            <button class="addSprintBtn" onclick="openAddSprintForm()">
                + Nuevo Sprint
            </button>
        </div>
    </section>

    <c:if test="${not empty createsprint}">

        <c:choose>

            <c:when test="${createsprint == 'success'}">
                <div class="banner success">
                    Sprint creado correctamente.
                </div>
            </c:when>

            <c:when test="${createsprint == 'fail'}">
                <div class="banner error">
                    No fue posible crear el Sprint.
                </div>
            </c:when>

            <c:otherwise>
                <div class="banner error">
                    ${createsprint}
                </div>
            </c:otherwise>

        </c:choose>

        <script>
            window.sprintMessage = "${createsprint}";
        </script>

    </c:if>

    <section class="sprint-grid">

        <c:choose>

            <c:when test="${not empty sprints}">

                <c:forEach var="sprint" items="${sprints}">

                    <a href="${pageContext.request.contextPath}/workspace/sprint?sprintId=${sprint.sprintId}"
                       class="sprint-card-link">

                        <article class="sprint-card">

                            <h3>
                                ${sprint.name}
                            </h3>

                            <p>
                                ${sprint.goal}
                            </p>

                            <div class="sprint-dates">
                                <strong>Inicio:</strong>
                                ${sprint.startDate}
                                <br>
                                <strong>Fin:</strong>
                                ${sprint.endDate}
                            </div>

                            <span class="sprint-status">

                                <c:choose>

                                    <c:when test="${sprint.status == 'CREATED'}">
                                        <span class="preparacion">
                                            En preparación
                                        </span>
                                    </c:when>

                                    <c:when test="${sprint.status == 'ACTIVE'}">
                                        <span class="activo">
                                            Activo
                                        </span>
                                    </c:when>

                                    <c:when test="${sprint.status == 'CLOSED'}">
                                        <span class="cerrado">
                                            Cerrado
                                        </span>
                                    </c:when>

                                </c:choose>

                            </span>

                        </article>

                    </a>

                </c:forEach>

            </c:when>

            <c:otherwise>

                <div class="empty">
                    <h2>No hay Sprints</h2>

                    <p>
                        Crea tu primer Sprint para comenzar.
                    </p>
                </div>

            </c:otherwise>

        </c:choose>

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
                            value="${sprintForm.name}"
                            required>

                </div>

                <div class="field">

                    <label for="goal">
                        Objetivo
                    </label>

                    <textarea
                            id="goal"
                            name="goal"
                            required>${sprintForm.goal}</textarea>

                </div>

                <div class="field">

                    <label for="startDate">
                        Fecha de inicio
                    </label>

                    <input
                            id="startDate"
                            type="date"
                            name="startDate"
                            value="${sprintForm.startDate}"
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
                            value="${sprintForm.endDate}"
                            required>

                </div>

                <div class="addSprint-buttons">

                    <button type="button"
                            class="btn-cancel"
                            onclick="closeAddSprintForm()">
                        Cancelar
                    </button>

                    <button type="submit"
                            class="btn-save">
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