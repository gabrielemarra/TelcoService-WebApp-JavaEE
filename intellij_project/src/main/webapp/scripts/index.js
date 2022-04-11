$(document).ready(function () {

    $.ajaxSetup({cache: false});

    //sessionStorage.setItem('isLoggedIn', "false");



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
            let username = $("#signupUsernameInput").val();
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
        sessionStorage.setItem('isLoggedIn', "false");
        sessionStorage.setItem('isEmployee', "false");


        let postRequest = $.post("Login", {email: email, password: password});

        postRequest.done(function (data, textStatus, jqXHR) {
            sessionStorage.setItem('isLoggedIn', 'true');

            //Here we save the data into the sessionStorage
            let user_data = jqXHR.responseJSON;
            sessionStorage.setItem('username', user_data.username);
            sessionStorage.setItem('email', user_data.email);
            sessionStorage.setItem('name', user_data.name);
            sessionStorage.setItem('employee', user_data.employee);

            //Redirect to the homepage (if user) or to employee page
            if ((sessionStorage.getItem("pendingOrder") == "true") & (sessionStorage.getItem("employee") == "false")) {
                window.location.href = window.location.href + "/confirmation.html";
            } else {
                window.location.href = window.location.href + jqXHR.responseJSON.new_url;
            }

        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            //Spawn an error message after now login
            let JSONText = jQuery.parseJSON(jqXHR.responseText)
            alert("Login Error: " + jqXHR.responseText);
        });
        // postRequest.always(function () {
        //     alert("Request completed");
        // });

    }

    function signup(email, name, username, password, password_confirmation) {
        if (password === password_confirmation) {

            let postRequest = $.post("Signup", {email: email, password: password, username:username, name:name});

            postRequest.done(function (data, textStatus, jqXHR) {
                //Spawn a success message after now login

            });
            postRequest.fail(function (jqXHR, textStatus, errorThrown) {
                //Spawn an error message after now login
                let JSONText = jQuery.parseJSON(jqXHR.responseText)
                alert("Login Error: " + jqXHR.responseText);
            });
            // postRequest.always(function () {
            //     alert("Request completed");
            // });

        } else {
            //PASSWORD DON'T MATCH!
        }
    }
});

function visitAsGuest (){
    sessionStorage.setItem('isLoggedIn', "false");
    sessionStorage.setItem('isEmployee', "false");

    window.location.href = "./" + "homepage.html";
}



