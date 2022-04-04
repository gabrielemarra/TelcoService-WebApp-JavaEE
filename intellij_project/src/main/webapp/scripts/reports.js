$(document).ready(function () {

    $.ajaxSetup({cache: false});

    function manageTabs(){

        var triggerTabList = [].slice.call(document.querySelectorAll('#goodthings button'))
        triggerTabList.forEach(function (triggerEl) {
            var tabTrigger = new bootstrap.Tab(triggerEl)
            triggerEl.addEventListener('click', function (event) {
                event.preventDefault()
                tabTrigger.show()
            })
        })


        var triggerTabList = [].slice.call(document.querySelectorAll('#badthings button'))
        triggerTabList.forEach(function (triggerEl) {
            var tabTrigger = new bootstrap.Tab(triggerEl)
            triggerEl.addEventListener('click', function (event) {
                event.preventDefault()
                tabTrigger.show()
            })
        })

    }


    getServicePackageReport();
    getRejectedOrdersReport();
    getInsolventUsersReport();

    function getServicePackageReport() {
        let getRequest = $.get("GetServicePackageReport");
        getRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            let table = document.getElementById("id_package_report_body")
            let template = document.getElementById("id_package_report_template");
            for(let i = 0; i < response.length; i++) {
                let clone = template.content.cloneNode(true);
                let packageID = clone.querySelector("th");
                let packageInfo = clone.querySelectorAll("td"); // should be size 7
                packageID.textContent = response[i].package_id;
                packageInfo[0].textContent = response[i].package_name;
                packageInfo[1].textContent = response[i].purchases_total;
                packageInfo[2].textContent = response[i].purchases_period1;
                packageInfo[3].textContent = response[i].purchases_period2;
                packageInfo[4].textContent = response[i].purchases_period3;
                packageInfo[5].textContent = response[i].sales_base;
                packageInfo[6].textContent = response[i].sales_total;
                table.appendChild(clone);
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("fallen package");

        });
    };

    function getRejectedOrdersReport() {
        let getRequest = $.get("GetRejectedOrdersReport");
        getRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            let table = document.getElementById("id_rejected_orders_table_body")
            let template = document.getElementById("id_rejected_orders_template");
            for(let i = 0; i < response.length; i++) {
                let clone = template.content.cloneNode(true);
                let orderID = clone.querySelector("th");
                let orderInfo = clone.querySelectorAll("td");
                orderID.textContent = response[i].order_id;
                orderInfo[0].textContent = response[i].user_id;
                orderInfo[1].textContent = response[i].user_name;
                orderInfo[2].textContent = response[i].package_id;
                orderInfo[3].textContent = response[i].package_name;
                orderInfo[4].textContent = response[i].total;
                table.appendChild(clone);
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("fallen orders");

        });
    };

    function getInsolventUsersReport() {
        let getRequest = $.get("GetInsolventUsersReport");
        getRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            let table = document.getElementById("id_insolvent_users_table_body")
            let template = document.getElementById("id_insolvent_users_template");
            for(let i = 0; i < response.length; i++) {
                let clone = template.content.cloneNode(true);
                let userID = clone.querySelector("th");
                let userInfo = clone.querySelectorAll("td");
                userID.textContent = response[i].user_id;
                userInfo[0].textContent = response[i].user_name;
                userInfo[1].textContent = response[i].num_rej_orders;
                userInfo[2].textContent = response[i].delinquent_total;

                table.appendChild(clone);
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("fallen users");
        });
    };









});
