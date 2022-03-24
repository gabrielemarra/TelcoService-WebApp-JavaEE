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

        servicePackageSelect.innerHTML="";

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

        //the package id is stored inside this custom attribute
        servicePackageRow.dataset.package_id = servicePackageInfo.package_id

        servicePackageSelect.appendChild(clone)
    }

});