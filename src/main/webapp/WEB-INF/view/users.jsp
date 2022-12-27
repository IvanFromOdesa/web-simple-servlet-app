<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="usersList" scope="request" type="java.util.List"/>
<!DOCTYPE html>
<html>
<head>
    <title>Listed Users</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/styles.css"/>
</head>
<body>
<h2 class="c-title">Present Users</h2>
<table class="c-table">
    <thead>
    <tr>
        <th>Name</th>
        <th>Login</th>
        <th>Role</th>
    </tr>
    </thead>
    <c:forEach var="user" items="${usersList}">
        <tr>
            <td>${user.getName()}</td>
            <td>${user.getLogin()}</td>
            <td>${user.getRole()}</td>
        </tr>
    </c:forEach>
</table>
<h2 class="c-title"><a href="<c:url value="/main"/>">Back to Main Menu</a></h2>
</body>
</html>
