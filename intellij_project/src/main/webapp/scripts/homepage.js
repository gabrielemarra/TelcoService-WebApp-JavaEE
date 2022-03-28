$(document).ready(function () {
    $.ajaxSetup({cache: false});

    if (sessionStorage.getItem("employee") === true) {
        window.location.href = "Employee/index.html"
    }

    displayPersonalData();
    getServicePackages();
    getRejectedOrders();

    $("#selectButton").click(
        function (event) {
            event.preventDefault();
            let startDate = $("#startDate").val();
            let user = $("#userID").val();
            let package = $("#servicePackage").val();
            let options = $("#optionalProducts").val();
            //submitNewOrder(startDate, userID, servicePackage, optionalProducts);

            /*
            *     public Order insertOrder(LocalDate subscriptionStartDate, User user, ServicePackage servicePackage, List<OptionalProduct> optionalProductList) {

            * */
        }
    );


    getServicePackages();
    getRejectedOrders();


    function displayPersonalData() {
        //    Should we make a request? For now we use the stored values
        let personalInfoString = sessionStorage.getItem("name") + " | " + sessionStorage.getItem("email")
        $("#username_right_corner").html(personalInfoString)

    }

    function getServicePackages() {
        let packages = $.get("GetAvailableServicePackages");
        packages.done(function (data, textStatus, jqXHR) {
            displayServicePackages(jqXHR.responseJSON);
        });
        packages.fail(function (jqXHR, textStatus, errorThrown) {
            throw "UnableToRetrieveServicePackages"
        });
    }

    function displayServicePackages(servicePackageList) {
        for (let key in servicePackageList) {
            displayOneServicePackage(servicePackageList[key])
        }
    }

    function displayOneServicePackage(servicePackageInfo) {
        let servicePackageRow = document.getElementById("servicePackageRow");
        let template = document.getElementById("package_card_template");

        let clone = template.content.cloneNode(true);
        let titleNode = clone.querySelector("h4");
        let costNode = clone.querySelector("span");
        let validityPeriodNode = clone.querySelector(".validityPeriod");
        let servicesListNode = clone.querySelector(".servicesList")
        let buttonSelectNode = clone.querySelector("button")

        titleNode.textContent = servicePackageInfo.package_name;

        //the package id is stored inside this custom attribute
        buttonSelectNode.dataset.package_id = servicePackageInfo.package_id

        let defaulValidityPeriod = servicePackageInfo.default_validity_period;
        costNode.textContent = servicePackageInfo.prices[defaulValidityPeriod - 1];

        let displayedValidityPeriod = defaulValidityPeriod * 12;
        validityPeriodNode.textContent = String(displayedValidityPeriod);

        let includedServices = servicePackageInfo.services;

        for (let includedServicesKey in includedServices) {
            let li = document.createElement('li');

            let currentService = includedServices[includedServicesKey];
            let serviceType = currentService.serviceType;
            li.textContent = serviceType.replace("_", " ");

            servicesListNode.appendChild(li);
        }

        servicePackageRow.appendChild(clone)
    }


    function getRejectedOrders() {
        let getResponse = $.get("GetRejectedOrders");

        getResponse.done(function (data, textStatus, jqXHR) {
            let table = document.getElementById("id_rejected_orders_body");
            let rejectedOrders = jqXHR.responseJSON;
            if (rejectedOrders.length > 0) {

                for (let i = 0; i < rejectedOrders.length; i++) {
                    let row = table.insertRow();

                    let orderIdCell = row.insertCell(0);
                    orderIdCell.innerHTML = rejectedOrders[i].order_id;

                    let servicePackageNameCell = row.insertCell(1);
                    servicePackageNameCell.innerHTML = rejectedOrders[i].service_package_name;

                    let totalPriceCell = row.insertCell(2);
                    totalPriceCell.innerHTML = rejectedOrders[i].total_price;
                }
            } else {
                document.getElementById("id_rejected_orders").style.display = "none";
                document.getElementById("id_rejected_orders_table_title").style.display = "none";
            }
        });

        getResponse.fail(function (data, textStatus, errorThrown) {
            alert("world");
        });
    }
});
