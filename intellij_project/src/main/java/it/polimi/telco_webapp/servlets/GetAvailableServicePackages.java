package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.exceptions.NoServicePackageFound;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.services.ServicePackageService;
import it.polimi.telco_webapp.services.UserService;
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
            for (int i = 0; i < packages.size(); i++) {

                JsonElement jsonElement = new JsonObject();
                ServicePackage temp = packages.get(i);

                jsonElement.getAsJsonObject().addProperty("name", temp.getName());
                jsonElement.getAsJsonObject().addProperty("package_id", temp.getId());
                jsonElement.getAsJsonObject().addProperty("validity_period", temp.getValidityPeriod());
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