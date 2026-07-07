<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
String loginMessage = (String) session.getAttribute("loginmessage");
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Iniciar sesión | SprintDev</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Inter:wght@400;500;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/login.css">

</head>

<body>

<div class="auth-screen">

    <div class="auth-box">

        <div class="auth-logo">

            <svg width="30" height="30" viewBox="0 0 100 100">

                <circle cx="50" cy="50" r="48" fill="#2E3A46"/>

                <path d="M50 20 A30 30 0 0 1 76 65"
                      fill="none"
                      stroke="#5B7C99"
                      stroke-width="9"
                      stroke-linecap="round"/>

                <path d="M76 65 A30 30 0 0 1 30 78"
                      fill="none"
                      stroke="#7A9B76"
                      stroke-width="9"
                      stroke-linecap="round"/>

                <path d="M30 78 A30 30 0 0 1 50 20"
                      fill="none"
                      stroke="#C08552"
                      stroke-width="9"
                      stroke-linecap="round"/>

            </svg>

            <span>
                Sprint<em>Dev</em>
            </span>

        </div>

        <h2 class="auth-title">Iniciar sesión</h2>

        <p class="auth-sub">
            Accede a tu cuenta
        </p>

        <% if ("fail".equals(loginMessage)) { %>

            <div class="banner error">
                Usuario o contraseña incorrectos.
            </div>

        <% } else if ("success".equals(loginMessage)) { %>

            <div class="banner success">
                Inicio de sesión exitoso.
            </div>

        <% }

        session.removeAttribute("loginmessage");
        %>

        <form action="${pageContext.request.contextPath}/login"
              method="post">

            <div class="field">

                <label for="user">
                    Usuario
                </label>

                <input
                        type="text"
                        id="user"
                        name="user"
                        placeholder="Ingrese su usuario"
                        required>

            </div>

            <div class="field">

                <label for="password">
                    Contraseña
                </label>

                <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Ingrese su contraseña"
                        required>

            </div>

            <button
                    class="btn btn-primary"
                    type="submit">

                Iniciar sesión

            </button>

        </form>

        <div class="auth-footer">

            ¿No tienes una cuenta?

            <a href="${pageContext.request.contextPath}/regist">

                Regístrate

            </a>

        </div>

    </div>

</div>

</body>

</html>