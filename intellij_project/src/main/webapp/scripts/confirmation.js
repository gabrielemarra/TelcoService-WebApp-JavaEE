$(document).ready(function () {
    $.ajaxSetup({cache: false});

    buttonFilter();
    showOrderInfo();
    showOptionsInfo();

    $("#idBuyButton").click(
        function (event) {
            event.preventDefault();
            insertNewOrder(false);
        }
    );

    $("#idBuyButtonFail").click(
        function (event) {
            event.preventDefault();
            insertNewOrder(true);
        }
    );

    $("#idLoginRegButton").click(
        function (event) {
            event.preventDefault();
            // TODO: how to properly navigate to login page?
            window.location.href = window.location.href.replace("confirmation.html", "");
            sessionStorage.setItem("pendingOrder", "true");
        }
    );

    function insertNewOrder(isOrderRejected) {
        let package_id = sessionStorage.getItem('package_id');
        let validity_period = sessionStorage.getItem('validity_period');
        let total_cost = sessionStorage.getItem('total_cost');
        let optionalProducts = JSON.parse(sessionStorage.getItem('optionalProducts'));
        let startDate = JSON.parse(sessionStorage.getItem('startDate'));


        let postRequest = $.post("PlaceNewOrder", {
            email: sessionStorage.getItem("email",),
            package_id: package_id,
            validity_period: validity_period,
            total_cost: total_cost,
            optionalProducts: optionalProducts,
            start_date: startDate,
            is_order_rejected: isOrderRejected
        });

        postRequest.done(function (data, textStatus, jqXHR) {
            let order_id = jqXHR.responseJSON.order_id;
            let order_status = jqXHR.responseJSON.order_status;


            if (order_status === "CONFIRMED") {
                displaySuccesssAlert("Your order has been placed correctly, you will be redirected to the homepage");
            } else if (order_status === "REJECTED") {
                displayWarningAlert("Your order has been placed but the payment process has been interrupted, please retry from the homepage");
            }

            window.setTimeout(function () {
                window.location.href = "./homepage.html";
            }, 5000);


            //window.location.href = "homepage.html"; //             window.location.href = "confirmation.html";

        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            //alert("Transaction failed");
            //            sessionStorage.setItem("pendingOrder", "false?");
        });

    }

    function showOrderInfo() {
        document.getElementById("id_start_date").textContent = JSON.parse(sessionStorage.getItem('startDate'));
        document.getElementById("id_validity_period").textContent = (parseInt(sessionStorage.getItem('validity_period')) * 12).toString() + " months";
        let packageId = sessionStorage.getItem('package_id');
        let getRequest = $.get("GetPackage", {package_id: packageId});

        getRequest.done(function (data, textStatus, jqXHR) {
            let package_info = jqXHR.responseJSON;
            document.getElementById("id_packageName").textContent = package_info[0].name;
            let period = sessionStorage.getItem('validity_period');
            for (let i = 1; i < package_info.length; i++) {
                let type = package_info[i].type.replace("_", " ");
                let cost;
                if (period === "1") {
                    cost = package_info[i].bp1;
                } else if (period === "2") {
                    cost = package_info[i].bp2;
                } else { // period == "3"
                    cost = package_info[i].bp3;
                }
                appendTable("id_cost_services_table", type, cost);
            }
            writeTotal("id_cost_services_table");
        });
        getRequest.fail(function (jqXHR, textStatus, errorThrown) {
        });
    }

    function writeTotal(tableId) {
        let total = document.getElementById(tableId).getAttribute("value");
        if (tableId === "id_cost_services_table") {
            document.getElementById("id_monthly_services").textContent = "€" + total.toString();
        } else {
            document.getElementById("id_monthly_options2").textContent = "€" + total.toString();
        }
        grandTotal(tableId);
    }

    function appendTable(tableId, name, cost) {
        let table = document.getElementById(tableId);
        table.setAttribute("value", parseInt(table.getAttribute("value")) + cost);
        let newRow = table.insertRow();
        newRow.insertCell().appendChild(document.createTextNode(name));
        newRow.insertCell().appendChild(document.createTextNode(cost));
    }

    function showOptionsInfo() {
        let options = JSON.parse(sessionStorage.getItem('optionalProducts'));
        if (options.length === 0) {
            document.getElementById("id_options_table2").style.display = "none";
            document.getElementById("id_monthly_options1").style.display = "none";
            document.getElementById("id_monthly_options2").style.display = "none";
        } else {
            for (let i = 0; i < options.length; i++) {
                appendTable("id_cost_options_table", options[i].name, options[i].price);
            }
            writeTotal("id_cost_options_table");
        }
    }

    function buttonFilter() {
        let isLoggedIn = sessionStorage.getItem("isLoggedIn");
        if (isLoggedIn === "true") {
            document.getElementById("idLoginRegButton").style.display = "none";
            displayPersonalData();
        } else {
            document.getElementById("idBuyButton").style.display = "none";
            document.getElementById("idBuyButtonFail").style.display = "none";
            displayLoginButton();
        }
    }

    function grandTotal(id) {
        let monthly = parseInt(document.getElementById(id).getAttribute("value"));
        let current = parseInt(document.getElementById("id_grand_total").getAttribute("value"));
        current = current + (monthly * 12 * parseInt(sessionStorage.getItem("validity_period")));
        document.getElementById("id_grand_total").setAttribute("value", current.toString());
        document.getElementById("id_grand_total").textContent = "€" + current.toString();
    }

    function displayLoginButton() {
        $("#username_right_corner").prop("hidden", true);
        $("#loginButtonRightCorner").prop("hidden", false);
    }

    function displayPersonalData() {
        //    Should we make a request? For now we use the stored values
        let personalInfoString = sessionStorage.getItem("name") + " | " + sessionStorage.getItem("email")
        $("#username_right_corner").html(personalInfoString)
    }

    function displayWarningAlert(textToDisplay) {
        $("#success_alert").prop("hidden", true);
        $("#danger_alert_text").text(textToDisplay);
        $("#danger_alert").prop("hidden", false);
    }

    function displaySuccesssAlert(textToDisplay) {
        $("#danger_alert").prop("hidden", true);
        $("#success_alert_text").text(textToDisplay);
        $("#success_alert").prop("hidden", false);
    }
})

function loginButtonPressed() {
    window.location.href = "./";
}