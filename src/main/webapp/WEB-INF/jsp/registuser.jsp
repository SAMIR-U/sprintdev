<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
String registMessage = (String) session.getAttribute("regist");
%>

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

        <% if ("fail".equals(registMessage)) { %>

            <div class="banner error">
                No fue posible registrar el usuario.
            </div>

        <% } else if ("success".equals(registMessage)) { %>

            <div class="banner success">
                Usuario registrado correctamente.
            </div>

        <% }

        session.removeAttribute("registmessage");
        %>

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

</body>

</html>