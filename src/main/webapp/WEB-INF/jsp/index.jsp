<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>TaskSprint · Bienvenido</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Poppins:wght@600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/index.css">
</head>
<body class="landing">

    <div class="page">

       <header class="hero">

           <div class="hero-glow">
               <span class="g-blue"></span>
               <span class="g-green"></span>
               <span class="g-orange"></span>
           </div>

           <div class="logo-big-wrap">
               <img src="${pageContext.request.contextPath}/image/logo_esprindev.png" alt="Logo SprintDev" class="logo-img">
           </div>

           <h1 class="brand-name">
               Sprint<em>Dev</em>
           </h1>

           <p class="tagline">
               Organiza tus sprints, tareas y equipos en un solo lugar, sin complicaciones.
           </p>

       </header>

       <div class="hero-divider"></div>

       <section class="about-section">

           <span class="eyebrow">Nuestra esencia</span>

           <h2>¿Quiénes somos?</h2>

           <p>
               Somos una plataforma pensada para equipos que quieren planificar,
               dar seguimiento y cerrar sus sprints con claridad. Nacimos para
               simplificar la gestión de proyectos y ayudar a cada equipo a
               enfocarse en lo que realmente importa: entregar valor.
           </p>

       </section>

       <div class="auth-actions">

           <button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/login'">
               <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/><path d="M10 17l5-5-5-5"/><path d="M15 12H3"/></svg>
               Iniciar sesión
           </button>

           <button type="button" class="btn btn-outline" onclick="location.href='${pageContext.request.contextPath}/registuser'">
               <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M19 8v6"/><path d="M22 11h-6"/></svg>
               Registrarse
           </button>

       </div>
       </body>
       </html>
