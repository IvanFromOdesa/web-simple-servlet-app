<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="login" scope="session" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
    <title>Main menu</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/styles.css"/>
</head>
<body>
<div class = "nav-bar">
    <nav>
        <h2 class="logo">Welcome, <c:out value="${login}"/>!</h2>
        <ul>
            <li><a href="<c:url value="/users"/>">All Users</a></li>
            <li><a href="<c:url value="/logout"/>">Logout</a></li>
        </ul>
    </nav>
</div>
</body>
</html>
