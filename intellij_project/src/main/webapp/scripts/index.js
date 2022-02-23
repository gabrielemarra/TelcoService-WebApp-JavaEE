window.addEventListener('load', (event) => {
    $("#loginButton").click(
        function () {
            let email = $("#loginEmailInput").val();
            let password = $("#loginPasswordInput").val();
            login(email, password);
        }
    )
    $("#signupButton").click(
        function () {
            let email = $("#signupEmailInput").val();
            let password = $("#signupPasswordInput").val();
            let password_confirmation = $("#signupPasswordConfirmation").val();
            signup(email, password, password_confirmation);
        }
    )
});

/**
 * This function make the AJAX call to the servlet Login and let the user logging in.
 *
 * @param email
 * @param password
 */
function login(email, password) {
    // alert("Your email is: "+email+" and your password is: "+password)
    let postRequest = $.post("Login", {email: email, password: password})

    postRequest.done(function (){
        alert("Login successful")
        }
    )

}

function signup(email, password, password_confirmation) {
    // alert("Your email is: "+email+" and your password is: "+password+" - "+password_confirmation)
}