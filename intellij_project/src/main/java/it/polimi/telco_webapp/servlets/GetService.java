package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.services.ServiceService;
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

@WebServlet(name = "GetService", value = "/GetService")
public class GetService extends HttpServlet {
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
        //doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Service oneService = serviceService.getService(Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("id"))));

            Gson gson = new Gson();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("bp1", oneService.getBasePrice1().toString());
            jsonElement.getAsJsonObject().addProperty("bp2", oneService.getBasePrice2().toString());
            jsonElement.getAsJsonObject().addProperty("bp3", oneService.getBasePrice3().toString());
            jsonElement.getAsJsonObject().addProperty("service_id", oneService.getId());
            jsonElement.getAsJsonObject().addProperty("type", oneService.getServiceType().toString());

            response.getWriter().println(gson.toJson(jsonElement));
        } catch (EJBException e) {
            sendError(request, response, "NoService", e.getCause().getMessage());
        }

    }
}
