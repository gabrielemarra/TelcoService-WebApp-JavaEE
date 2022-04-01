package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.services.OptionalProductService;
import it.polimi.telco_webapp.services.OrderService;
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
import java.util.List;

@WebServlet(name = "GetOptionValue", value = "/GetOptionValue")
public class GetOptionValue extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OptionService")
    private OptionalProductService optionService;
    @EJB(name = "it.polimi.db2.entities.services/OrderService")
    private OrderService orderService;

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
            // Get the option object from the option ID
            Integer optionId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("option_id")));
            OptionalProduct option = optionService.getOption(optionId);

            // Multiply the price of the option by the # of times the option was ordered (the size of the list)
            BigDecimal timesOrdered = BigDecimal.valueOf(orderService.getAllOrdersByOption(option).size());
            BigDecimal value = option.getPrice().multiply(timesOrdered);

            Gson gson = new Gson();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("value", value);
            response.getWriter().println(gson.toJson(jsonElement));
        } catch (EJBException e) {
            sendError(request, response, "No Orders Found with the Provided Option", e.getCause().getMessage());
        }

    }
}