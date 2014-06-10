<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE >
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>${title}</title>
    </head>

    <link rel="stylesheet" type="text/css" href="<c:url value="/bootstrap3/css/bootstrap.css" />" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css" />" />
    <script src="<c:url value = "/js/dynamicProcess.js"/> ">
    </script>
    <script src="<c:url value = "/js/jquery.js"/> "></script>
    <script src="<c:url value = "/js/bootstrap.min.js"/> "></script>
    <script src="<c:url value = "/js/form-validation.js"/> "></script>
    
    <body>
        <section id="register-form-container">
            <div class="form-group has-success has-feedback" id="username-container">
                <input type="text" placeholder="username" name="username" id="username" class="form-control" required onkeyup="checkFieldsLogin();"/>
                <span class="glyphicon glyphicon-ok form-control-feedback" style="margin-top:-25px;display:none;"></span>
                <span class="glyphicon glyphicon-warning-sign form-control-feedback" style="margin-top:-25px;"></span>
                <span class="glyphicon glyphicon-remove form-control-feedback" style="margin-top:-25px;display:none;"></span>
            </div>
            <div class="form-group has-success has-feedback" id="password-container">
                <input placeholder="pass" type="password" name="password" class="form-control" id="password" required onkeyup="checkFieldsLogin();"/>
                <span class="glyphicon glyphicon-ok form-control-feedback" style="margin-top:-25px;display:none;"></span>
                <span class="glyphicon glyphicon-warning-sign form-control-feedback" style="margin-top:-25px;"></span>
                <span class="glyphicon glyphicon-remove form-control-feedback" style="margin-top:-25px;display:none;"></span>
            </div>
            <input type="button" value='sign in' class="btn btn-default" onclick="checkValidLogin()" style="margin-left:100px;"/>
            <a href="FacebookSignIn">
                <input type="button" value='facebook' class="btn btn-default" style="margin-left:10px;"/>
            </a>
            <div id="sign-loader">
                <h6 style="color:#fff;position:absolute;margin-top:39px;margin-left:127px;">signing in</h6>
                <div class="spinner" style="margin-top:30px;position:absolute;margin-left:185px;">
                    <div class="rect1"></div>
                    <div class="rect2"></div>
                    <div class="rect3"></div>
                    <div class="rect4"></div>
                    <div class="rect5"></div>
                </div>
            </div>
        </section>
    </body>
</html>
