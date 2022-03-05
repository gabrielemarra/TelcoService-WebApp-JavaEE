$(document).ready(function () {
    $.ajaxSetup({cache: false});

    $("#selectButton").click(
        function (event) {
            event.preventDefault();
            let startDate = $("#startDate").val();
            let user = $("#userID").val();
            let package = $("#servicePackage").val();
            let options = $("#optionalProducts").val();
            submitNewOrder(startDate, userID, servicePackage, optionalProducts);

            /*
            *     public Order insertOrder(LocalDate subscriptionStartDate, User user, ServicePackage servicePackage, List<OptionalProduct> optionalProductList) {

            * */
        }
    );


    getServicePackages()

    function getServicePackages() {
        let packages = $.get("GetAvailableServicePackages");
        packages.done(function (data, textStatus, jqXHR) {
            alert("Success!")
        });
        packages.fail(function (jqXHR, textStatus, errorThrown) {
            alert("FAIL!")
        });

    }


    function submitNewOrder(startDate, userID, servicePackage, optionalProducts){


    }

    /* Potential functions we may need for constructing homepage:
    * isUserLoggedIn
    * isUserInsolvent
    * getRejectedOrders
    * listOneRejectedOrder
    * buildPackageDisplayCard
    *
    */


    /*
    *
    function getRejectedOrders(user) {
        let rejectedOrders = $.get("GetRejectedOrders");
        $.each(rejectedOrders, function(i, order) {
            name = order.getName();
        })
    }
    * */





});