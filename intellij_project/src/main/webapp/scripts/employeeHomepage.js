$(document).ready(function () {
    $.ajaxSetup({cache: false});

    $("#submitServicePackage").click(
        function (event) {
            event.preventDefault();
            //let id = $("#id").val(); // automatically generated?
            let name = $("#name").val();
            let validityPeriod = $("#validityPeriod").val();
            let options = $("#optionalProducts").val();
            //let services = $("#services").val();
        }
    );

    $("#submitOptionalProduct").click(
        function (event) {
            event.preventDefault();
            //let id = $("#id").val(); // automatically generated?
            let name = $("#name").val();
            let validityPeriod = $("#validityPeriod").val();
        }
    );

    $("#addService").click(
        function (event) {
            event.preventDefault();
            //let id = $("#id").val(); // automatically generated?
            let type = $("#type").val();
            let bp1 = $("#validityPeriod").val();
            let bp2 = $("#optionalProducts").val();
            let bp3 = $("#services").val();
            let gigIncluded = $("#gigIncluded").val();
            let minIncluded = $("#minIncluded").val();
            let smsIncluded = $("#smsIncluded").val();
            let gigExtra = $("#gigExtra").val();
            let minExtra = $("#minExtra").val();
            let smsExtra = $("#smsExtra").val();
        }
    );

    function addOptionalProduct(name, validityPeriod) {
        let postRequest = $.post("AddOptionalProduct", {name: name, validityPeriod: validityPeriod});

        postRequest.done(function (data, textStatus, jqXHR) {
            let product_data = jqXHR.responseJSON;
            sessionStorage.setItem('name', product_data.name);
            sessionStorage.setItem('validityPeriod', product_data.validityPeriod);
        });

        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            //Spawn an error message after now login
            let JSONText = jQuery.parseJSON(jqXHR.responseText)
            alert("Adding Optional Product Err: " + jqXHR.responseText);
        });
    }

    function addService(type, bp1, bp2, bp3, gig_incl, min_incl, sms_incl, gig_extra, min_extra, sms_extra) {
        let postRequest = $.post("AddService", {
            type: type,
            bp1: bp1,
            bp2: bp2,
            bp3: bp2,
            gig_incl: gig_incl,
            min_incl: min_incl,
            sms_incl: sms_incl,
            gig_extra: gig_extra,
            min_extra: min_extra,
            sms_extra: sms_extra
        });

        postRequest.done(function (data, textStatus, jqXHR) {

            //Here we save the data into the sessionStorage
            let service_data = jqXHR.responseJSON;
            sessionStorage.setItem('type', service_data.type);
            sessionStorage.setItem('bp1', service_data.bp1);
            sessionStorage.setItem('bp2', service_data.bp2);
            sessionStorage.setItem('bp3', service_data.bp3);
            sessionStorage.setItem('gig_incl', service_data.gig_incl);
            sessionStorage.setItem('min_incl', service_data.min_incl);
            sessionStorage.setItem('sms_incl', service_data.sms_incl);
            sessionStorage.setItem('gig_extra', service_data.gig_extra);
            sessionStorage.setItem('min_extra', service_data.min_extra);
            sessionStorage.setItem('sms_extra', service_data.sms_extra);


        });
        postRequest.fail(function (jqXHR, textStatus, errorThrown) {
            //Spawn an error message after now login
            let JSONText = jQuery.parseJSON(jqXHR.responseText)
            alert("Login Error: " + jqXHR.responseText);
        });
        // postRequest.always(function () {
        //     alert("Request completed");
        // });

    }
});

