$(document).ready(function () {
    $.ajaxSetup({cache: false});

    $("#loginButton").click(
        function (event) {
            event.preventDefault();
            let email = $("#loginEmailInput").val();
            let password = $("#loginPasswordInput").val();
            login(email, password);
        }
    );

    $("#signupButton").click(
        function (event) {
            event.preventDefault();
            let email = $("#signupEmailInput").val();
            let username = $("#signupUsernameInputInput").val();
            let name = $("#signupNameInput").val();
            let password = $("#signupPasswordInput").val();
            let password_confirmation = $("#signupPasswordConfirmation").val();
            signup(email, name, username, password, password_confirmation);
        }
    );

    /**
     * This function make the AJAX call to the servlet Login and let the user logging in.
     *
     * @param email
     * @param password
     */
    function login(email, password) {
        let postRequest = $.post("Login", {email: email, password: password});

        postRequest.done(function (data, textStatus, jqXHR) {

            //Here we save the data into the sessionStorage
            let user_data = jqXHR.responseJSON;
            sessionStorage.setItem('username', user_data.username);
            sessionStorage.setItem('email', user_data.email);
            sessionStorage.setItem('name', user_data.name);
            sessionStorage.setItem('employee', user_data.employee);


            //Redirect to the homepage (if user) or to employee page
            window.location.href = window.location.href + jqXHR.responseJSON.new_url;

        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            alert("Login Error");
        });
        // postRequest.always(function () {
        //     alert("Request completed");
        // });

    }

    function signup(email, password, password_confirmation) {
        if (password === password_confirmation) {

            let postRequest = $.post("Signup", {email: email, password: password});
        } else {
            //PASSWORD DON'T MATCH!
        }
    }
});




