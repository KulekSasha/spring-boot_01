<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <c:set var="url">${pageContext.request.contextPath}</c:set>

    <title>User</title>

    <link href="${url}/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="${url}/resources/img/favicon.ico">

    <style>
        body {
            padding-top: 70px;
        }
    </style>

</head>
<body>

<!-- Page Content -->

<div class="container">

    <div class="row">
        <div class="col-lg-4 col-lg-offset-4 text-left">
            <h1>
                <p class="text-center">Hello, <c:out value="${user.firstName}"/>!</p>
            </h1>
            </br>

            <h4><p class="text-center">Click <a href="${url}/logout">here</a> to logout</p></h4>
        </div>
    </div>
</div>

<!-- jQuery Version -->
<script src="${url}/webjars/jquery/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${url}/webjars/bootstrap/js/bootstrap.min.js"></script>

</body>
</html>