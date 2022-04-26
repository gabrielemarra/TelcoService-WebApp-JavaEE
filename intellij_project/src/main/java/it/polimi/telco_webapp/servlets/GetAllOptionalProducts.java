package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.services.OptionalProductService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetAllOptionalProducts", value = "/GetAllOptionalProducts")
public class GetAllOptionalProducts extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OptionalProductService")
    private OptionalProductService optionService;

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
            List<OptionalProduct> options = optionService.getAllOptions();

            Gson gson = new Gson();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < options.size(); i++) {

                JsonElement jsonElement = new JsonObject();
                OptionalProduct temp = options.get(i);

                jsonElement.getAsJsonObject().addProperty("name", temp.getName());
                jsonElement.getAsJsonObject().addProperty("price", temp.getPrice().toString());
                jsonElement.getAsJsonObject().addProperty("option_id", temp.getId());
                jsonArray.add(jsonElement);
            }

            response.getWriter().println(gson.toJson(jsonArray));
        } catch (EJBException e) {
            sendError(request, response, "No Options", e.getCause().getMessage());
        }

    }
}
