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
            // Can we write one function and reuse it for both services and options?
            let listServices = document.querySelectorAll('input[name=service]:checked');
            let listOptions = document.querySelectorAll('input[name=option]:checked');
            let name = $("#servicePackageNameId").val();
            let period = document.querySelectorAll('input[name=defaultValidity]:checked'); //

            addPackage(name, period[0].value, listServices, listOptions);
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
                   showOptionalProduct(options[i].name, options[i].price);
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
    function showOptionalProduct(name, price) {
        let div = document.getElementById("id_allOptionalProductsList");
        let input = document.createElement('input');
        let label = document.createElement('label')
        input.className = "btn-check";
        input.type = "checkbox";
        /*TODO: Use the optional product ID number instead */
        input.id = "id_checkbox" + name.replace(/\s+/g, '');
        input.autocomplete = "off";
        input.value = price;
        input.name = "option";


        label.className = "btn btn-outline-primary";
        label.htmlFor = "id_checkbox" + name.replace(/\s+/g, '');
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
     * @param service
     */
    function showService(planType, bp1, bp2, bp3, service_id) {
        let div = document.getElementById("id_allServicesList");
        let input = document.createElement('input');
        let label = document.createElement('label')

        input.className = "btn-check services";
        input.type = "checkbox";
        input.id = "id_checkbox" + service_id;
        input.autocomplete = "off";
        // TODO: the value should be the base price associated with the validity period the user has currently selected.
        input.value = bp1;
        input.name = "service";

        label.className = "btn btn-outline-primary";
        label.htmlFor = "id_checkbox" + service_id;
        // TODO: the value shown should be the base price associated with the validity period the user has currently
        //  selected.
        label.appendChild(document.createTextNode("$" + bp1 + " / " + "$" + bp2 + " / " + "$" + bp3 + "   " + planType.toString()));

        div.appendChild(input);
        div.appendChild(label);

    };


    /**
     * This function performs the POST request to add the new service package to the DB
     * @param name
     * @param period
     * @param listServices
     * @param listOptions
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
                let id = services[i].getAttribute("id").replace(/\D/g, '');;

                // need to get the service by its ID?
                // then change the value to the corresponding baseprice
                // then change the innerHTML? of the label? with the new baseprice
                //services[i].setAttribute("value", )
            }
        }
    );





});



