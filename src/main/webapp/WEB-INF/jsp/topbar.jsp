<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="user" value="${sessionScope.user}" />

<header class= "topbar">
        <a href = "${pageContext.request.contextPath}/workspace"
        class = "home-link">
             <div class= "app-logo-header">
                <img src = "${pageContext.request.contextPath}/image/logo_esprindev.png"
                 alt = "SprintDevIcon"
                class = "app-logo-icon">
                <span class = "name-app">
                    Sprint<em>Dev</em>
                </span>
        </div>
        </a>
        <nav>
            <div class= "user-header" id= "userMenuToggle">
                <div class= "avatar">
                    <c:if test="${not empty user}">${fn:toUpperCase(fn:substring(user.userName, 0, 1))}</c:if>
                </div>
                <div class = "user-text-header">
                     <strong class = "user-text">
                        <c:out value="${not empty user ? user.userName : ''}" />
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
