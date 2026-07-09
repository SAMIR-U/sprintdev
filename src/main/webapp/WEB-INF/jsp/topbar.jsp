<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.uptc.swi.sprintdev.domain.User" %>

<%
 User user = (User) session.getAttribute("user");
%>
<header class= "topbar">
 <div class= "app-logo-header">
            <img src = "${pageContext.request.contextPath}/image/logo_esprindev.png"
            alt = "SprintDevIcon"
            class = "app-logo-icon">
            <span class = "name-app">
            Sprint<em>Dev</em>
            </span>
        </div>
        <nav>
            <div class= "user-header" id= "userMenuToggle">
                <div class= "avatar">
                    <%
                        if(user != null){
                             out.print(user.getUserName().substring(0,1).toUpperCase());
                        }
                    %>
                </div>
                <div class = "user-text-header">
                     <strong class = "user-text">
                        <%= user != null ? user.getUserName() : "" %>
                     </strong>
                     <p>member</p>
                </div>
             <div class="user-dropdown" id="userDropdown">
                 <form action="${pageContext.request.contextPath}/logout" method="post">
                     <button type="submit" class="dropdown-item logout">
                         Cerrar sesión
                     </button>
                 </form>
             </div>
            </div>
        </nav>
</header>
