$(document).ready(function () {
   $.ajaxSetup({cache: false});

   $("#addOptionalProductButton").click(
       function (event) {
           event.preventDefault();
           let name = $("#optionalProductNameId").val();
           let price = $("#optionalProductPriceId").val();
           addOptionalProduct(name, price);
       }
   );

   function addOptionalProduct(name, price) {
       let postRequest = $.post("AddOptionalProduct", {name: name, price: price});

       postRequest.done(function (data, textStatus, jqXHR) {
           alert("optional product POST success");
       });

       postRequest.fail(function (data, textStatus, jqXHR) {
           alert("optional product POST fail");
       });

   }


});



// /*
// $(document).ready(function () {
//     $.ajaxSetup({cache: false});
//
//     $("#submitServicePackage").click(
//         function (event) {
//             event.preventDefault();
//             //let id = $("#id").val(); // automatically generated?
//             let name = $("#servicePackageNameInput").val();
//             let validityPeriod = $("#validityPeriodInput").val();
//             addServicePackage(name, validityPeriod);
//         }
//     );
//
//
//     function checkSelectedServiceType() {
//         let fixed_phone = document.getElementById("fixed_phone");
//         let mobile_phone = document.getElementById("mobile_phone");
//         let fixed_internet = document.getElementById("fixed_internet");
//         let mobile_internet = document.getElementById("mobile_internet");
//
//         if(fixed_phone.checked == true) {
//             sessionStorage.setItem("serviceType", fixed_phone.val());
//             document.getElementById("smsExtraId").style.display = "none";
//             document.getElementById("smsInclId").style.display = "none";
//             document.getElementById("minExtraId").style.display = "none";
//             document.getElementById("minInclId").style.display = "none";
//             document.getElementById("gigExtraId").style.display = "none";
//             document.getElementById("gigInclId").style.display = "none";
//
//
//         } else if(mobile_phone.checked == true) {
//             sessionStorage.setItem("serviceType", mobile_phone.val());
//             document.getElementById("gigExtraId").style.display = "none";
//             document.getElementById("gigInclId").style.display = "none";
//         } else if(fixed_internet.checked == true) {
//             sessionStorage.setItem("serviceType", fixed_internet.val());
//             document.getElementById("smsExtraId").style.display = "none";
//             document.getElementById("smsInclId").style.display = "none";
//             document.getElementById("minExtraId").style.display = "none";
//             document.getElementById("minInclId").style.display = "none";
//
//         } else if(mobile_internet.checked == true) {
//             sessionStorage.setItem("serviceType", mobile_internet.val());
//             document.getElementById("smsExtraId").style.display = "none";
//             document.getElementById("smsInclId").style.display = "none";
//             document.getElementById("minExtraId").style.display = "none";
//             document.getElementById("minInclId").style.display = "none";
//
//
//         } else {
//             alert("select a type if submitting a new service?");
//         }
//     }
//
//
//
//     function addServicePackage(name, validityPeriod) {
//         let services = [];
//         let optional = [];
//         /!* TODO: Right now, this is static but this for-loop has to iterate over dynamic # of available services *!/
//         for(let i = 1; i < 10; i++) {
//             let checkbox = document.getElementById(('service' + i));
//             if(checkbox.checked == true) {
//                 services.push(checkbox.val());
//             }
//         }
//         /!* TODO: Right now this is static but this for-loop has to iterate over dynamic # of available optional products *!/
//         for(let i = 1; i < 10; i++) {
//             let checkbox = document.getElementById(('optional' + i));
//             if(checkbox.checked == true) {
//                 optional.push(checkbox.val());
//             }
//         }
//
//         let postRequest = $.post("AddServicePackage", {name: name, validityPeriod: validityPeriod, services: services, optional: optional});
//
//         postRequest.done(function (data, textStatus, jqXHR) {
//             let package_date = jqXHR.responseJSON;
//             alert("successfully added service package");
//
//         });
//
//         postRequest.fail(function (data, textStatus, jqXHR) {
//             let package_date = jqXHR.responseJSON;
//             alert("failed to service package");
//
//         });
//
//
//     }
//
//
//
//
//     $("#submitOptionalProduct").click(
//         function (event) {
//             event.preventDefault();
//             //let id = $("#id").val(); // automatically generated?
//             let name = $("#name").val();
//             let validityPeriod = $("#validityPeriod").val();
//         }
//     );
//
//     $("#addServiceButton").click(
//         function (event) {
//             event.preventDefault();
//             //let id = $("#id").val(); // automatically generated?
//             let type = $("#type").val();
//             let bp1 = $("#validityPeriod").val();
//             let bp2 = $("#optionalProducts").val();
//             let bp3 = $("#services").val();
//             let gigIncluded = $("#gigIncluded").val();
//             let minIncluded = $("#minIncluded").val();
//             let smsIncluded = $("#smsIncluded").val();
//             let gigExtra = $("#gigExtra").val();
//             let minExtra = $("#minExtra").val();
//             let smsExtra = $("#smsExtra").val();
//         }
//     );
//
//     function addOptionalProduct(name, validityPeriod) {
//         let postRequest = $.post("AddOptionalProduct", {name: name, validityPeriod: validityPeriod});
//
//         postRequest.done(function (data, textStatus, jqXHR) {
//             let product_data = jqXHR.responseJSON;
//             sessionStorage.setItem('name', product_data.name);
//             sessionStorage.setItem('validityPeriod', product_data.validityPeriod);
//         });
//
//         postRequest.fail(function (jqXHR, textStatus, errorThrown) {
//             //Spawn an error message after now login
//             let JSONText = jQuery.parseJSON(jqXHR.responseText)
//             alert("Adding Optional Product Err: " + jqXHR.responseText);
//         });
//     }
//
//     function addService(type, bp1, bp2, bp3, gig_incl, min_incl, sms_incl, gig_extra, min_extra, sms_extra) {
//         let postRequest = $.post("AddService", {
//             type: type,
//             bp1: bp1,
//             bp2: bp2,
//             bp3: bp2,
//             gig_incl: gig_incl,
//             min_incl: min_incl,
//             sms_incl: sms_incl,
//             gig_extra: gig_extra,
//             min_extra: min_extra,
//             sms_extra: sms_extra
//         });
//
//         postRequest.done(function (data, textStatus, jqXHR) {
//             checkSelectedServiceType();
//
//             //Here we save the data into the sessionStorage
//             let service_data = jqXHR.responseJSON;
//             sessionStorage.setItem('type', service_data.type);
//             sessionStorage.setItem('bp1', service_data.bp1);
//             sessionStorage.setItem('bp2', service_data.bp2);
//             sessionStorage.setItem('bp3', service_data.bp3);
//             sessionStorage.setItem('gig_incl', service_data.gig_incl);
//             sessionStorage.setItem('min_incl', service_data.min_incl);
//             sessionStorage.setItem('sms_incl', service_data.sms_incl);
//             sessionStorage.setItem('gig_extra', service_data.gig_extra);
//             sessionStorage.setItem('min_extra', service_data.min_extra);
//             sessionStorage.setItem('sms_extra', service_data.sms_extra);
//
//
//         });
//         postRequest.fail(function (jqXHR, textStatus, errorThrown) {
//             //Spawn an error message after now login
//             let JSONText = jQuery.parseJSON(jqXHR.responseText)
//             alert("Error: " + jqXHR.responseText);
//         });
//
//     }
// });
//
// */
