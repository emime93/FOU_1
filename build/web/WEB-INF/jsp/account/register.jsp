<section id="register-form-container">
    <form:form method="POST" action="register_user" modelAttribute="user">
        <form:input type='text' placeholder="username" path="username"/>
        <form:input placeholder="pass" type="password" path="password"/>
        <form:input type='text' placeholder="email" path="email"/>
        <input type='submit' value='submit'/>
    </form:form>
    <p>or..sign up with <image src='images/icons/facebook12.png' /></p>
</section>