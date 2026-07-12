<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="sprint" value="${sessionScope.sprint}" />
<c:set var="currentUser" value="${sessionScope.user}" />

<c:set var="addReaderMsg" value="${addreader}" />
<c:remove var="addreader" scope="session" />

<c:set var="activateMsg" value="${activesprint}" />
<c:remove var="activesprint" scope="session" />

<c:set var="closeMsg" value="${closesprint}" />
<c:remove var="closesprint" scope="session" />

<c:choose>
    <c:when test="${not empty sprint and sprint.status == 'ACTIVE'}">
        <c:set var="statusLabel" value="Activo" />
        <c:set var="statusClass" value="activo" />
    </c:when>
    <c:when test="${not empty sprint and sprint.status == 'CLOSED'}">
        <c:set var="statusLabel" value="Cerrado" />
        <c:set var="statusClass" value="cerrado" />
    </c:when>
    <c:otherwise>
        <c:set var="statusLabel" value="En preparación" />
        <c:set var="statusClass" value="preparacion" />
    </c:otherwise>
</c:choose>

<%-- El calculo de dias entre fechas requiere ChronoUnit, EL no tiene aritmetica de fechas nativa --%>
<c:set var="durationDays" value="0" />
<% if (session.getAttribute("sprint") != null) {
    edu.uptc.swi.sprintdev.domain.Sprint s = (edu.uptc.swi.sprintdev.domain.Sprint) session.getAttribute("sprint");
    pageContext.setAttribute("durationDays", ChronoUnit.DAYS.between(s.getStartDate(), s.getEndDate()));
} %>

<c:set var="isCreator" value="${not empty sprint and not empty currentUser and sprint.creator.id == currentUser.id}" />
<c:set var="hasTasks" value="${not empty sprint and not empty sprint.tasks}" />

<c:set var="existingReadersCsv" value="" />
<c:if test="${not empty sprint}">
    <c:set var="existingReadersCsv" value="${sprint.creator.userName}" />
    <c:forEach var="reader" items="${sprint.readers}">
        <c:set var="existingReadersCsv" value="${existingReadersCsv},${reader.userName}" />
    </c:forEach>
