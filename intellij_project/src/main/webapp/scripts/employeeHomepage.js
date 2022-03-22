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

           addOptionalProduct(name, price);
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


    $("#addServicePackageButton").click (
        function (event) {
            event.preventDefault();
            let name = $("#servicePackageNameId").val();
            let period = document.querySelectorAll('input[name=defaultValidity]:checked'); //
            let listServices = Array.from(document.querySelectorAll('input[name=service]:checked'));
            let listOptions = Array.from(document.querySelectorAll('input[name=option]:checked'));
            let services = new Array();
            for(let i=0; i< listServices.length; i++) {
                // remove "id_checkboxService" from the id string (example: id_checkboxService937)
                let id = listServices[i].getAttribute("id").replace(/\D/g, '');

                services.push(id);
            }

            let options = new Array();
            for(let i=0; i< listOptions.length; i++) {
                // remove "id_checkboxOption" from the id string (example: id_checkboxOption28)
                let id = listOptions[i].getAttribute("id").replace(/\D/g, '');
                options.push(id);
            }

            addPackage(name, period[0].value, services, options);
        }
    );

    /**
     * FUNCTION CALLS FOR THE ENTIRE DOCUMENT:
     */
    showAllOptionalProducts();
    showAllServices();

    /**
     * FUNCTIONS RELATED TO OPTIONAL PRODUCTS
     */

    /**
     * This function performs the POST request to add the new optional product to the DB
     * @param name: Name of the new optional product
     * @param price: Decimal number of the monthly price of the optional product
     */
   function addOptionalProduct(name, price) {
       let postRequest = $.post("AddOptionalProduct", {name: name, price: price});
       postRequest.done(function (data, textStatus, jqXHR) {
           //alert("optional product POST success");
           showOptionalProduct(name, price);
       });
       postRequest.fail(function (data, textStatus, jqXHR) {
           alert("optional product POST fail");
       });

   };

    /**
     * This function performs the GET request to retrieve all the optional products in the DB. All the optional products
     * need to be displayed to the user.
     */
   function showAllOptionalProducts() {
       let getRequest = $.get("GetAllOptionalProducts");
       getRequest.done(function (data, textStatus, jqXHR) {
           let options = jqXHR.responseJSON;
           if(options.length > 0) {
               for(let i = 0; i < options.length; i++) {
                   showOptionalProduct(options[i].name, options[i].price, options[i].option_id);
               }
           }
       });
       getRequest.fail(function (data, textStatus, jqXHR) {
           //alert("show opt prod fail!");
       });
   };

    /**
     * This functions appends a button checkbox in the list of optional products displayed to the user. This function
     * builds the HTML element to the document.
     * @param name: Name of the new optional product
     * @param price: Decimal price of the optional product
     */
    function showOptionalProduct(name, price, option_id) {
        let ul = document.getElementById("id_allOptionsTiles");

        let li = document.createElement("li");
        li.className = "list-group-item list-group-item-action";
        li.setAttribute("aria-current", "true");

        let divOuter1 = document.createElement("div");
        divOuter1.className = "d-flex w-100 justify-content-between";

        let p = document.createElement("p");
        p.className = "lead mb-1";
        p.appendChild(document.createTextNode(name));

        let divInner = document.createElement("div");

        let plusBtn = document.createElement("button");
        plusBtn.className = "btn btn-sm btn-default btn-circle cart-actions";
        plusBtn.type = "button";

        plusBtn.appendChild(document.createTextNode("+"));

        let id = "id_serviceQuantity" + option_id;

        let quantity = document.createElement("small");
        quantity.className = "text-muted";
        quantity.id = id;
        quantity.type = "number";
        quantity.value = "0";

        let minusBtn = document.createElement("button");
        minusBtn.className = "btn btn-sm btn-default btn-circle cart-actions";
        minusBtn.type = "button";
        minusBtn.appendChild(document.createTextNode("-"));

        divInner.appendChild(plusBtn);
        divInner.appendChild(quantity);
        divInner.appendChild(minusBtn);

        divOuter1.appendChild(p);
        divOuter1.appendChild(divInner);

        let divOuter2 = document.createElement("div");

        let p2 = document.createElement("p");
        p2.className = "text-muted mb-1";
        p2.appendChild(document.createTextNode("$" + price + "/mo"));

        divOuter2.appendChild(p2);

        li.appendChild(divOuter1);
        li.appendChild(divOuter2);

        ul.appendChild(li);

   };

    /**
     * FUNCTIONS RELATED TO (SINGULAR) SERVICE
     */

    /**
     * This function performs the POST request to add the new service to the DB
     * @param planType
     * @param bp1
     * @param bp2
     * @param bp3
     * @param gigIncl
     * @param smsIncl
     * @param minIncl
     * @param gigExtra
     * @param smsExtra
     * @param minExtra
     */
   function addService(planType, bp1, bp2, bp3, gigIncl, smsIncl, minIncl, gigExtra, smsExtra, minExtra) {
       let postRequest = $.post("AddService", {planType: planType, bp1:bp1, bp2:bp2, bp3:bp3, gigIncl: gigIncl, smsIncl: smsIncl, minIncl:minIncl, gigExtra: gigExtra, smsExtra: smsExtra, minExtra:minExtra});
       postRequest.done(function (data, textStatus, jqXHR) {
           let service_id = jqXHR.responseText.replace(/\D/g, '');

           //showService(planType, bp1, bp2, bp3, service_id, gigIncl, smsIncl, minIncl, gigExtr, smsExtr, minExtr) {
           showService(planType, bp1, bp2, bp3,service_id, gigIncl, smsIncl, minIncl, gigExtra, smsExtra, minExtra);
       });
       postRequest.fail(function (data, textStatus, jqXHR) {
           alert("adding service POST fail");
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
            if(services.length > 0) {
                for(let i = 0; i < services.length; i++) {
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

        let ul = document.getElementById("id_allServicesTiles");

        let li = document.createElement("li");
        li.className = "list-group-item list-group-item-action";
        li.setAttribute("name", "service");
        li.id = "id_service" + service_id.toString();
        li.setAttribute("aria-current", "true");

        /////////////////////////// Outline of the guts ////////////////////////////
        let rowMajor = document.createElement("div");
        rowMajor.className = "d-flex w-100 align-content-center row";

        let majorBtns = document.createElement("div");
        let majorTitle = document.createElement("div");
        let majorParams = document.createElement("div");
        majorBtns.className = "col-2";
        majorParams.className = "col-5";
        majorTitle.className = "col-5";

        let rowMinor = document.createElement("div");
        rowMinor.className = "d-flex w-100 align-content-center row";

        let minorMonth = document.createElement("div");
        let minorExtra = document.createElement("div");
        minorMonth.className = "col-5";
        minorExtra.className = "col-5";

        ////////////////////// Getting into each of the cols ///////////////////////

        // Filling out majorTitle <div class="col-5">
        let title = document.createElement("p");
        title.className = "lead mb-1";
        title.appendChild(document.createTextNode(planType.replace('_', ' ')));
        majorTitle.appendChild(title);

        // Filling out majorParams <div class="col-5">
        let majorParamsRow = document.createElement("div");
        majorParamsRow.className = "row";

        let majorParamsCol1 = document.createElement("div");
        majorParamsCol1.className = "col scrollLabelDiv";
        let param1 = document.createElement("p");
        param1.className = "lead text-muted";
        let majorParamsCol2 = document.createElement("div");
        majorParamsCol2.className = "col d-flex";
        let param2 = document.createElement("p");
        param2.className = "lead text-muted";

        // Filling out minorExtra <div class="col-5">
        let minorExtraRow = document.createElement("div");
        minorExtraRow.className = "row";

        let minorExtraCol1 = document.createElement("div");
        let minorExtraCol2 = document.createElement("div");

        minorExtraCol1.className = "col scrollLabelDiv";
        let extra1 = document.createElement("small");
        extra1.className = "fw-lighter text-muted";


        minorExtraCol2.className = "col d-flex";
        let extra2 = document.createElement("small");
        extra2.className = "fw-lighter text-muted";

        // Filling out params by plan type
        if(planType == "Mobile_Internet" | planType == "Fixed_Internet") {
            param1.appendChild(document.createTextNode(gigIncl.toString() + " GB"));
            extra1.appendChild(document.createTextNode("$"+ gigExtr.toString() + "/extra GB"));
        } else if (planType == "Mobile_Phone") {


            param1.appendChild(document.createTextNode(minIncl.toString() + " min"));
            param2.appendChild(document.createTextNode(smsIncl.toString() + " sms"));

            extra1.appendChild(document.createTextNode("$" + minExtr.toString() + "/extra min"));
            extra2.appendChild(document.createTextNode("$" + smsExtr.toString() + "/extra sms"));

        } else{
            // do nothing?
        }
        majorParamsCol1.appendChild(param1);
        majorParamsCol2.appendChild(param2);
        majorParamsRow.appendChild(majorParamsCol1);
        majorParamsRow.appendChild(majorParamsCol2);
        majorParams.appendChild(majorParamsRow);

        minorExtraCol1.appendChild(extra1);
        minorExtraCol2.appendChild(extra2);
        minorExtraRow.appendChild(minorExtraCol1);
        minorExtraRow.appendChild(minorExtraCol2);
        minorExtra.appendChild(minorExtraRow);

        // Filling out majorBtns <div class="col-2">
        let btnP = document.createElement("input");
        btnP.className = "btn btn-sm btn-default btn-plus";
        btnP.setAttribute("name", "plusBtn");
        btnP.type = "button";
        btnP.id = "id_btnPlus" + service_id;
        btnP.value = "+"


        let count = document.createElement("small");
        count.className = "text-muted";
        count.id = "id_serviceQuantity" + service_id.toString();
        count.setAttribute("value", "0");
        count.appendChild(document.createTextNode("0"));

        let btnM = document.createElement("input");
        btnM.setAttribute("name", "minusBtn");
        btnM.className = "btn btn-sm btn-default btn-minus";
        btnM.type = "button";
        btnM.id = "id_btnMinus" + service_id.toString();
        btnM.value = "-";

        majorBtns.appendChild(btnP);
        majorBtns.appendChild(count);
        majorBtns.appendChild(btnM);

        // Filling out minorMonth <div class="col-5">
        let defaultPeriodTag = document.createElement("p");
        defaultPeriodTag.id = "id_defaultPeriod" + service_id.toString();
        defaultPeriodTag.className = "text-muted mb-1";
        let defaultStr = "$" + bp1.toString() + " /mo for 12 months";
        defaultPeriodTag.appendChild(document.createTextNode(defaultStr));

        let otherPeriodsTag = document.createElement("small");
        otherPeriodsTag.className = "fw-lighter text-muted";
        otherPeriodsTag.id = "id_otherPeriods" + service_id.toString();
        let otherStr = "$" + bp2.toString() + " - 24mo | $" + bp3.toString() + " - 36mo";
        otherPeriodsTag.appendChild(document.createTextNode(otherStr));

        minorMonth.appendChild(defaultPeriodTag);
        minorMonth.appendChild(otherPeriodsTag);

        rowMajor.appendChild(majorTitle);
        rowMajor.appendChild(majorParams);
        rowMajor.appendChild(majorBtns);

        rowMinor.appendChild(minorMonth);
        rowMinor.appendChild(minorExtra);

        li.appendChild(rowMajor);
        li.appendChild(rowMinor);
        ul.appendChild(li);


        btnP.onclick = function() { // increment
            let quantityElement = this.parentElement.childNodes[1];
            let oldValue = quantityElement.getAttribute("value");
            let oldSum = parseInt(oldValue.valueOf());
            quantityElement.removeChild(quantityElement.lastChild);
            let newSum = oldSum + 1;
            quantityElement.value = newSum.toString();
            quantityElement.setAttribute("value", newSum.toString());
            quantityElement.appendChild(document.createTextNode(newSum.toString()));
            console.log("INCR " + oldSum + " to " + newSum.toString());
        }

        btnM.onclick = function() { // decrement
            let quantityElement = this.parentElement.childNodes[1];
            let oldValue = quantityElement.getAttribute("value");
            let oldSum = parseInt(oldValue.valueOf());
            quantityElement.removeChild(quantityElement.lastChild);
            let newSum = oldSum - 1;
            if(newSum < 0) {
                newSum = 0;
            }
            quantityElement.value = newSum.toString();
            quantityElement.setAttribute("value", newSum.toString());
            quantityElement.appendChild(document.createTextNode(newSum.toString()));
            console.log("INCR " + oldSum + " to " + newSum.toString());
        }




    };


    /**
     * This function performs the POST request to add the new service package to the DB
     * @param name: Name of the service package
     * @param period: The default validity period selected
     * @param listServices: Array of the service IDs to be offered with the new service package
     * @param listOptions: Array of the optional product IDs to be offered with the new service package.
     */
    function addPackage(name, period, listServices, listOptions) {
        let postRequest = $.post("AddServicePackage", {name: name, period:period, listServices:listServices, listOptions:listOptions});
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
        let getRequest = $.get("GetService", {service_id: id});
        getRequest.done(function (data, textStatus, jqXHR) {
            let service = jqXHR.responseJSON;
            let defaultBasePrice = 0;
            let other1 = 0;
            let other2 = 0;
            let period1 = 0;
            let period2 = 0;
            if(period == 1) {
                defaultBasePrice = service.bp1;
                other1 = service.bp2;
                period1 = 2;
                other2 = service.bp3;
                period2 = 3;
            } else if(period == 2) {
                defaultBasePrice = service.bp2;
                other1 = service.bp1;
                period1 = 1;
                other2 = service.bp3;
                period2 = 3;
            } else if(period == 3) {
                defaultBasePrice = service.bp3;
                other1 = service.bp1;
                period1 = 1;
                other2 = service.bp2;
                period2 = 2;
            } else {
                alert("Houston, another problemo!");
            }
            let defaultElement = document.getElementById("id_defaultPeriod" + service.service_id);
            let otherElement = document.getElementById("id_otherPeriods" + service.service_id);

            defaultElement.removeChild(defaultElement.lastChild);
            otherElement.removeChild(otherElement.lastChild);

            defaultElement.appendChild(document.createTextNode("$" + defaultBasePrice + "/mo for" + period * 12 + "months"));
            otherElement.appendChild(document.createTextNode("$" + other1 + " - " + period1 * 12 + "mo | $" + other2 + " - " + period2 * 12 + "mo"));

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
        function(){
            let planSelected = $(this).val();
            $("div.selectDiv").hide();
            $("#show"+planSelected).show();
        }
    );

    $('input[name="defaultValidity"]').click(
        function(){
            let period = $(this).val();
            let services = document.getElementsByName("service")
            for(let i = 0; i < services.length; i++) {
                let id = services[i].getAttribute("id").replace(/\D/g, '');
                changeServiceLabel(id, period);
                // update total
            }
        }
    );
});



