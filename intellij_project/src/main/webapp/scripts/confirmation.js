$(document).ready(function () {
    $.ajaxSetup({cache: false});

    showOrderInfo();
    showOptionsInfo();


    $("#loginButton").click(
        function (event) {
            event.preventDefault();
            let email = $("#loginEmailInput").val();
            let password = $("#loginPasswordInput").val();
            login(email, password);
        }
    );



    function showOrderInfo() {
        document.getElementById("id_start_date").textContent = sessionStorage.getItem('startDate');
        document.getElementById("id_validity_period").textContent = (parseInt(sessionStorage.getItem('validity_period')) * 12).toString();
        let packageId = sessionStorage.getItem('package_id');
        let getRequest = $.get("GetPackage", {package_id: packageId});

        getRequest.done(function (data, textStatus, jqXHR) {
            let package_info = jqXHR.responseJSON;
            document.getElementById("id_packageName").textContent = package_info[0].name
            let period = sessionStorage.getItem('validity_period');
            for (let i = 1; i < package_info.length; i++) {
                let type = package_info[i].type;
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
    }

    function showOptionsInfo() {
        let options = JSON.parse(sessionStorage.getItem('optionalProducts'));
        for(let i = 0; i < options.length; i++) {
            appendTable("id_cost_options_table", options[i].name, options[i].price);
        }
    }
})