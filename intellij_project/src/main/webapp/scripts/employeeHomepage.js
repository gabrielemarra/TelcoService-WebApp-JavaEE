$(document).ready(function () {

    $.ajaxSetup({cache: false});

    /**
     * All the buttons!
     */

    $("#addOptionalProductButton").click(
        function (event) {
            event.preventDefault();
            let name = $("#optionalProductNameId").val();
            let price = $("#optionalProductPriceId").val();

            addOption(name, price);
        }
    );

    $("#addServiceButton").click(
        function (event) {
            event.preventDefault();
            let planType = $("#fixed_phone:checked").val() || $("#mobile_phone:checked").val() || $("#fixed_internet:checked").val() || $("#mobile_internet:checked").val();
            let bp1 = $("#baseprice1Id").val();
            let bp2 = $("#baseprice2Id").val();
            let bp3 = $("#baseprice3Id").val();
            let gigIncl = $("#gigInclId").val();
            let smsIncl = $("#smsInclId").val();
            let minIncl = $("#minInclId").val();
            let gigExtra = $("#gigExtraId").val();
            let smsExtra = $("#smsExtraId").val();
            let minExtra = $("#minExtraId").val();

            addService(planType, bp1, bp2, bp3, gigIncl, smsIncl, minIncl, gigExtra, smsExtra, minExtra);
        }
    );

    $("#addServicePackageButton").click(
        function (event) {
            event.preventDefault();
            let name = $("#servicePackageNameId").val();
            let period = document.querySelectorAll('input[name=defaultValidity]:checked'); //
            let children = document.getElementById("id_packageSummary").childNodes;
            let services = [];
            let serviceQuantities = [];
            let optionQuantities = [];
            let options = [];
            for(let i = 0; i < children.length; i++) {
                if(children[i].id != null) {
                    let id = children[i].id.replace(/\D/g, '');
                    let quantity = children[i].value.toString();
                    if(children[i].id.includes("Service")) {
                        services.push(id);
                        serviceQuantities.push(quantity);
                    } else { // children[i].id.includes("Option")
                        options.push(id);
                        optionQuantities.push(quantity);
                    }
                }
            }
            addPackage(name, period[0].value, services, serviceQuantities, options, optionQuantities);
        }
    );

    /**
     * FUNCTION CALLS FOR THE ENTIRE DOCUMENT:
     */
    showAllOptions();
    showAllServices();

    /**
     * FUNCTIONS RELATED TO OPTIONAL PRODUCTS
     */

    /**
     * This function performs the POST request to add the new optional product to the DB
     * @param name: Name of the new optional product
     * @param price: Decimal number of the monthly price of the optional product
     */
    function addOption(name, price) {
        let postRequest = $.post("AddOption", {name: name, price: price});
        postRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            showOneOption(name, price, response.id);
        });
        postRequest.fail(function (data, textStatus, jqXHR) {
            alert("Adding new optional product POST fail");
        });
    };

    /**
     * This function performs the GET request to retrieve all the optional products in the DB. All the optional products
     * need to be displayed to the user.
     */
    function showAllOptions() {
        let getRequest = $.get("GetAllOptions");
        getRequest.done(function (data, textStatus, jqXHR) {
            let options = jqXHR.responseJSON;
            if (options.length > 0) {
                for (let i = 0; i < options.length; i++) {
                    showOneOption(options[i].name, options[i].price, options[i].option_id);
                }
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("Show all options GET fail!");
        });
    };

    /**
     * This functions appends a button checkbox in the list of optional products displayed to the user. This function
     * builds the HTML element to the document.
     * @param name: Name of the new optional product
     * @param price: Decimal price of the optional product
     */
    function showOneOption(name, price, option_id) {
        let ul = document.getElementById("id_allOptionsTiles");



        /*
        *
        *

        let li = document.createElement("li");
        li.className = "list-group-item list-group-item-action";
        li.setAttribute("aria-current", "true");
        li.id = "id_option" + option_id;
        li.setAttribute("name", "option");


        let rowMajor = document.createElement("div");
        rowMajor.className = "d-flex w-100 align-content-center row";
        let rowMinor = document.createElement("div");
        rowMinor.className = "d-flex w-100 align-content-center row";

        let majorTitle = document.createElement("div");
        majorTitle.className = "col-5";
        let majorParams = document.createElement("div"); //empty
        majorParams.className = "col-5"; // empty
        let majorBtns = document.createElement("div");
        majorBtns.className = "col-2";

        let minorMonth = document.createElement("div");
        minorMonth.className = "col-5";

        let title = document.createElement("p");
        title.className = "lead mb-1";
        title.appendChild(document.createTextNode(name));
        majorTitle.appendChild(title);

        let plusBtn = document.createElement("input");
        plusBtn = makeBtn("plus", "Opt", plusBtn, option_id);

        let quantity = document.createElement("small");
        quantity.className = "text-muted";
        quantity.id = "id_optionQuantity" + option_id.toString();
        //quantity.setAttribute("value", "0");
        quantity.appendChild(document.createTextNode("0"));
        quantity.type = "number";
        quantity.value = "0";

        let minusBtn = document.createElement("input");
        minusBtn = makeBtn("minus", "Opt", minusBtn, option_id);

        majorBtns.appendChild(plusBtn);
        majorBtns.appendChild(quantity);
        majorBtns.appendChild(minusBtn);

        let p2 = document.createElement("p");
        p2.className = "text-muted mb-1";
        p2.appendChild(document.createTextNode("$" + price + "/mo"));

        minorMonth.appendChild(p2);

        rowMajor.appendChild(majorTitle);
        rowMajor.appendChild(majorParams); // empty
        rowMajor.appendChild(majorBtns);

        rowMinor.appendChild(minorMonth);

        li.appendChild(rowMajor);
        li.appendChild(rowMinor);

        ul.appendChild(li);

        plusBtn.onclick = function () { // increment
            let quantityElement = this.parentElement.childNodes[1];
            let oldValue = quantityElement.value;
            let oldSum = parseInt(oldValue);
            quantityElement.removeChild(quantityElement.lastChild);
            let newSum = oldSum + 1;
            quantityElement.value = newSum;
            quantityElement.appendChild(document.createTextNode(newSum.toString()));
            addItemToSummary(this.parentElement.parentElement.parentElement, newSum, true, "Option");
        }

        minusBtn.onclick = function () { // decrement
            let quantityElement = this.parentElement.childNodes[1];
            let oldValue = quantityElement.value;
            let oldSum = parseInt(oldValue);
            quantityElement.removeChild(quantityElement.lastChild);
            let newSum = oldSum - 1;
            if (newSum < 0) {
                newSum = 0;
            }
            quantityElement.value = newSum;
            quantityElement.appendChild(document.createTextNode(newSum.toString()));
            addItemToSummary(this.parentElement.parentElement.parentElement, newSum, false, "Option");
        }
        */
    };

    /**
     * FUNCTIONS RELATED TO (SINGULAR) SERVICE
     */

    /**
     * This function performs the POST request to add the new service to the DB
     */
    function addService(planType, bp1, bp2, bp3, gigIncl, smsIncl, minIncl, gigExtra, smsExtra, minExtra) {
        let postRequest = $.post("AddService", {
            planType: planType, bp1: bp1, bp2: bp2, bp3: bp3, gigIncl: gigIncl, smsIncl: smsIncl, minIncl: minIncl,
            gigExtra: gigExtra, smsExtra: smsExtra, minExtra: minExtra});

        postRequest.done(function (data, textStatus, jqXHR) {
            let service_id = jqXHR.responseText.replace(/\D/g, '');
            showService(planType, bp1, bp2, bp3, service_id, gigIncl, smsIncl, minIncl, gigExtra, smsExtra, minExtra);
            let period = document.querySelector('input[name=defaultValidity]:checked').value;
            changeServiceLabel(service_id, period);
        });
        postRequest.fail(function (data, textStatus, jqXHR) {
            alert("Adding new service POST fail");
        });
    };

    /**
     * This function performs the GET request to retrieve all the services in the DB. All the services
     * need to be displayed to the user.
     */
    function showAllServices() {
        let getRequest = $.get("GetAllServices");
        getRequest.done(function (data, textStatus, jqXHR) {
            let services = jqXHR.responseJSON;
            if (services.length > 0) {
                for (let i = 0; i < services.length; i++) {
                    showService(services[i].planType, services[i].bp1, services[i].bp2, services[i].bp3, services[i].service_id, services[i].gigIncl, services[i].smsIncl, services[i].minIncl, services[i].gigExtra, services[i].smsExtra, services[i].minExtra);
                }
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
        });
    };

    /**
     * This functions appends a button checkbox in the list of services displayed to the user. This function
     * builds the HTML element to the document.
     *
     *
     */
    function showService(planType, bp1, bp2, bp3, service_id, gigIncl, smsIncl, minIncl, gigExtr, smsExtr, minExtr) {

        let tilesList = document.getElementById("id_allServicesTiles");
        let template = document.getElementById("id_service_tile_template");
        let clone = template.content.cloneNode(true);

        let pElements = clone.querySelectorAll("p");
        let smallElements = clone.querySelectorAll("small");

        clone.id = "id_service" + service_id;
        pElements[0].textContent = planType.replace('_', ' ');
        pElements[1].textContent = "€" + bp1 + "/mo for 12 months";//gigIncl.toString() + " GB";
        pElements[1].id = "id_defaultPeriod" + service_id;
        smallElements[0].textContent = "€" + bp2 + " - 24mo | €" + bp3 + " - 36mo";
        smallElements[0].id = "id_otherPeriods" + service_id;
        if (planType == "Mobile_Internet" | planType == "Fixed_Internet") { // gigIncl.toString() + " GB"
            pElements[2].textContent = gigIncl.toString() + " GB";
            smallElements[1].textContent = "€" + gigExtr.toString() + "/extra";

        } else if (planType == "Mobile_Phone") {
            pElements[2].textContent = minIncl.toString() + " min";
            smallElements[1].textContent = "€" + minExtr.toString() + "/extra";
            pElements[3].textContent = smsIncl.toString() + " sms";
            smallElements[2].textContent = "€" + smsIncl.toString() + "/extra";

        } else {
            // specify unlimited...?
            pElements[2].textContent = "Unlimited";
        }

        tilesList.appendChild(clone);

    };


    /**
     * This function performs the POST request to add the new service package to the DB
     * @param name: Name of the service package
     * @param period: The default validity period selected
     * @param listServices: Array of the service IDs to be offered with the new service package
     * @param listOptions: Array of the optional product IDs to be offered with the new service package.
     */
    function addPackage(name, period, serviceIds, serviceQuantities, optionIds, optionQuantities) {
        let postRequest = $.post("AddServicePackage", {
            name: name,
            period: period,
            serviceIds: serviceIds,
            serviceQuantities: serviceQuantities,
            optionIds: optionIds,
            optionQuantities: optionQuantities
        });
        postRequest.done(function (data, textStatus, jqXHR) {
            alert("Adding service package SUCCESS");
        });
        postRequest.fail(function (data, textStatus, jqXHR) {
            alert("adding service POST fail");
        });
    };

    /**
     * As user clicks on a different validity period, the price listed for each service changes to reflect the default
     * validity period selected.
     * @param id: ID of the service
     * @param period: the New validity period selected (1, 2, or 3)
     */

    function changeServiceLabel(id, period) {
        let getRequest = $.get("GetService", {id: id});
        getRequest.done(function (data, textStatus, jqXHR) {
            let service = jqXHR.responseJSON;
            let costs = [service.bp1, service.bp2, service.bp3];
            let defaultBasePrice = costs[period - 1];
            let periods = [1, 2, 3];
            periods.splice(period-1, 1);
            costs.splice(period-1, 1);
            let defaultElement = document.getElementById("id_defaultPeriod" + service.service_id);
            let otherElement = document.getElementById("id_otherPeriods" + service.service_id);

            defaultElement.removeChild(defaultElement.lastChild);
            otherElement.removeChild(otherElement.lastChild);

            defaultElement.appendChild(document.createTextNode("$" + defaultBasePrice + "/mo for " + period * 12 + " months"));
            otherElement.appendChild(document.createTextNode("$" + costs[0] + " - " + periods[0] * 12 + "mo | $" + costs[1] + " - " + periods[1] * 12 + "mo"));

        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("Houston we have a problemo");
        });
    };


    /**
     * FUNCTIONS TO DYNAMICALLY CHANGE CONTENT ACCORDING TO USER CLICKS
     */

    /**
     * The form for adding a service package changes depending on which plan type is selected.
     */
    $('input[name="planType"]').click(
        function () {
            let planSelected = $(this).val();
            $("div.selectDiv").hide();
            $("#show" + planSelected).show();
        }
    );

    $('input[name="defaultValidity"]').click(
        function () {
            let period = $(this).val();
            let services = document.getElementsByName("service")
            for (let i = 0; i < services.length; i++) {
                let id = services[i].getAttribute("id").replace(/\D/g, '');
                changeServiceLabel(id, period);
            }
            updatePricesInSummary(period);
            updateTotalInSummary();
        }
    );

    function addItemToSummary(element, quantity, increment, type) {
        let id = element.id.replace(/\D/g, '');
        let item_id = "id_item" + type + id;
        let getRequestText = "Get"+ type; //(type == "Option")? "GetOption" : "GetService";
        let getRequest = $.get(getRequestText, {id: id});
        getRequest.done(function (data, textStatus, jqXHR) {
            let resp = jqXHR.responseJSON;
            // To get the current validity period
            let period = document.querySelector('input[name=defaultValidity]:checked').value;
            let summaryList = document.getElementById("id_packageSummary");
            let name = (type == "Option")? resp.name : resp.type.replace("_", " ");

            if (quantity == 0) { // remove an existing line item, quantity will never equal 0 when increment == false
                let child = document.getElementById("id_item" + type + id);
                summaryList.removeChild(child);
            } else if (quantity == 1 & increment) { // create new lineItem
                let child = document.createElement("li");
                child.id = item_id;
                child.value = quantity;
                child.className = "list-group-item d-flex w-100 justify-content-between";
                let divName = document.createElement("div");
                divName.appendChild(document.createTextNode(quantity.toString() + " " + name));

                child.appendChild(divName);
                appendNewPriceInSummary(child, id, period, quantity, getRequestText);
                summaryList.appendChild(child);

            } else { // find existing lineItem
                let child = document.getElementById(item_id);
                child.value = quantity;
                child.removeChild(child.lastChild);// remove totalCost
                child.removeChild(child.lastChild); // remove name
                let divName = document.createElement("div");
                divName.appendChild(document.createTextNode(quantity.toString() + " " + name));
                child.appendChild(divName);
                appendNewPriceInSummary(child, id, period, quantity, getRequestText);
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("adding item to summary GET fail");
        });
    };


    // this only applies to services
    function updatePricesInSummary(newValidity) {
        let items = document.getElementById("id_packageSummary").childNodes;
        let itemIds = [];
        for (let i = 0; i < items.length; i++) {
            if (items[i].id != null) {
                // get only the service IDs
                itemIds.push((items[i].id).replace(/\D/g, ''));
            }
        }
        let total = 0;
        for (let i = 0; i < itemIds.length; i++) {
            let element = document.getElementById("id_itemService" + itemIds[i]);
            if(element != null) { // if element is in fact a service, the search by ID will find a match.
                element.removeChild(element.lastChild);
                appendNewPriceInSummary(element, itemIds[i], newValidity, element.value, "GetService");
            }
        }
    };

    /**
     *
     * @param element: The li element that requires two child nodes: and node for quantity + name
     * and a node for the total price
     * @param id: ID of the service package associated with the line item
     * @param validity: the validity period drives which price tier we select
     */
    function appendNewPriceInSummary(element, id, validity, quantity, request) {
        let newPrice = 0;
        let getRequest = $.get(request, {id: id});
        getRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            let costPer;
            if(request == "GetService") {
                let costs = [response.bp1, response.bp2, response.bp3];
                costPer = costs[validity - 1];
            } else {
                costPer = response.price;
            }
            element.appendChild(document.createTextNode("$" + quantity * costPer));
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert(request + " ID:" + id + "failed");
        });
    };

    function updateTotalInSummary() {
        let serviceItems = document.getElementById("id_packageSummary").childNodes;
        let total = 0;
        for(let i = 0; i < serviceItems.length; i++) {
            if(serviceItems[i] != null){
                console.log(serviceItems[i].textContent);
                //if((serviceItems[i].id).contains("Service")) { // document.getElementById("id_itemService2").lastChild.data
                //    let contributionToTotal = serviceItems[i].lastChild.textContent.replace(/\D/g, '');
                //    total = total + contributionToTotal;
                //}
            }
        }
        let totalElement = document.getElementById("id_summaryTotal");
        //totalElement.textContent = total;
        //totalElement.appendChild(document.createTextNode("Default Base Cost: $" + total.toString()));

    }

});