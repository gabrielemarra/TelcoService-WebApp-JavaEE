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
    function showOptionalProduct(name, price, id) {
        let div = document.getElementById("id_allOptionalProductsList");
        let input = document.createElement('input');
        let label = document.createElement('label')
        input.className = "btn-check";
        input.type = "checkbox";
        /*TODO: Use the optional product ID number instead */
        input.id = "id_checkboxOption" + id;
        input.autocomplete = "off";
        input.value = price;
        input.name = "option";


        label.className = "btn btn-outline-primary";
        label.htmlFor = "id_checkboxOption" + id;
        label.appendChild(document.createTextNode(" $" + price + " - "+ name));
        div.appendChild(input);
        div.appendChild(label);
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
           showService(planType, bp1, bp2, bp3,service_id);
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
                    showService(services[i].planType, services[i].bp1, services[i].bp2, services[i].bp3, services[i].service_id);
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
    function showService(planType, bp1, bp2, bp3, service_id) {
        let div = document.getElementById("id_allServicesList");
        let input = document.createElement('input');
        let label = document.createElement('label')

        input.className = "btn-check services";
        input.type = "checkbox";
        input.id = "id_checkboxService" + service_id;
        input.autocomplete = "off";
        // TODO: the value should be the base price associated with the validity period the user has currently selected.
        //input.value = bp1;
        input.name = "service";
        input.onclick="testClick()";
        //input.dataset.quantity = 0;
        //input.setAttribute("dataset.quantity", 0);



        label.className = "btn btn-outline-primary";
        label.htmlFor = "id_checkboxService" + service_id;
        label.id = "id_serviceLabel" + service_id;
        label.name= "serviceLabel";
        // TODO: the value shown should be the base price associated with the validity period the user has currently
        //  selected.
        label.appendChild(document.createTextNode("$" + bp1 + " / " + "$" + bp2 + " / " + "$" + bp3 + "   " + planType.toString().replace('_', ' ')));

        div.appendChild(input);
        div.appendChild(label);

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
            if(period == 1) {
                defaultBasePrice = service.bp1;
            } else if(period == 2) {
                defaultBasePrice = service.bp2;
            } else if(period == 3) {
                defaultBasePrice = service.bp3;
            } else {
                alert("Houston, another problemo!");
            }

            let inputElement = document.getElementById("id_checkboxService" + service.service_id)
            inputElement.setAttribute("value", defaultBasePrice.toString());
            let labelElement = document.getElementById("id_serviceLabel" + service.service_id)

            let child = labelElement.lastChild;
            labelElement.removeChild(child)
            labelElement.appendChild(document.createTextNode("$" + defaultBasePrice + " | " + service.type.replace('_', ' ')));
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



    function testClick() {
        console.log("clicked with onclick!");
    };

    $('input[name="serviceLabel"]').change(
        function() {
            console.log("changed!");
        }
    );

    $('input[name="serviceLabel"]').select(
        function() {
            console.log("selected!");
        }
    );

    $('input[name="serviceLabel"]').click(
        function() {
            console.log("clicked!");
        }
    );

    $('input[name="service"]').change(
        function() {
            console.log("changed!");
        }
    );

    $('input[name="service"]').select(
        function() {
            console.log("selected!");
        }
    );

    $('input[name="service"]').click(
        function() {
            console.log("clicked!");
        }
    );

    /*

    let sumElement = document.getElementById("id_totalCostServices");
    let val = parseInt(sumElement.getAttribute("value"));
    if($(this).is(':checked')) {
        val = val + parseInt($(this).val());
    } else {
        val = val - parseInt($(this).val());
    }
    // update the value attribute
    sumElement.setAttribute("value", val.toString());
    // update the html text
    sumElement.innerHTML = val.toString();
    * */








});



