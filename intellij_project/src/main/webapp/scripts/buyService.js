$(document).ready(function () {
    let servicePackagesAvailable;
    let optionalProductsAvailable;
    let servicesAvailable;

    let servicePackageSelected;
    let optionalProductsSelected = [];
    let validityPeriodSelected;
    let startingDateSelected = null;
    let totalPriceSelected;


    setAvailableDate();
    getServicePackages();
    getServiceList();
    getOptionalProductList();


    $("#servicePackage_select").change(function () {
        changePackageSelection();
    });

    $("#startDate").change(function () {
        startingDateSelected = new Date(Date.parse($("#startDate").val()));
        updateSummaryStartingDate();
    });

    $('input[type=radio][name=validityPeriodCheckbox]').change(function () {
        if (this.value === 'option1') {
            validityPeriodSelected = 1;
        } else if (this.value === 'option2') {
            validityPeriodSelected = 2;
        } else if (this.value === 'option3') {
            validityPeriodSelected = 3;
        }

        updateSummaryValidityPeriod();
    });

    $("#buy_button").click(function () {
        buyButtonPressed();
    })

    $("#optionalProductSelectionCollapse").on('change', '.optional_prod_checkboxes', function () {
        let read_opt_id = parseInt(this.dataset.opt_id);
        if (this.checked) {
            for (let optionalProductsAvailableKey in optionalProductsAvailable) {
                if (optionalProductsAvailable[optionalProductsAvailableKey].option_id === read_opt_id) {
                    optionalProductsSelected.push(optionalProductsAvailable[optionalProductsAvailableKey]);
                    break;
                }
            }
        } else {
            for (let optionalProductsSelectedKey in optionalProductsSelected) {
                if (optionalProductsSelected[optionalProductsSelectedKey].option_id === read_opt_id) {
                    optionalProductsSelected.splice(optionalProductsSelectedKey, 1);
                    break;
                }
            }
        }

        updateSummaryOptionalProducts();
    });

    function getServicePackages() {
        let packages_request = $.get("GetAvailableServicePackages");
        packages_request.done(function (data, textStatus, jqXHR) {
            servicePackagesAvailable = jqXHR.responseJSON;
            displayServicePackages();
        });
        packages_request.fail(function (jqXHR, textStatus, errorThrown) {
            throw "UnableToRetrieveServicePackages"
        });
    }

    function displayServicePackages(servicePackageList) {
        let servicePackageSelect = document.getElementById("servicePackage_select");

        servicePackageSelect.innerHTML = "";

        for (let key in servicePackagesAvailable) {
            displayOneServicePackage(servicePackagesAvailable[key])
        }

        servicePackageSelect.classList.remove("placeholder")
        servicePackageSelect.disabled = false;
    }

    function displayOneServicePackage(singleServicePackageInfo) {
        let servicePackageSelect = document.getElementById("servicePackage_select");
        let template = document.getElementById("package_select_row_template");

        let clone = template.content.cloneNode(true);
        let servicePackageRow = clone.querySelector("option");

        servicePackageRow.textContent = singleServicePackageInfo.package_name;

        let params = new URLSearchParams(document.location.search);
        let requested_service_package_id = parseInt(params.get("package_id"));

        if (singleServicePackageInfo.package_id === requested_service_package_id) {
            servicePackageRow.selected = true;
            servicePackageSelected = singleServicePackageInfo;
            updateDataOnPackageSelection(singleServicePackageInfo);
            updateSummaryServicePackage();
        }

        //the package id is stored inside this custom attribute
        servicePackageRow.dataset.package_id = singleServicePackageInfo.package_id;

        servicePackageSelect.appendChild(clone);
    }

    function changePackageSelection() {
        let servicePackageSelect = document.getElementById("servicePackage_select");
        let selected_package_id = parseInt(servicePackageSelect.options[servicePackageSelect.selectedIndex].dataset.package_id);

        for (let servicePackagesAvailableKey in servicePackagesAvailable) {
            if (servicePackagesAvailable[servicePackagesAvailableKey].package_id === selected_package_id) {
                servicePackageSelected = servicePackagesAvailable[servicePackagesAvailableKey];
                updateDataOnPackageSelection(servicePackageSelected);
                updateSummaryServicePackage();
                optionalProductsSelected = [];
                updateSummaryOptionalProducts();
                break;
            }
        }
    }

    function updateDataOnPackageSelection(singleServicePackageInfo) {
        validityPeriodSelected = servicePackageSelected.default_validity_period;
        set_default_validity_period(singleServicePackageInfo.default_validity_period);
        display_default_optional_products(singleServicePackageInfo.optional_products)
    }

    function set_default_validity_period(default_validity_period) {
        switch (default_validity_period) {
            case 1:
                $("#validityPeriodCheckbox12").prop("checked", true);
                break;
            case 2:
                $("#validityPeriodCheckbox24").prop("checked", true);
                break;
            case 3:
                $("#validityPeriodCheckbox36").prop("checked", true);
                break;
            default:
                alert("ERROR_VALIDITY PERIOD")
        }
    }

    function display_default_optional_products(optional_prod_to_show) {
        let optionalProductAccordion = document.getElementById("selectedOptionalProductsText");

        optionalProductAccordion.innerHTML = "";

        if (optional_prod_to_show.length === 0) {
            $("#label_optional_product_available").prop("hidden", true);
            $("#label_optional_product_not_available").prop("hidden", false);
        } else {
            $("#label_optional_product_available").prop("hidden", false);
            $("#label_optional_product_not_available").prop("hidden", true);
            for (let key in optional_prod_to_show) {
                displayOneOptionalProduct(optional_prod_to_show[key])
            }
        }
    }

    function displayOneOptionalProduct(singleOptionalProduct) {
        let optionalProductAccordion = document.getElementById("selectedOptionalProductsText");
        let template = document.getElementById("optional_product_checkbox_template");

        let clone = template.content.cloneNode(true);
        let optionProdInput = clone.querySelector("input");
        let optionProdLabel = clone.querySelector("label");

        optionProdInput.id = "opt_prod_" + singleOptionalProduct.opt_id;
        optionProdInput.value = singleOptionalProduct.opt_id;

        optionProdLabel.textContent = singleOptionalProduct.opt_name + " - " + singleOptionalProduct.opt_cost + "€/mo";
        optionProdLabel.for = "opt_prod_" + singleOptionalProduct.opt_id;

        //the package id is stored inside this custom attribute
        optionProdInput.dataset.opt_id = singleOptionalProduct.opt_id;

        clone.querySelector("div").classList.remove("placeholder");
        optionalProductAccordion.appendChild(clone);
    }

    function getServiceList() {
        let service_request = $.get("GetAllServices");
        service_request.done(function (data, textStatus, jqXHR) {
            servicesAvailable = jqXHR.responseJSON;
        });
        service_request.fail(function (jqXHR, textStatus, errorThrown) {
            throw "UnableToRetrieveServices"
        });
    }

    function getOptionalProductList() {
        let service_request = $.get("GetAllOptionalProducts");
        service_request.done(function (data, textStatus, jqXHR) {
            optionalProductsAvailable = jqXHR.responseJSON;
        });
        service_request.fail(function (jqXHR, textStatus, errorThrown) {
            throw "UnableToRetrieveOptionalProducts"
        });
    }

    function setAvailableDate() {
        let today = new Date().toISOString().split('T')[0];
        $("#startDate").prop('min', today);
    }


    function updateSummaryServicePackage() {
        $("#summary_service_package_name").text(servicePackageSelected.package_name).removeClass("placeholder");
        $("#summary_service_package_price").text(servicePackageSelected.prices[servicePackageSelected.default_validity_period - 1]).removeClass("placeholder");
        updateSummaryValidityPeriod();

        updateSummaryEndingDate();
        updateSummaryTotalPrice();
    }

    function updateSummaryValidityPeriod() {
        $("#summary_validity_period").text(validityPeriodSelected * 12).removeClass("placeholder");
        $("#summary_service_package_price").text(servicePackageSelected.prices[validityPeriodSelected - 1]).removeClass("placeholder");

        updateSummaryTotalPrice();
        updateSummaryEndingDate();
    }

    function updateSummaryStartingDate() {
        $("#summary_starting_date").text(startingDateSelected.toDateString());

        updateSummaryEndingDate();
    }

    function updateSummaryEndingDate() {
        if (startingDateSelected != null) {
            let year = startingDateSelected.getFullYear();
            let endingDate = new Date(startingDateSelected);
            endingDate.setFullYear(year + validityPeriodSelected);
            $("#summary_ending_date").text(endingDate.toDateString());
        }
    }

    function updateSummaryOptionalProducts() {
        let summaryTable = document.getElementById("summary_table");

        for (let i = summaryTable.rows.length - 2; i > 0; i--) {
            summaryTable.deleteRow(i);
        }

        let summaryTableBody = document.getElementById("summary_table_body");

        for (let optionalProductsSelectedKey in optionalProductsSelected) {

            let template = document.getElementById("summary_optional_prod_row_template");

            let clone = template.content.cloneNode(true);
            let optionProdText = clone.querySelector(".summary_opt_prod_name");
            let optionProdPrice = clone.querySelector(".summary_opt_prod_price");

            optionProdText.textContent = optionalProductsSelected[optionalProductsSelectedKey].name;
            optionProdPrice.textContent = optionalProductsSelected[optionalProductsSelectedKey].price;

            summaryTableBody.appendChild(clone);
        }

        updateSummaryTotalPrice();
    }

    function updateSummaryTotalPrice() {
        let totalPrice = 0;
        let optProdPriceSum = 0;

        for (let optionalProductsSelectedKey in optionalProductsSelected) {
            optProdPriceSum += optionalProductsSelected[optionalProductsSelectedKey].price;
        }

        totalPrice = optProdPriceSum + servicePackageSelected.prices[validityPeriodSelected - 1];

        $("#summary_total_price").text(totalPrice).removeClass("placeholder");
        totalPriceSelected = totalPrice;
    }

    function buyButtonPressed() {
        sessionStorage.setItem('package_id', servicePackageSelected.package_id);
        sessionStorage.setItem('validity_period', validityPeriodSelected);
        sessionStorage.setItem('total_cost',totalPriceSelected);
        sessionStorage.setItem('optionalProducts',JSON.stringify(optionalProductsSelected));
        sessionStorage.setItem('startDate',JSON.stringify(startingDateSelected));

        sessionStorage.setItem('existingOrder', "false");


        window.location.href = "./" + "confirmation.html"
    }
});


