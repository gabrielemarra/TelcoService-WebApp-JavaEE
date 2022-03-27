$(document).ready(function () {

    $.ajaxSetup({cache: false});

    showAllPackages();

    function showAllPackages() {
        let getRequest = $.get("GetAvailableServicePackages");
        getRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            alert("get all packages: success");
            for(let i = 0; i < response.length; i++) {
                showOnePackage(response[i].name, response[i].package_id);
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("Could not get all service packages...fail");
        });
    };

    function showOnePackage(name, id) {
        let tilesList = document.getElementById("id_allServicesPackageTiles");
        let template = document.getElementById("id_package_tile_template");
        let clone = template.content.cloneNode(true);
        let lineItem = clone.querySelector("a");
        lineItem.id = "id_package" + id;
        lineItem.addEventListener("click", function(){ showTable("id_package"+ id); });

        let text = clone.querySelectorAll("p");
        text[0].textContent = name;
        text[1].textContent = "Package ID " + id;
        lineItem.querySelector("button").textContent = 0;
        tilesList.appendChild(clone);
        getOrderInformation(id);
    }

    function getOrderInformation(package_id) {
        let serviceTile = document.getElementById("id_package" + package_id);
        let purchasesByPeriod = [0, 0, 0];
        let totalNoOptions = 0;
        let totalWithOptions = 0;

        let getRequest = $.get("GetPackageOrders", {package_id: package_id});
        getRequest.done(function (data, textStatus, jqXHR) {
            let orders = jqXHR.responseJSON;
            if (orders.length > 0) {
                for (let i = 0; i < orders.length; i++) {
                    totalNoOptions += orders[i].order_baseCost;
                    totalWithOptions += orders[i].order_totalCost;
                    purchasesByPeriod[parseInt(orders[i].order_validity) - 1] += 1;
                }
            }
            serviceTile.dataset.period1 = purchasesByPeriod[0].toString();
            serviceTile.dataset.period2 = purchasesByPeriod[1].toString();
            serviceTile.dataset.period3 = purchasesByPeriod[2].toString();
            serviceTile.dataset.totalNoOptions = totalNoOptions;
            serviceTile.dataset.totalWithOptions = totalWithOptions;
            updatePurchaseCount();
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            alert("Get all orders for single package GET fail!");
            serviceTile.dataset.period1 = purchasesByPeriod[0].toString();
            serviceTile.dataset.period2 = purchasesByPeriod[1].toString();
            serviceTile.dataset.period3 = purchasesByPeriod[2].toString();
            serviceTile.dataset.totalNoOptions = totalNoOptions;
            serviceTile.dataset.totalWithOptions = totalWithOptions;
        });
    }

    function updatePurchaseCount() {
        let titleList = document.getElementById("id_allServicesPackageTiles");
        let info = titleList.querySelectorAll("a");
        let buttons = titleList.querySelectorAll("button");
        for(let i = 0; i < buttons.length; i++) {
            buttons[i].textContent = parseInt(info[i].dataset.period1) + parseInt(info[i].dataset.period2) + parseInt(info[i].dataset.period3);
        }
    }

    function showTable(elementId) {
        //alert("clicked:" + elementId);
        let info = document.getElementById(elementId);
        let tablePlacement = document.getElementById("id_packageSalesTableCol");
        while(tablePlacement.hasChildNodes()) {
            tablePlacement.removeChild(tablePlacement.lastChild);
        }
        let template = document.getElementById("id_sales_summary_package_template");
        let clone = template.content.cloneNode(true);

        let name = clone.querySelector("h5");
        name.textContent = "Sales Summary: " + info.querySelector("p").textContent;
        let td = clone.querySelectorAll("td");
        td[0].textContent = info.dataset.period1;
        td[1].textContent = info.dataset.period2;
        td[2].textContent = info.dataset.period3;
        td[3].textContent = parseInt(info.dataset.period1) + parseInt(info.dataset.period2) + parseInt(info.dataset.period3);
        td[7].textContent = "$" + info.dataset.totalNoOptions;
        td[11].textContent = "$" + info.dataset.totalWithOptions;

        tablePlacement.appendChild(clone);
    }

});
