<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.User" %>

<%
    User user = (User) session.getAttribute("user");
%>

<aside class="sidebar">

    <div>

        <div class="sidebar-logo">

            <img
                src="${pageContext.request.contextPath}/image/logo_esprindev.jpeg"
                alt="SprintDev"
                class="sidebar-logo-img">

            <span>
                Sprint<em>Dev</em>
            </span>

        </div>

        <nav class="sidebar-menu">

            <a href="${pageContext.request.contextPath}/mainmenu" class="active">

                <span>Mis Sprints</span>

            </a>

            <a href="#">

                <span>Sprint Backlog</span>

            </a>

            <a href="#">

                <span>Tablero Scrum</span>

            </a>

            <a href="#">

                <span>Lectores</span>

            </a>

        </nav>

    </div>

    <div class="sidebar-footer">

        <div class="sidebar-user">

            <div class="avatar">

                <%
                    if (user != null && user.getUserName() != null && !user.getUserName().isEmpty()) {
                        out.print(user.getUserName().substring(0,1).toUpperCase());
                    } else {
                        out.print("U");
                    }
                %>

            </div>

            <div>

                <strong>

                    <%= (user != null) ? user.getUserName() : "Usuario" %>

                </strong>

                <p>

                    Miembro

                </p>

            </div>

        </div>

        <form action="${pageContext.request.contextPath}/logout"
              method="post">

            <button type="submit" class="logout-btn">

                Cerrar sesión

            </button>

        </form>

    </div>

</aside>