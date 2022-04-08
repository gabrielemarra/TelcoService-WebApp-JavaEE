package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.exceptions.NoServicePackageFound;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.services.ServicePackageService;
import it.polimi.telco_webapp.services.ServiceService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetAvailableServicePackages", value = "/GetAvailableServicePackages")
public class GetAvailableServicePackages extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService servicePackageService;

    @EJB(name = "it.polimi.db2.entities.services/ServiceService")
    private ServiceService serviceService;

    /**
     * Method to handle errors, send json with error info
     *
     * @param request   request
     * @param response  response
     * @param errorType type of error
     * @param errorInfo information about the error
     * @throws IOException if there are problems redirecting
     */
    protected void sendError(HttpServletRequest request, HttpServletResponse response, String errorType, String errorInfo) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("errorType", errorType);
        jsonObject.addProperty("errorInfo", errorInfo);

        response.getWriter().println(jsonObject);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ServicePackage> packages = servicePackageService.getAllServicePackages();
            Gson gson = new Gson();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonArray jsonArray = new JsonArray();

            for (ServicePackage aPackage : packages) {

                Double packagePrice1 = (double) 0;
                Double packagePrice2 = (double) 0;
                Double packagePrice3 = (double) 0;
                JsonArray jsonArrayServices = new JsonArray();

                // TODO: see HOW this info is being used in the JS. There is potential to get get the prices of the
                //  package (sum of all the included services) via the pacakge prices VIEW instead of summing all the
                //  services in a for loop BUT only if the JS does NOT use the prices of each service individually
                for (Service service : aPackage.getServices()) {
                    //Sum the prices
                    packagePrice1 += service.getBasePrice1();
                    packagePrice2 += service.getBasePrice2();
                    packagePrice3 += service.getBasePrice3();

                    //Store some properties about the services
                    JsonElement jsonService = new JsonObject();
                    jsonService.getAsJsonObject().addProperty("id", service.getId());
                    jsonService.getAsJsonObject().addProperty("serviceType", service.getServiceType().toString());

                    jsonArrayServices.add(jsonService);
                }

                JsonArray jsonArrayOptional = new JsonArray();

                for (OptionalProduct optionalProduct : aPackage.getOptions()) {

                    //Store some properties about the services
                    JsonElement jsonOptional = new JsonObject();
                    jsonOptional.getAsJsonObject().addProperty("opt_id", optionalProduct.getId());
                    jsonOptional.getAsJsonObject().addProperty("opt_cost", optionalProduct.getPrice());
                    jsonOptional.getAsJsonObject().addProperty("opt_name", optionalProduct.getName());

                    jsonArrayOptional.add(jsonOptional);
                }

                JsonElement jsonElement = new JsonObject();

                jsonElement.getAsJsonObject().addProperty("package_name", aPackage.getName());
                jsonElement.getAsJsonObject().addProperty("package_id", aPackage.getId());
                jsonElement.getAsJsonObject().addProperty("default_validity_period", aPackage.getValidityPeriod());

                JsonArray jsonPrices = new JsonArray();
                jsonPrices.add(packagePrice1);
                jsonPrices.add(packagePrice2);
                jsonPrices.add(packagePrice3);
                jsonElement.getAsJsonObject().add("prices", jsonPrices);

                jsonElement.getAsJsonObject().add("services", jsonArrayServices);
                jsonElement.getAsJsonObject().add("optional_products", jsonArrayOptional);

                /*
                 * TODO: I think we also need to add Services and Optional Products to the json element... for now those
                 *  objects have not been "serialized is in json-pretty way...
                 * */

                jsonArray.add(jsonElement);
            }

            response.getWriter().println(gson.toJson(jsonArray));
        } catch (NoServicePackageFound | EJBException e) {
            sendError(request, response, "NoServicePackage", e.getCause().getMessage());
        }

    }
}
