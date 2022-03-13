$(document).ready(function () {
    $.ajaxSetup({cache: false});

    if (sessionStorage.getItem("employee")===true){
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

    function displayPersonalData(){
    //    Should we make a request? For now we use the stored values
        let personalInfoString = sessionStorage.getItem("name") + " | " + sessionStorage.getItem("email")
        $("#username_right_corner").html(personalInfoString)

    }



    function getServicePackages() {
        let packages = $.get("GetAvailableServicePackages");
        packages.done(function (data, textStatus, jqXHR) {
            alert("Success!")
        });
        packages.fail(function (jqXHR, textStatus, errorThrown) {
            alert("FAIL!")
        });
    }

    function getRejectedOrders() {
        let getResponse = $.get("GetRejectedOrders");

        getResponse.done(function (data, textStatus, jqXHR) {
            let rejectedOrders = jqXHR.responseJSON;

            let table = document.getElementById("id_rejected_orders_body");
            for (let i = 0; i < rejectedOrders.length; i++) {
                let row = table.insertRow();

                let packageIdCell = row.insertCell(0);
                packageIdCell.innerHTML = rejectedOrders[i].package_id;

                let totalPriceCell = row.insertCell(1);
                totalPriceCell.innerHTML = rejectedOrders[i].total_price;
            }

        });

        rejectedOrders.fail(function (data, textStatus, errorThrown) {
            alert("world");

        });


    }
});
