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

        if(email == null | email == "" | password == null | password == "") {
            if(email == null | email == "") {
                userInvalidFeedback(document.getElementById("loginEmailInput"));
            } else {
                userValidFeedback(document.getElementById("loginEmailInput"));
            }
            if(password == null | password == "") {
                userInvalidFeedback(document.getElementById("loginPasswordInput"));
            } else {
                userValidFeedback(document.getElementById("loginPasswordInput"));
            }
        } else {
            userValidFeedback(document.getElementById("loginEmailInput"))
            userValidFeedback(document.getElementById("loginPasswordInput"));
            userValidFeedback(document.getElementById("loginTitle"))

            let postRequest = $.post("Login", {email: email, password: password});

            postRequest.done(function (data, textStatus, jqXHR) {
                sessionStorage.setItem('isLoggedIn', 'true');

                //Here we save the data into the sessionStorage
                let user_data = jqXHR.responseJSON;
                // sessionStorage.setItem('username', user_data.username);
                sessionStorage.setItem('email', user_data.email);
                sessionStorage.setItem('name', user_data.name);
                sessionStorage.setItem('isEmployee', user_data.employee);

                //Redirect to the homepage (if user) or to employee page
                if ((sessionStorage.getItem("pendingOrder") === "true") && (sessionStorage.getItem("isEmployee") === "false")) {
                    window.location.href = "./" + "confirmation.html";
                } else {
                    window.location.href = "./" + jqXHR.responseJSON.new_url;
                }

            });
            postRequest.fail(function (jqXHR, textStatus, errorThrown) {
                //Spawn an error message after now login
                //let JSONText = jQuery.parseJSON(jqXHR.responseText)
                //alert("Login Error: " + jqXHR.responseText);
                userInvalidFeedback(document.getElementById("loginTitle"))

            });

        }



        // postRequest.always(function () {
        //     alert("Request completed");
        // });

    }

    function userInvalidFeedback(element) {
        element.style.borderColor = "red";
        element.nextElementSibling.style = "visibility: show";
    }

    function userValidFeedback(element) {
        element.style.borderColor = "gray";
        element.nextElementSibling.style = "visibility: hidden";
    }


    function signup(email, name, username, password, password_confirmation) {

        if(email == "" | email == null | name == "" | name == null | username == "" | username == null | password == "" | password == null | password_confirmation == "" | password_confirmation == null) {
            if(email == "" | email == null) {
                userInvalidFeedback(document.getElementById("signupEmailInput"));
            } else {
                userValidFeedback(document.getElementById("signupEmailInput"));
            }
            if(name == "" | name == null) {
                userInvalidFeedback(document.getElementById("signupNameInput"));
            } else {
                userValidFeedback(document.getElementById("signupNameInput"));
            }
            if(username == "" | username == null) {
                userInvalidFeedback(document.getElementById("signupUsernameInput"));
            } else {
                userValidFeedback(document.getElementById("signupUsernameInput"));
            }
            if(password == "" | password == null) {
                userInvalidFeedback(document.getElementById("signupPasswordInput"));
            } else {
                userValidFeedback(document.getElementById("signupPasswordInput"));
            }
            if(password_confirmation == "" | password_confirmation == null) {
                userInvalidFeedback(document.getElementById("signupPasswordConfirmation"));
            } else {
                userValidFeedback(document.getElementById("signupPasswordConfirmation"));
            }

        } else if (password === password_confirmation) {
            userValidFeedback(document.getElementById("signupNameInput"));
            userValidFeedback(document.getElementById("signupUsernameInput"));
            userValidFeedback(document.getElementById("signupEmailInput"));
            userValidFeedback(document.getElementById("signupPasswordInput"));
            userValidFeedback(document.getElementById("signupPasswordConfirmation"));

            let postRequest = $.post("Signup", {email: email, password: password, username: username, name: name});

            postRequest.done(function (data, textStatus, jqXHR) {
                //Spawn a success message after now login
                document.getElementById("id_registration_form").reset();
            });
            postRequest.fail(function (jqXHR, textStatus, errorThrown) {
                //Spawn an error message after now login
                //let JSONText = jQuery.parseJSON(jqXHR.responseText)
                //alert("Login Error: " + jqXHR.responseText);
                userInvalidFeedback(document.getElementById("signupButton"));
            });

        } else {
            userInvalidFeedback(document.getElementById("signupPasswordConfirmation"));
        }
    }
});

function visitAsGuest() {
    sessionStorage.setItem('isLoggedIn', "false");
    sessionStorage.setItem('isEmployee', "false");

    window.location.href = "./" + "homepage.html";
}



