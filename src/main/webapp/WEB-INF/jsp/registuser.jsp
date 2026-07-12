<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="registMessage" value="${regist}" />
<c:remove var="regist" scope="session" />

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Registro | SprintDev</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/login.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/register.css">

</head>

<body>

<div class="auth-screen">

    <div class="auth-box">

        <div class="auth-logo">

        <img src="${pageContext.request.contextPath}/image/logo_esprindev.png" alt="Logo SprintDev" class="logo">


        </div>

        <h2 class="auth-title">Crear cuenta</h2>

        <p class="auth-sub">
            Registra un nuevo usuario para acceder a SprintDev.
        </p>

        <c:choose>
            <c:when test="${registMessage == 'fail'}">
                <div class="banner error">
                    No fue posible registrar el usuario.
                </div>
            </c:when>
            <c:when test="${registMessage == 'success'}">
                <div class="banner success">
                    Usuario registrado correctamente.
                </div>
            </c:when>
        </c:choose>

        <form action="${pageContext.request.contextPath}/registuser"
              method="post">

            <div class="field">

                <label for="user">Usuario</label>

                <input
                        type="text"
                        id="user"
                        name="user"
                        placeholder="Ingrese un usuario"
                        required>

            </div>

            <div class="field">

                <label for="password">Contraseña</label>

                <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Ingrese una contraseña"
                        required>

            </div>

            <button
                    type="submit"
                    class="btn btn-primary">

                Registrarme

            </button>

        </form>

        <div class="auth-footer">

            ¿Ya tienes una cuenta?

            <a href="${pageContext.request.contextPath}/login">

                Iniciar sesión

            </a>

        </div>

    </div>

</div>
  <script src="${pageContext.request.contextPath}/scripts/bannerMessage.js"></script>
</body>

</html>
