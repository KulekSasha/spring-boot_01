<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <c:set var="url">${pageContext.request.contextPath}</c:set>

    <title>Login</title>

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
            <div class="alert alert-danger">
                404 error.
            </div>
        </div>
    </div>
</div>

<!-- jQuery Version -->
<script src="${url}/webjars/jquery/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${url}/webjars/bootstrap/js/bootstrap.min.js"></script>

</body>
</html>
