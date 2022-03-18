$(document).ready(function () {

   $.ajaxSetup({cache: false});

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

   function addOptionalProduct(name, price) {
       let postRequest = $.post("AddOptionalProduct", {name: name, price: price});

       postRequest.done(function (data, textStatus, jqXHR) {
           alert("optional product POST success");
       });

       postRequest.fail(function (data, textStatus, jqXHR) {
           alert("optional product POST fail");
       });

   };

   function addService(planType, bp1, bp2, bp3, gigIncl, smsIncl, minIncl, gigExtra, smsExtra, minExtra) {

       let postRequest = $.post("AddService", {planType: planType, bp1:bp1, bp2:bp2, bp3:bp3, gigIncl: gigIncl, smsIncl: smsIncl, minIncl:minIncl, gigExtra: gigExtra, smsExtra: smsExtra, minExtra:minExtra});

       postRequest.done(function (data, textStatus, jqXHR) {
           alert("adding service POST success");
       });

       postRequest.fail(function (data, textStatus, jqXHR) {
           alert("adding service POST fail");
       });
   };


   $('input[type="radio"]').click(
       function(){
           let planSelected = $(this).val();
            $("div.selectDiv").hide();
            $("#show"+planSelected).show();
       }
   );

   showAllOptionalProducts();

   function showAllOptionalProducts() {
       let getRequest = $.get("GetAllOptionalProducts");
       getRequest.done(function (data, textStatus, jqXHR) {
           alert("getting all optional prods GET success");
       });

       getRequest.fail(function (data, textStatus, jqXHR) {
           alert("getting all optional prods GET fail");
       });


   };


   function addOptionalProduct() {

    let checkbox = document.createElement('input');
    let container = document.getElementById("id_allOptionalProducts")
    checkbox.type = "checkbox";
    checkbox.name = "optionalProductCheckbox";
    checkbox.value = "";
    checkbox.id = "id_optionalProductCheckbox";

    let label = document.createElement('label')
    label.htmlFor = "id";
    label.appendChild(document.createTextNode('text for label after checkbox'));

    container.appendChild(checkbox);
    container.appendChild(label);
   };


});



