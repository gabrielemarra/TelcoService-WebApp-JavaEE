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




    function showOrderInfo() {
        let validity_period = parseInt(sessionStorage.getItem('validity_period')) * 12;
        let total_cost = parseFloat(sessionStorage.getItem('total_cost')) * validity_period;
        let startDate = sessionStorage.getItem('startDate');
        let packageId = sessionStorage.getItem('package_id');


        let getRequest = $.get("GetPackage", {package_id: email, password: password});

        postRequest.done(function (data, textStatus, jqXHR) {

            //Here we save the data into the sessionStorage
            let package_info = jqXHR.responseJSON;
            document.getElementById("id_packageName").textContent = package_info[0].name
            for (let i = 0; i < package_info.length; i++) {
                package_info[i]
            }

        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            //Spawn an error message after now login
            let JSONText = jQuery.parseJSON(jqXHR.responseText)
            alert("Login Error: " + jqXHR.responseText);
        });



    };


    })