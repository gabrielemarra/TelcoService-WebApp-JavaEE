$(document).ready(function () {
    $.ajaxSetup({cache: false});

    if (sessionStorage.getItem("employee") === true) {
        window.location.href = "Employee/index.html"
    }

    displayPersonalData();

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
        let title = clone.querySelector("h4");
        let baseCost = clone.querySelector("span");

        title.textContent = servicePackageInfo.package_name;
        title.id = servicePackageInfo.package_id;

        baseCost.textContent = servicePackageInfo.price3;

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
