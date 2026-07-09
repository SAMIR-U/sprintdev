<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.Sprint" %>

<%
    List<Sprint> sprints = (List<Sprint>) session.getAttribute("sprints");
    String sprintMessage = (String) session.getAttribute("createsprint"); //esto me sirve para crear un mensaje
    session.removeAttribute("createsprint"); //esto para borrarlo al recargar la pagina
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


    <script src="${pageContext.request.contextPath}/scripts/topbar.js"></script>
</body>
</html>