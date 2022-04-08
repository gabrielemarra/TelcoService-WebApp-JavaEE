package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.services.ServicePackageService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetPackage", value = "/GetPackage")
public class GetPackage extends HttpServlet {
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
        //doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");


        try {
            ServicePackage servicePackage = servicePackageService.getServicePackage(Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("package_id"))));
            List <Service> services = servicePackage.getServices();
            Gson gson = new Gson();
            JsonArray packageInfo = new JsonArray();
            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("name", servicePackage.getName());
            packageInfo.add(jsonElement);
            for(int i = 0; i < services.size(); i++) {
                JsonElement serviceJson = new JsonObject();
                serviceJson.getAsJsonObject().addProperty("type", services.get(i).getServiceType().toString());
                serviceJson.getAsJsonObject().addProperty("bp1", services.get(i).getBasePrice1());
                serviceJson.getAsJsonObject().addProperty("bp2", services.get(i).getBasePrice2());
                serviceJson.getAsJsonObject().addProperty("bp3", services.get(i).getBasePrice3());
                packageInfo.add(serviceJson);
            }


            response.getWriter().println(gson.toJson(packageInfo));
        } catch (EJBException e) {
            sendError(request, response, "NoService", e.getCause().getMessage());
        }

    }
}
