<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <c:set var="url">${pageContext.request.contextPath}</c:set>

    <title>Login</title>

    <link rel='stylesheet' href='${url}/webjars/bootstrap/css/bootstrap.min.css'>
    <link rel="icon" type="image/x-icon"
          href="${url}/resources/img/favicon.ico">

    <style>
        body {
            padding-top: 70px;
        }

        .btn-space {
            margin-right: 10px;
        }
    </style>

</head>
<body>

<!-- Page Content -->

<div class="container">
    <div class="row">
        <div class="col-lg-4 col-lg-offset-4 text-left">
            <form id="login-form" class="form-horizontal" action="${url}/login" method="post">
                <div class="form-group">
                    <label for="login">Login:</label>
                    <input type="text" class="form-control" id="login" name="login"> <br>
                </div>
                <div class="form-group">
                    <label for="pwd">Password:</label>
                    <input class="form-control" type="password" id="pwd" name="pwd"> <br>
                </div>
                <button type="submit" class="btn btn-default btn-space">Login</button>

                <a href="${url}/registration/new" class="btn btn-default btn-space"
                   role="button">Registration Form</a>
            </form>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger">
                    Invalid username and password.
                </div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="alert alert-success">
                    You have been logged out.
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- jQuery Version -->
<script src="${url}/webjars/jquery/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${url}/webjars/bootstrap/js/bootstrap.min.js"></script>

<script>
    $("#login-form").submit(function (event) {
        localStorage.setItem("currentLogin", $('#login').val());
        localStorage.setItem("currentPass", $('#pwd').val());
    });
</script>

</body>
</html>
