/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function checkFieldsLogin() {
    var username = $("#username").val().trim();
    var pass = $("#password").val().trim();
   
    if (username == "") {
        $("#username-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "block");
    } else {
        $("#username-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "block");
        $("#username-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
    }
    if (pass.length < 5) {
        $("#password-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "none");
        $("#password-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
        $("#password-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "block");
    } else {
        $("#password-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "block");
        $("#password-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "none");
        $("#password-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
    }
}
function checkValidLogin() {
    var user_status = $("#username-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display");
    var pass_status = $("#password-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display");

    if (user_status === "none" || pass_status === "none")
        alert("Please fill the required fields appropriately");
    else {
        
        var username = $("#username").val().trim();
        var password = $("#password").val().trim();
        
        $("#sign-loader").css("display","inline-block");
        $.ajax({
            type: "POST",
            url: "/FOU_1/login_user",
            data: {username: username, password: password},
            dataType: "text",
            async: false
        }).done(function(msg) {
            setTimeout(function (){
                document.location.href = "http://localhost:8080/FOU_1/my-drive";
            }, 1000);
        }).fail(function(err) {
            return false;
        });
        
    }
}
function checkValid() {
    var user_status = $("#username-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display");
    var pass_status = $("#password-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display");
    var email_status = $("#email-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display");

    if (user_status === "none" || pass_status === "none" || email_status === "none")
        alert("Please fill the required fields appropriately");
    else {
        
        var username = $("#username").val().trim();
        var password = $("#password").val().trim();
        var email = $("#email").val().trim();
        
        $("#sign-loader").css("display","inline-block");
        $.ajax({
            type: "POST",
            url: "/FOU_1/register_user",
            data: {username: username, password: password, email: email},
            dataType: "text",
            async: false
        }).done(function(msg) {
            setTimeout(function (){
                document.location.href = "http://localhost:8080/FOU_1/my-drive";
            }, 1000);
        }).fail(function(err) {
            return false;
        });
        
    }
}
function checkFields() {
    var username = $("#username").val().trim();
    var pass = $("#password").val().trim();
    var email = $("#email").val().trim();
    var result = checkUserExistence(username);
   
    if (result === "true") {
        $("#username-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "block");
    }
    if (result === "false") {
        $("#username-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "block");
        $("#username-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
    }
    if (pass.length < 5) {
        $("#password-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "none");
        $("#password-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
        $("#password-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "block");
    } else {
        $("#password-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "block");
        $("#password-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "none");
        $("#password-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
    }
    if (validateEmail(email) == false) {
        $("#email-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "none");
        $("#email-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
        $("#email-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "block");
    } else {
        $("#email-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "block");
        $("#email-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "none");
        $("#email-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
    }
     if (username === "") {
        $("#username-container [class='glyphicon glyphicon-ok form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-warning-sign form-control-feedback']").css("display", "none");
        $("#username-container [class='glyphicon glyphicon-remove form-control-feedback']").css("display", "block");
    }
}
function validateEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}
function checkUserExistence(username) {
    var ret;
    $.ajax({
        type: "GET",
        url: "/FOU_1/check-user-existence",
        data: {username: username},
        dataType: "text",
        async: false
    }).done(function(msg) {
        ret = msg;
    }).fail(function(err) {
        return false;
    });
    return ret;
}

