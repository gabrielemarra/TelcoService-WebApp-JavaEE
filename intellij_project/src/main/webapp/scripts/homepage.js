$(document).ready(function () {
    $.ajaxSetup({cache: false});

    if (sessionStorage.getItem("employee") === "true") {
        window.location.href = "Employee/index.html"
    }

    if (sessionStorage.getItem("isLoggedIn") === "true") {
        displayPersonalData();
        getRejectedOrders();
        showActivationRecords();
    } else {
        hideLoggedInFunction();
        displayLoginButton();
    }

    getServicePackages();


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
                    let template = document.getElementById("id_rejected_orders_template");
                    let clone = template.content.cloneNode(true);
                    let divs = clone.querySelectorAll("td");
                    divs[0].textContent = rejectedOrders[i].order_id;
                    divs[1].textContent = rejectedOrders[i].service_package_name;
                    divs[2].textContent = rejectedOrders[i].total_price;
                    let button = divs[3].querySelector("button");
                    // this dataset val may not be necessary since we can pass it as a param to the event listener directly
                    button.dataset.orderId = rejectedOrders[i].order_id;
                    button.id = "buyAgainButton";
                    //         input.addEventListener("click", function(){addItemToSummary(this, "OptionalProduct")});
                    button.addEventListener('click', function () {
                        attemptTransaction(rejectedOrders[i].order_id)
                    });
                    table.appendChild(clone);
                }
                $("#id_rejected_orders_table_row").prop("hidden", false);
            } else {
                $("#id_rejected_orders_table_row").prop("hidden", true);
                table.style.display = "none";
                document.getElementById("id_rejected_orders_table_title").style.display = "none";
            }
        });

        getResponse.fail(function (data, textStatus, errorThrown) {
            $("#id_rejected_orders_table_row").prop("hidden", true);
            table.style.display = "none";
            document.getElementById("id_rejected_orders_table_title").style.display = "none";
            console.log("Error while retrieving Rejected Orders");
        });
    }

    function attemptTransaction(order_id) {
        //alert("button clicked for order:" + order_id);
        let getRequest = $.get("GetOrderInfo", {order_id: order_id});
        getRequest.done(function (data, textStatus, jqXHR) {
            alert("success?");
            // add all the info to sesison and navigation to confirmation page
            let allInfo = jqXHR.responseJSON;
            sessionStorage.setItem('order_id', order_id);

            sessionStorage.setItem('package_id', allInfo[0].package_id);
            sessionStorage.setItem('validity_period', allInfo[0].validity_period);
            sessionStorage.setItem('total_cost', allInfo[0].total_cost);
            allInfo.splice(0, 1);
            sessionStorage.setItem('optionalProducts', JSON.stringify(allInfo));
            sessionStorage.setItem('startDate', allInfo[0].startDate);

            sessionStorage.setItem('existingOrder', "true");


            window.location.href = "confirmation.html";
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            //alert("fail?");
        });
    }

    function showActivationRecords() {
        let getRequest = $.get("GetAllOrders");

        getRequest.done(function (data, textStatus, jqXHR) {
            let allOrders = jqXHR.responseJSON;
            for (let i = 0; i < allOrders.length; i++) {
                let oneOrder = allOrders[i];
                //if(oneOrder.orderInfo.status == "CONFIRMED") {

                let template = document.getElementById("id_ActivationRecords");
                let clone = template.content.cloneNode(true);

                clone.querySelector(".schedule_name").textContent = oneOrder[0].package_name;
                clone.querySelector(".schedule_start_date").textContent = oneOrder[0].start_date;

                let start = new Date(oneOrder[0].start_date);
                let today = new Date();
                // this also manipulates start1
                let start1 = new Date(oneOrder[0].start_date);
                let end = new Date(start1.setMonth(start1.getMonth() + (12 * oneOrder[0].validity_period)));

                if (today < start) { // future
                    clone.querySelector(".schedule_card").className += " border-primary text-primary";
                } else if ((today > start) && (today < end)) { // current
                    clone.querySelector(".schedule_card").className += " border-success text-success";
                } else if (today > end) { // expired
                    clone.querySelector(".schedule_card").className += " border-danger text-danger";
                } else {
                    clone.querySelector(".schedule_card").className += " border-secondary text-secondary";
                }
                // border-primary, border-success, border-danger

                clone.querySelector(".schedule_end_date").textContent = end.toISOString().split('T')[0];

                let servicesUl = clone.querySelector(".schedule_services");
                for (let j = 0; j < oneOrder[1].length; j++) {
                    let newElement = document.createElement("li");
                    newElement.textContent = (oneOrder[1][j].type).replace("_", " ");
                    servicesUl.append(newElement);
                }
                let optionsUl = clone.querySelector(".schedule_optionsschedule_options");
                for (let j = 0; j < oneOrder[2].length; j++) {
                    let newElement = document.createElement("li");
                    newElement.textContent = (oneOrder[2][j].name);
                    optionsUl.append(newElement);
                }

                document.getElementById("id_activation_schedule_row").appendChild(clone);
            }

            $("#id_activation_schedule_row").prop("hidden", false);
        });

        getRequest.fail(function (data, textStatus, jqXHR) {
            $("#id_activation_schedule_row").prop("hidden", true);
        });


    }

    function hideLoggedInFunction() {
        $("#id_rejected_orders_table_row").prop("hidden", true);
        $("#id_activation_schedule_row").prop("hidden", true);
    }

    function displayLoginButton() {
        $("#username_right_corner").prop("hidden", true);
        $("#loginButtonRightCorner").prop("hidden", false);
    }


});

function packageSelectButtonPressed(buttonPressed) {
    window.location.href = "./" + "buyService.html" + "?package_id=" + buttonPressed.dataset.package_id
}

function loginButtonPressed() {
    window.location.href = "./";
}