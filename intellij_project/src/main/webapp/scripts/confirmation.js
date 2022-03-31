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
        let packageId = sessionStorage.getItem('package_id');
        let validity_period = sessionStorage.getItem('validity_period');
        let total_cost = sessionStorage.getItem('total_cost');
        let optionalProducts = JSON.parse(sessionStorage.getItem('optionalProducts'));
        let startDate = sessionStorage.getItem('startDate');
    };


    })