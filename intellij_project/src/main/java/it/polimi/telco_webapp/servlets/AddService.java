package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.services.ServiceService;
import it.polimi.telco_webapp.entities.Service;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;


@WebServlet(name = "AddService", value = "/AddService")
public class AddService extends HttpServlet {
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
        String type = StringEscapeUtils.escapeJava(request.getParameter("planType"));
        // Doubles
        Double bp1 = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("bp1")));
        Double bp2 = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("bp2")));
        Double bp3 = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("bp3")));

        Double gigExtra, minExtra, smsExtra;
        Integer gigIncl, minIncl, smsIncl;

        gigIncl = minIncl = smsIncl = 0;
        gigExtra = minExtra = smsExtra = 0.0;


        switch (type) {
            case "Fixed_Phone":
                //
                break;
            case "Mobile_Phone":
                minIncl = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("minIncl")));
                smsIncl = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("smsIncl")));
                minExtra = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("minExtra")));
                smsExtra = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("smsExtra")));
                break;
            case "Fixed_Internet":
            case "Mobile_Internet":
                gigIncl = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("gigIncl")));
                gigExtra = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("gigExtra")));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        try{
            int service_id = serviceService.insertNewService(type, bp1, bp2, bp3, gigIncl, minIncl, smsIncl, gigExtra, minExtra, smsExtra).getId();

            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("service_id", service_id);
            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(jsonElement));

    } catch (EJBException e) {
            sendError(request, response, "InternalDBErrorException", e.getCause().getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
