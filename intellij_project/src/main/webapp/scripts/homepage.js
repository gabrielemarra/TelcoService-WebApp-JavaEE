$(document).ready(function () {
    $.ajaxSetup({cache: false});

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

});