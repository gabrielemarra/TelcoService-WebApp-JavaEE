$(document).ready(function () {
    let servicePackagesAvailable;
    let optionalProductsAvailable;
    let servicesAvailable;


    setAvailableDate();
    getServicePackages();
    getServiceList();
    getOptionalProductList();


    $("#servicePackage_select").change(function (e) {
        changePackageSelection();
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
            updateDataOnPackageSelection(singleServicePackageInfo)
        }

        //the package id is stored inside this custom attribute
        servicePackageRow.dataset.package_id = singleServicePackageInfo.package_id;

        servicePackageSelect.appendChild(clone)
    }

    function changePackageSelection() {
        let servicePackageSelect = document.getElementById("servicePackage_select");
        let selected_package_id = parseInt(servicePackageSelect.options[servicePackageSelect.selectedIndex].dataset.package_id);

        for (let servicePackagesAvailableKey in servicePackagesAvailable) {
            if (servicePackagesAvailable[servicePackagesAvailableKey].package_id === selected_package_id) {
                updateDataOnPackageSelection(servicePackagesAvailable[servicePackagesAvailableKey])
                break;
            }
        }
    }

    function updateDataOnPackageSelection(singleServicePackageInfo) {
        console.log("Update package " + singleServicePackageInfo.package_id + " - " + singleServicePackageInfo.package_name)
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
});


