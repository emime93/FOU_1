<c:if test="${empty username}">
<section id="register-form-container">
    <form:form method="POST" action="login_user" modelAttribute="loginUser">
        <form:input type='text' placeholder="username" path="username"/>
        <form:input type='password' placeholder="pass" path="password"/>
        <input type="submit" value="login" />        
    </form:form>
    <p>or..login with<image src='images/icons/facebook12.png' /></p>
</section>
</c:if>