</c:if>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${not empty sprint ? sprint.name : 'Sprint'}" /> | SprintDev</title>
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

    <c:choose>
    <c:when test="${empty sprint}">
        <main class="workspace">
            <div class="empty">
                <h2>No se encontró el Sprint</h2>
                <p>Vuelve a Mis Sprints e inténtalo de nuevo.</p>
            </div>
        </main>
    </c:when>
    <c:otherwise>

    <main class="sprint-page">


        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/workspace">Mis sprints</a>
            <span class="crumb-separator">/</span>
            <span class="crumb-current"><c:out value="${sprint.name}" /></span>
        </nav>

        <h1 class="sprint-section-title">Sprint</h1>

        <c:if test="${not empty addReaderMsg}">
            <c:choose>
                <c:when test="${addReaderMsg == 'success'}">
                    <div class="banner success">Lector agregado correctamente.</div>
                </c:when>
                <c:when test="${addReaderMsg == 'fail'}">
                    <div class="banner error">No fue posible agregar el lector.</div>
                </c:when>
                <c:otherwise>
                    <div class="banner error">${addReaderMsg}</div>
                </c:otherwise>
            </c:choose>
        </c:if>

        <c:if test="${activateMsg == 'success'}">
            <div class="banner success">Sprint activado correctamente.</div>
        </c:if>
        <c:if test="${activateMsg == 'fail'}">
            <div class="banner error">No fue posible activar el Sprint. Verifica que tenga al menos una tarea.</div>
        </c:if>

        <c:if test="${closeMsg == 'success'}">
            <div class="banner success">Sprint cerrado correctamente.</div>
        </c:if>
        <c:if test="${closeMsg == 'fail'}">
            <div class="banner error">No fue posible cerrar el Sprint. Verifica que todas las tareas estén terminadas.</div>
        </c:if>


        <section class="sprint-hero">
            <div class="sprint-hero-info">
                <div class="sprint-hero-title-row">
                    <h1 class="page-title"><c:out value="${sprint.name}" /></h1>
                    <span class="sprint-status ${statusClass}">${statusLabel}</span>
                </div>
            </div>


            <div class="sprint-actions">
                <a class="sprint-nav-btn"
                   href="${pageContext.request.contextPath}/workspace/backlog?sprintId=${sprint.sprintId}">
                    Ver Backlog
                </a>
                <a class="sprint-nav-btn" href="${pageContext.request.contextPath}/workspace/dashboard?sprintId=${sprint.sprintId}">
                    Ver Dashboard
                </a>

                <c:if test="${isCreator and sprint.status == 'CREATED'}">
                    <form class="sprint-status-form"
                          action="${pageContext.request.contextPath}/workspace/sprint/active"
                          method="post"
                          onsubmit="return confirm('¿Activar este Sprint? Una vez activo podrás mover tareas en el tablero.');">
                        <input type="hidden" name="sprintId" value="${sprint.sprintId}">
                        <button type="submit" class="sprint-nav-btn primary"
                                <c:if test="${not hasTasks}">disabled title="Agrega al menos una tarea al Sprint Backlog para poder activarlo"</c:if>>
                            Activar Sprint
                        </button>
                    </form>
                </c:if>

                <c:if test="${isCreator and sprint.status == 'ACTIVE'}">
                    <form class="sprint-status-form"
                          action="${pageContext.request.contextPath}/workspace/sprint/close"
                          method="post"
                          onsubmit="return confirm('¿Cerrar este Sprint? Solo podrás hacerlo si todas las tareas están terminadas, y esta acción no se puede deshacer.');">
                        <input type="hidden" name="sprintId" value="${sprint.sprintId}">
                        <button type="submit" class="sprint-nav-btn danger">
                            Cerrar Sprint
                        </button>
                    </form>
                </c:if>
            </div>
        </section>


        <section class="sprint-content">

            <div class="sprint-summary">
                <h2 class="section-title">Detalles del Sprint</h2>

                <div class="sprint-objective">
                    <span class="sprint-objective-label">Objetivo</span>
                    <p><c:out value="${sprint.goal}" /></p>
                </div>

                <div class="sprint-stats-grid">
                    <div class="stat-card">
                        <span class="stat-label">Inicio</span>
                        <span class="stat-value">${sprint.startDate}</span>
                    </div>
                    <div class="stat-card">
                        <span class="stat-label">Fin</span>
                        <span class="stat-value">${sprint.endDate}</span>
                    </div>
                    <div class="stat-card">
                        <span class="stat-label">Duración</span>
                        <span class="stat-value">${durationDays} días</span>
                    </div>
                    <div class="stat-card">
                        <span class="stat-label">Estado</span>
                        <span class="stat-value">${statusLabel}</span>
                    </div>
                </div>
            </div>


            <aside class="sprint-team">
                <div class="sprint-team-header">
                    <h2 class="section-title">Equipo</h2>
                    <c:if test="${isCreator}">
                        <button class="team-add-btn" onclick="openAddReaderForm()" title="Agregar lector">
                            +
                        </button>
                    </c:if>
                </div>

                <p class="team-subtitle">Creador</p>
                <div class="team-member creator">
                    <div class="team-avatar">
                        ${fn:toUpperCase(fn:substring(sprint.creator.userName, 0, 1))}
                    </div>
                    <div class="team-member-info">
                        <strong><c:out value="${sprint.creator.userName}" /></strong>
                        <span>Creador</span>
                    </div>
                </div>

                <p class="team-subtitle">Lectores</p>
                <c:choose>
                    <c:when test="${empty sprint.readers}">
                        <p class="team-empty">Todavía no hay lectores en este Sprint.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="reader" items="${sprint.readers}">
                            <div class="team-member">
                                <div class="team-avatar reader">
                                    ${fn:toUpperCase(fn:substring(reader.userName, 0, 1))}
                                </div>
                                <div class="team-member-info">
                                    <strong><c:out value="${reader.userName}" /></strong>
                                    <span>Lector</span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </aside>
        </section>
    </main>

    <c:if test="${isCreator}">

    <div class="addSprintMenu" id="addReaderMenu"
         data-context-path="${pageContext.request.contextPath}"
         data-existing-readers="${existingReadersCsv}">
        <div class="addSprintMenu-content">
            <h2>Agregar lector</h2>
            <form id="addReaderForm" action="${pageContext.request.contextPath}/workspace/addreader" method="post">
                <input type="hidden" name="sprintId" value="${sprint.sprintId}">
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
    </c:if>

    </c:otherwise>
    </c:choose>
    <script src="${pageContext.request.contextPath}/scripts/bannerMessage.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/sprint.js"></script>
</body>
</html>
