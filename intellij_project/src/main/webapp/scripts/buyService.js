$(document).ready(function () {

    getServicePackages()

    function getServicePackages() {
        let packages = $.get("GetAvailableServicePackages");
        packages.done(function (data, textStatus, jqXHR) {
            displayServicePackages(jqXHR.responseJSON);
        });
        packages.fail(function (jqXHR, textStatus, errorThrown) {
            throw "UnableToRetrieveServicePackages"
        });
    }

    function displayServicePackages(servicePackageList) {
        let servicePackageSelect = document.getElementById("servicePackage_select");

        servicePackageSelect.innerHTML = "";

        for (let key in servicePackageList) {
            displayOneServicePackage(servicePackageList[key])
        }

        servicePackageSelect.classList.remove("placeholder")
        servicePackageSelect.disabled = false;
    }

    function displayOneServicePackage(servicePackageInfo) {
        let servicePackageSelect = document.getElementById("servicePackage_select");
        let template = document.getElementById("package_select_row_template");

        let clone = template.content.cloneNode(true);
        let servicePackageRow = clone.querySelector("option");

        servicePackageRow.textContent = servicePackageInfo.package_name;

        let params = new URLSearchParams(document.location.search);
        let requested_service_package_id = parseInt(params.get("package_id"));

        if (servicePackageInfo.package_id === requested_service_package_id) {
            servicePackageRow.selected = true;
            set_default_validity_period(servicePackageInfo.default_validity_period);
        }

        //the package id is stored inside this custom attribute
        servicePackageRow.dataset.package_id = servicePackageInfo.package_id;

        servicePackageSelect.appendChild(clone)
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
            default: alert("ERROR_VALIDITY PERIOD")
        }
    }

});