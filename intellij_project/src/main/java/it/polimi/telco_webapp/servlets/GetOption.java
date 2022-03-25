package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.Option;
import it.polimi.telco_webapp.services.OptionService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

@WebServlet(name = "GetOption", value = "/GetOption")
public class GetOption extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OptionService")
    private OptionService optionService;

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
            Option option = optionService.getOption(Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("id"))));
            Gson gson = new Gson();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("name", option.getName());
            jsonElement.getAsJsonObject().addProperty("price", option.getPrice());

            response.getWriter().println(gson.toJson(jsonElement));
        } catch (EJBException e) {
            sendError(request, response, "No Option Found", e.getCause().getMessage());
        }

    }
}
