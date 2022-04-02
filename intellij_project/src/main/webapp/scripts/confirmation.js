$(document).ready(function () {
    $.ajaxSetup({cache: false});

    showOrderInfo();
    showOptionsInfo();
    //setStatus("PENDING");
    buttonFilter();

    $("#idBuyButton").click(
        function (event) {
            event.preventDefault();
            submitTransaction(false);

        }
    );

    $("#idBuyButtonFail").click(
        function (event) {
            event.preventDefault();
            submitTransaction(true);

        }
    );

    $("#idLoginRegButton").click(
        function (event) {
            event.preventDefault();

        }
    );


    function setStatus(status) {
        let orderId = sessionStorage.getItem("order_id");
        let postRequest = $.post("SetOrderStatus", {order_id: orderId, status: status});

        postRequest.done(function (data, textStatus, jqXHR) {
            alert("Order: " + orderId + " is " + status);
        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            alert("Order: " + orderId + " status not updated.");
        });

    }

    function submitTransaction(isOrderRejected) {
        let orderID = sessionStorage.getItem("order_id");
        let postRequest = $.post("Transact", {isOrderRejected: isOrderRejected, order_id: orderID});

        postRequest.done(function (data, textStatus, jqXHR) {
            alert("Transaction performed. Payment rejected? " + isOrderRejected);
        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            alert("Transaction failed");
        });
    }


    function showOrderInfo() {
        document.getElementById("id_start_date").textContent = sessionStorage.getItem('startDate');
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
                if(period == "1") {
                    cost = package_info[i].bp1;
                } else if (period == "2") {
                    cost = package_info[i].bp2;
                } else { // period == "3"
                    cost = package_info[i].bp3;
                }
                appendTable("id_cost_services_table", type, cost);
            }
        });
        getRequest.fail(function (jqXHR, textStatus, errorThrown) {
        });
    };

    function appendTable(tableId, name, cost) {
        let table = document.getElementById(tableId);
        let newRow = table.insertRow();
        newRow.insertCell().appendChild(document.createTextNode(name));
        newRow.insertCell().appendChild(document.createTextNode(cost));
    };

    function showOptionsInfo() {
        let options = JSON.parse(sessionStorage.getItem('optionalProducts'));
        if(options.length == 0) {
            document.getElementById("id_options_table2").style.display = "none";
            document.getElementById("id_monthly_options1").style.display = "none";
            document.getElementById("id_monthly_options2").style.display = "none";
        } else {
            for(let i = 0; i < options.length; i++) {
                appendTable("id_cost_options_table", options[i].name, options[i].price);
            }
        }
    };

    function buttonFilter() {
        let isLoggedIn = sessionStorage.getItem("isLoggedIn");

        if(isLoggedIn == "true") {
            document.getElementById("idLoginRegButton").style.display = "none";
        } else {
            document.getElementById("idBuyButton").style.display = "none";
            document.getElementById("idBuyButtonFail").style.display = "none";
        }
    };

    /*
    window.addEventListener('beforeunload', function (event) {
        event.preventDefault();
        setStatus("REJECTED");
    });
    * */


})