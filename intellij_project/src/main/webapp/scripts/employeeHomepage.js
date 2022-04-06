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
            let options = [];
            for(let i = 0; i < children.length; i++) {
                if(children[i].id != null) {
                    let id = children[i].id.replace(/\D/g, '');
                    if(children[i].id.includes("Service")) {
                        services.push(id);
                    } else { // children[i].id.includes("Option")
                        options.push(id);
                    }
                }
            }
            addPackage(name, period[0].value, services, options);
        }
    );

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
            updatePricesInSummary();
            updateTotalInSummary();
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
        let postRequest = $.post("AddOptionalProduct", {name: name, price: price});
        postRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            showOneOption(name, price, response.id);
        });
        postRequest.fail(function (data, textStatus, jqXHR) {
            //alert("Adding new optional product POST fail");
        });
    };

    /**
     * This function performs the GET request to retrieve all the optional products in the DB. All the optional products
     * need to be displayed to the user.
     */
    function showAllOptions() {
        let getRequest = $.get("GetAllOptionalProducts");
        getRequest.done(function (data, textStatus, jqXHR) {
            let options = jqXHR.responseJSON;
            if (options.length > 0) {
                for (let i = 0; i < options.length; i++) {
                    showOneOption(options[i].name, options[i].price, options[i].option_id);
                }
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            //alert("Show all options GET fail!");
        });
    };

    /**
     * This functions appends a button checkbox in the list of optional products displayed to the user. This function
     * builds the HTML element to the document.
     * @param name: Name of the new optional product
     * @param price: Decimal price of the optional product
     */
    function showOneOption(name, price, option_id) {
        let tileList = document.getElementById("id_allOptionsTiles");
        let template = document.getElementById("id_service_tile_template");
        let clone = template.content.cloneNode(true);
        let pElements = clone.querySelectorAll("p");
        pElements[0].textContent = name;
        pElements[1].textContent = "€" + price + "/mo";

        let input = clone.querySelector("input");
        let label = clone.querySelector("label");
        input.id = "id_option" + option_id;
        input.name = "option";
        input.dataset.price = price
        input.addEventListener("click", function(){addItemToSummary(this, "OptionalProduct")});
        label.setAttribute("for", "id_option" + option_id);
        tileList.appendChild(clone);
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
            //alert("Adding new service POST fail");
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
            pElements[2].textContent = "Unlimited";
        }
        let input = clone.querySelector("input")
        let label = clone.querySelector("label");
        input.id = "id_service" + service_id;
        input.name = "service";
        input.addEventListener("click", function(){addItemToSummary(this, "Service")});
        input.dataset.bp1 = bp1;
        input.dataset.bp2 = bp2;
        input.dataset.bp3 = bp3;

        label.setAttribute("for", "id_service" + service_id);
        tilesList.appendChild(clone);
    };


    /**
     * This function performs the POST request to add the new service package to the DB
     * @param name: Name of the service package
     * @param period: The default validity period selected
     * @param listServices: Array of the service IDs to be offered with the new service package
     * @param listOptions: Array of the optional product IDs to be offered with the new service package.
     */
    function addPackage(name, period, serviceIds, optionIds) {
        let postRequest = $.post("AddServicePackage", {name: name, period: period, serviceIds: serviceIds, optionIds: optionIds});
        postRequest.done(function (data, textStatus, jqXHR) {
            //alert("Adding service package SUCCESS");
        });
        postRequest.fail(function (data, textStatus, jqXHR) {
            //alert("adding service POST fail");
        });
    };

    /**
     * As user clicks on a different validity period, the price listed for each service changes to reflect the default
     * validity period selected.
     * @param id: ID of the service
     * @param period: the New validity period selected (1, 2, or 3)
     */

    function changeServiceLabel(id, period) {
        let element = document.getElementById("id_service" + id);
        let prices = priceBasedOnPeriod([element.dataset.bp1, element.dataset.bp2, element.dataset.bp3]);
        let defaultElement = document.getElementById("id_defaultPeriod" + id);
        let otherElement = document.getElementById("id_otherPeriods" + id);
        defaultElement.textContent = "€" + prices.defaultPrice + "/mo for " + prices.defaultPeriod * 12 + " months";
        otherElement.textContent = "€" + prices.otherPrice1 + " - " + prices.otherPeriod1 * 12 + "mo | €" + prices.otherPrice2 + " - " + prices.otherPeriod2 * 12 + "mo";
    };



    function priceBasedOnPeriod(costs) {
        let period = document.querySelector('input[name=defaultValidity]:checked').value;
        let defaultBasePrice = costs[period - 1];
        let periods = [1, 2, 3];
        periods.splice(period-1, 1);
        costs.splice(period-1, 1);
        const prices = {
            defaultPeriod : period,
            defaultPrice : defaultBasePrice,
            otherPrice1 : costs[0],
            otherPeriod1 : periods[0],
            otherPrice2 : costs[1],
            otherPeriod2 : period[1]
        }
        return prices;
    }

    function addItemToSummary(element, type) {
        let id = element.id.replace(/\D/g, '');
        let item_id = "id_item" + type + id;
        let getRequestText = "Get"+ type; //(type == "Option")? "GetOptionalProduct" : "GetService";
        let getRequest = $.get(getRequestText, {id: id});
        getRequest.done(function (data, textStatus, jqXHR) {
            let resp = jqXHR.responseJSON;
            let summaryList = document.getElementById("id_packageSummary");
            if(element.checked) {
                let clone = document.getElementById("id_summary_item_template").content.cloneNode(true);
                clone.querySelector("li").id = item_id;
                let divs = clone.querySelectorAll("div");
                divs[0].textContent = (type == "OptionalProduct")? resp.name : resp.type.replace("_", " ");
                divs[1].textContent = (type == "OptionalProduct")? resp.price: (priceBasedOnPeriod([resp.bp1, resp.bp2, resp.bp3]).defaultPrice);
                summaryList.appendChild(clone);
            } else { // element is not checked: delete from summary
                let toRemove = document.getElementById(item_id);
                summaryList.removeChild(toRemove);
            }
            updateTotalInSummary();
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            //alert("adding item to summary GET fail");
        });
    };


    // this only applies to services
    function updatePricesInSummary() {
        let items = document.getElementById("id_packageSummary").childNodes;
        // Get all the IDs of only the services listed in the summary
        for (let i = 0; i < items.length; i++) {
            if (items[i].id != null) {
                let id = items[i].id.replace(/\D/g, '');
                let element = document.getElementById("id_itemService" + id); // get only the service IDs
                if(element != null) { // if element is in fact a service, the search by ID will find a match.
                    let info = document.getElementById("id_service" + id);
                    element.querySelector("div.price").textContent = priceBasedOnPeriod([info.dataset.bp1, info.dataset.bp2, info.dataset.bp3]).defaultPrice;
                }
            }
        }
    };

    function updateTotalInSummary() {
        let cartPrices = document.getElementById("id_packageSummary").querySelectorAll("div.price");
        let total = 0.0;
        for (let i = 0; i < cartPrices.length; i++) {
            if(cartPrices[i] != null) {
                total = total + parseFloat(cartPrices[i].textContent);
            }
        }
        document.getElementById("id_summaryTotal").textContent = "€" + total.toString();
    }
});