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
    <body>
        <section id="register-form-container">
            <form:form method="POST" action="register_user" modelAttribute="user">
                <form:input type='text' placeholder="username" path="username"/>
                <form:input placeholder="pass" type="password" path="password"/>
                <form:input type='text' placeholder="email" path="email"/>
                <input type='submit' value='submit'/>
            </form:form>
                <p>or..sign up with <a href="FacebookSignIn"><image src='images/icons/facebook12.png' /></a></p>
        </section>
    </body>
</html>
