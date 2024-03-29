$(document).ready(function () {
    $.ajaxSetup({cache: false});

    if (sessionStorage.getItem("isEmployee") === "true") {
        window.location.href = "./employee/homepage.html"
    }

    buttonFilter();
    showOrderInfo();
    showOptionsInfo();
    checkDateValidity();

    function checkDateValidity() {
        const _MS_PER_DAY = 1000 * 60 * 60 * 24;
        let start_date = new Date(JSON.parse(sessionStorage.getItem('startDate')));
        let today = new Date()
        let utc_start_date = Date.UTC(start_date.getFullYear(), start_date.getMonth(), start_date.getDate());
        let utc_today = Date.UTC(today.getFullYear(), today.getMonth(), today.getDate());

        let diff = Math.floor((utc_start_date - utc_today) / _MS_PER_DAY);
        if (diff < 0) {
            $("#date_picker_modal").modal('show');
            $("#startDate").prop('min', new Date().toISOString().split('T')[0]);
            return false;
        } else {
            return true;
        }
    }

    $("#idBuyButton").click(
        function (event) {
            event.preventDefault();
            if (checkDateValidity()) {
                let isExistingOrder = sessionStorage.getItem("existingOrder");
                if (isExistingOrder === "false") {
                    insertNewOrder(false);
                } else {
                    submitTransaction(false);
                }
            }
        }
    );

    $("#date_picker_modal_confirm_button").click(function () {
        let startingDateSelected = new Date(Date.parse($("#startDate").val()));
        sessionStorage.setItem('startDate', JSON.stringify( startingDateSelected))
        $("#date_picker_modal").modal('hide');
    })

    function submitTransaction(isOrderRejected) {
        let orderID = sessionStorage.getItem("order_id");
        let startDate = JSON.parse(sessionStorage.getItem('startDate')).split("T")[0];

        let postRequest = $.post("Transact", {isOrderRejected: isOrderRejected, order_id: orderID, start_date: startDate,});
        postRequest.done(function (data, textStatus, jqXHR) {
            let order_id = jqXHR.responseJSON.order_id;
            let order_status = jqXHR.responseJSON.order_status;
            sessionStorage.setItem("pendingOrder", "false");

            if (order_status === "CONFIRMED") {
                displaySuccesssAlert("Your order has been placed correctly, you will be redirected to the <a href='./homepage.html' class='alert-link'>homepage</a><span>  </span><div class='spinner-border text-success' style='width: 1rem; height: 1rem;' role='status'>\n" +
                    "  <span class='visually-hidden'>Loading...</span>\n" +
                    "</div>");
            } else if (order_status === "REJECTED") {
                displayWarningAlert("The payment process has been interrupted again, please retry from the <a href='./homepage.html' class='alert-link'>homepage</a> <span>  </span><div class='spinner-border text-danger' style='width: 1rem; height: 1rem;' role='status'>\n" +
                    "  <span class='visually-hidden'>Loading...</span>\n" +
                    "</div>");
            }

            window.setTimeout(function () {
                window.location.href = "./homepage.html";
            }, 3000);
        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            //alert("Transaction failed");
        });
    }

    $("#idBuyButtonFail").click(
        function (event) {
            event.preventDefault();
            if (checkDateValidity()) {
                let isExistingOrder = sessionStorage.getItem("existingOrder");
                if (isExistingOrder === "false") {
                    insertNewOrder(true);
                } else {
                    submitTransaction(true);
                }
            }
        }
    );

    $("#idLoginRegButton").click(
        function (event) {
            event.preventDefault();
            // TODO: how to properly navigate to login page?
            sessionStorage.setItem("pendingOrder", "true");
            window.location.href = "./";
        }
    );

    function insertNewOrder(isOrderRejected) {
        let package_id = sessionStorage.getItem('package_id');
        let validity_period = sessionStorage.getItem('validity_period');
        let optionalProducts = JSON.parse(sessionStorage.getItem('optionalProducts'));
        let startDate = JSON.parse(sessionStorage.getItem('startDate')).split("T")[0];

        let optionalProductsIDs = [];

        for (let optionalProductsKey in optionalProducts) {
            optionalProductsIDs.push(optionalProducts[optionalProductsKey].option_id);
        }


        let postRequest = $.post("PlaceNewOrder", {
            email: sessionStorage.getItem("email",),
            package_id: package_id,
            validity_period: validity_period,
            optionalProducts: JSON.stringify(optionalProductsIDs),
            start_date: startDate,
            is_order_rejected: isOrderRejected
        });

        postRequest.done(function (data, textStatus, jqXHR) {
            let order_id = jqXHR.responseJSON.order_id;
            let order_status = jqXHR.responseJSON.order_status;
            sessionStorage.setItem("pendingOrder", "false");

            if (order_status === "CONFIRMED") {
                displaySuccesssAlert("Your order has been placed correctly, you will be redirected to the <a href='./homepage.html' class='alert-link'>homepage</a><span>  </span><div class='spinner-border text-success' style='width: 1rem; height: 1rem;' role='status'>\n" +
                    "  <span class='visually-hidden'>Loading...</span>\n" +
                    "</div>");
            } else if (order_status === "REJECTED") {
                displayWarningAlert("Your order has been placed but the payment process has been interrupted, please retry from the <a href='./homepage.html' class='alert-link'>homepage</a> <span>  </span><div class='spinner-border text-danger' style='width: 1rem; height: 1rem;' role='status'>\n" +
                    "  <span class='visually-hidden'>Loading...</span>\n" +
                    "</div>");
            }

            window.setTimeout(function () {
                window.location.href = "./homepage.html";
            }, 3000);

        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            //alert("Transaction failed");
            //            sessionStorage.setItem("pendingOrder", "false?");
        });

    }

    function showOrderInfo() {
        document.getElementById("id_start_date").textContent = JSON.parse(sessionStorage.getItem('startDate')).split("T")[0];
        document.getElementById("id_validity_period").textContent = (parseInt(sessionStorage.getItem('validity_period')) * 12).toString() + " months";
        let packageId = sessionStorage.getItem('package_id');
        let getRequest = $.get("GetPackage", {package_id: packageId});

        getRequest.done(function (data, textStatus, jqXHR) {
            let package_info = jqXHR.responseJSON;
            document.getElementById("id_packageName").textContent = package_info[0].name;
            let period = sessionStorage.getItem('validity_period');
            // id_start_date
            // document.getElementById("id_start_date").textContent = sessionStorage.getItem('startDate').split("T")[0].replace(/["]/g, "");

            for (let i = 1; i < package_info.length; i++) {
                let type = package_info[i].type.replace("_", " ");
                let cost;
                if (period === "1") {
                    cost = parseFloat(package_info[i].bp1);
                } else if (period === "2") {
                    cost = parseFloat(package_info[i].bp2);
                } else { // period == "3"
                    cost = parseFloat(package_info[i].bp3);
                }
                appendTable("id_cost_services_table", type, cost);
            }
            writeTotal("id_cost_services_table");
        });
        getRequest.fail(function (jqXHR, textStatus, errorThrown) {
        });
    }

    function writeTotal(tableId) {
        let total = parseFloat(document.getElementById(tableId).getAttribute("value"));
        if (tableId === "id_cost_services_table") {
            document.getElementById("id_monthly_services").textContent = "€" + total.toString();
        } else {
            document.getElementById("id_monthly_options2").textContent = "€" + total.toString();
        }
        grandTotal(tableId);
    }

    function appendTable(tableId, name, cost) {
        let table = document.getElementById(tableId);
        table.setAttribute("value", parseFloat(table.getAttribute("value")) + parseFloat(cost));
        let newRow = table.insertRow();
        newRow.insertCell().appendChild(document.createTextNode(name));
        newRow.insertCell().appendChild(document.createTextNode(cost));
    }

    function showOptionsInfo() {
        let items = sessionStorage.getItem('optionalProducts');
        if (items == null | items === "" | items.length === 0) {
            document.getElementById("id_options_table2").style.display = "none";
            document.getElementById("id_monthly_options1").style.display = "none";
            document.getElementById("id_monthly_options2").style.display = "none";
        } else {
            let options = JSON.parse(items);
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
        let monthly = parseFloat(document.getElementById(id).getAttribute("value"));
        let current = parseFloat(document.getElementById("id_grand_total").getAttribute("value"));
        current = current + (monthly * 12 * parseFloat(sessionStorage.getItem("validity_period")));
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
        $("#danger_alert_text").html(textToDisplay);
        $("#danger_alert").prop("hidden", false);
    }

    function displaySuccesssAlert(textToDisplay) {
        $("#danger_alert").prop("hidden", true);
        $("#success_alert_text").html(textToDisplay);
        $("#success_alert").prop("hidden", false);
    }
})

function loginButtonPressed() {
    window.location.href = "./";
}