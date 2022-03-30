package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.polimi.telco_webapp.services.OptionalProductService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.math.BigDecimal;


@WebServlet(name = "AddOptionalProduct", value = "/AddOptionalProduct")
public class AddOptionalProduct extends HttpServlet {
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
        /* QUESTION: difference between passing info via getParameter() versus getting from items put in sessionStorage() */
        String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
        String priceStr = StringEscapeUtils.escapeJava(request.getParameter("price"));

        try {
            int id = optionService.insertNewOption(name, new BigDecimal(priceStr)).getId();
            Gson gson = new Gson();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("id", id);
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
