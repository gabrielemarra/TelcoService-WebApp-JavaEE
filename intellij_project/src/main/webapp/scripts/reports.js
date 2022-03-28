$(document).ready(function () {

    $.ajaxSetup({cache: false});

    showAllPackages();
    showAllOptions();
    window.identifyBestSeller();

    function showAllPackages() {
        let getRequest = $.get("GetAvailableServicePackages");
        getRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            //alert("get all packages: success");
            for(let i = 0; i < response.length; i++) {
                showOnePackage(response[i].name, response[i].package_id);
            }
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
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
        let avgNumOptions = 0;

        let getRequest = $.get("GetPackageOrders", {package_id: package_id});
        getRequest.done(function (data, textStatus, jqXHR) {
            let orders = jqXHR.responseJSON;
            if (orders.length > 0) {
                for (let i = 0; i < orders.length; i++) {
                    totalNoOptions += orders[i].order_baseCost;
                    totalWithOptions += orders[i].order_totalCost;
                    purchasesByPeriod[parseInt(orders[i].order_validity) - 1] += 1;
                    avgNumOptions += orders[i].order_num_options;
                }
                avgNumOptions = avgNumOptions/orders.length;
            }
            serviceTile.dataset.period1 = purchasesByPeriod[0].toString();
            serviceTile.dataset.period2 = purchasesByPeriod[1].toString();
            serviceTile.dataset.period3 = purchasesByPeriod[2].toString();
            serviceTile.dataset.totalNoOptions = totalNoOptions;
            serviceTile.dataset.totalWithOptions = totalWithOptions;
            serviceTile.dataset.avgNumOptions = avgNumOptions;
            updateServiceButtons(package_id);
        });
        getRequest.fail(function (data, textStatus, jqXHR) {
            serviceTile.dataset.period1 = purchasesByPeriod[0].toString();
            serviceTile.dataset.period2 = purchasesByPeriod[1].toString();
            serviceTile.dataset.period3 = purchasesByPeriod[2].toString();
            serviceTile.dataset.totalNoOptions = totalNoOptions;
            serviceTile.dataset.totalWithOptions = totalWithOptions;
            serviceTile.dataset.avgNumOptions = avgNumOptions;
            updateServiceButtons(package_id);

        });
    }

    function updateServiceButtons(package_id) {
        let tile = document.getElementById("id_package"+ package_id);
        let buttonPurchases = tile.querySelector("button.purchases");
        let buttonOptions = tile.querySelector("button.options");
        buttonPurchases.textContent = parseInt(tile.dataset.period1) + parseInt(tile.dataset.period2) + parseInt(tile.dataset.period3);
        buttonOptions.textContent = parseInt(tile.dataset.avgNumOptions);
    }

    function showAllOptions() {
        let getRequest = $.get("GetAllOptions");
        getRequest.done(function (data, textStatus, jqXHR) {
            let response = jqXHR.responseJSON;
            for(let i = 0; i < response.length; i++) {
                showOneOption(response[i].name, response[i].price, response[i].option_id);
            }
        });


        getRequest.fail(function (data, textStatus, jqXHR) {
        });
    }

    function showOneOption(name, price, id) {
        let tilesList = document.getElementById("id_allOptionsTiles");
        let template = document.getElementById("id_option_tile_template");
        let clone = template.content.cloneNode(true);
        let lineItem = clone.querySelector("a");
        lineItem.id = "id_option" + id;

        let text = clone.querySelectorAll("p");
        text[0].textContent = name;
        text[1].textContent = "Option ID " + id;
        lineItem.querySelector("button").textContent = 0;
        tilesList.appendChild(clone);
        getOptionInformation(id);
    }

    function getOptionInformation(option_id) {
        let optionTile = document.getElementById("id_option" + option_id);
        let getRequest = $.get("GetOptionValue", {option_id: option_id});
        getRequest.done(function (data, textStatus, jqXHR) {
            //alert("option value GET success");
            let response = jqXHR.responseJSON;
            optionTile.dataset.salesValue = response.value;
            updateOptionButtons(option_id);
            });
        getRequest.fail(function (data, textStatus, jqXHR) {
        });
    }

    function updateOptionButtons(option_id) {
        let tile = document.getElementById("id_option" + option_id);
        let button = tile.querySelector("button.optionsSales");
        button.textContent = tile.dataset.salesValue;
    }
    function showTable(elementId) {
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
        td[0].textContent = parseInt(info.dataset.period1) + parseInt(info.dataset.period2) + parseInt(info.dataset.period3);
        td[2].textContent = info.dataset.period1;
        td[4].textContent = info.dataset.period2;
        td[6].textContent = info.dataset.period3;
        //TODO: use the euro money sign?
        td[7].textContent = info.dataset.avgNumOptions;
        td[8].textContent = "$" + info.dataset.totalNoOptions;
        td[9].textContent = "$" + info.dataset.totalWithOptions;
        tablePlacement.appendChild(clone);
        showChart(info.dataset.period1, info.dataset.period2, info.dataset.period3);
    }

    function showChart(period1, period2, period3) {
        let currentPlot = document.getElementById("id_plotPlaceholder");
        while (currentPlot.hasChildNodes()) {
            currentPlot.removeChild(currentPlot.lastChild);
        }

        let template = document.getElementById("id_validity_plot_template");
        let clone = template.content.cloneNode(true);
        currentPlot.appendChild(clone);
        var data = [{
            title: 'Purchases By Period',
            values: [period1, period2, period3],
            labels: ['Period 1', 'Period 2', 'Period 3'],
            type: 'pie',
            textinfo: 'percent+value'
        }];
        var layout = {height: 400, width: 400};

        Plotly.newPlot('validityPeriodPlot', data, layout);
    }

    function identifyBestSeller() {
        let tileList = document.getElementById("id_allOptionsTiles");
        let max = 0;
        let optionTileId="";
        for(let i = 0; i < tileList.childElementCount; i++) {
            let temp = tileList[i].querySelector("button.optionsSales");
            if(temp > max) {
                max = temp;
                optionTileId = temp.id;
            }
        }
        document.getElementById(optionTileId).textContent += "* Best Seller *";

    }



});
