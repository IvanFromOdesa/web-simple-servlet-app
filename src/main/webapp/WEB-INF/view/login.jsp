<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sign In</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/styles.css"/>
</head>
<body>
<div class = "center">
    <h1>Please, sign in!</h1>
    <form method="post" action="${pageContext.request.contextPath}/main">
        <div class="txt_field">
            <input type="text" required name="login">
            <span></span>
            <label>Login</label>
        </div>
        <div class="txt_field">
            <input type="password" required name="password">
            <span></span>
            <label>Password</label>
        </div>
        <div class = "error-message">
            <%--@elvariable id="errorMessage" type="java.lang.String"--%>
            <c:out value="${empty errorMessage ? '' : errorMessage}" />
        </div>
        <input class="button" type="submit" value="Login">
    </form>
</div>
</body>
</html>
