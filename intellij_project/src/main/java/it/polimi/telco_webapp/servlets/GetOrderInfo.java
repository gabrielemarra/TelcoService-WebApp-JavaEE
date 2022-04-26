package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.auxiliary.exceptions.UserNotFoundException;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.services.OrderService;
import it.polimi.telco_webapp.services.PackagePricesViewService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "GetOrderInfo", value = "/GetOrderInfo")
public class GetOrderInfo extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OrderService")
    private OrderService orderService;

    @EJB(name = "it.polimi.db2.entities.services/PackagePricesViewService")
    private PackagePricesViewService pricesService;

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

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        try { // instead: get the order info from dataset in the html table, buy button just invokes a "rebuy" servlet
            // which instead finds the rejected order, calls the exteral service, and changes that status in the DB from rejected to confirmed?
            Integer orderId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("order_id")));

            Order order = orderService.getOrder(orderId);

            JsonElement orderInfo = new JsonObject();
            orderInfo.getAsJsonObject().addProperty("status", order.getStatus().toString());
            orderInfo.getAsJsonObject().addProperty("startDate", order.getSubscriptionStart().toString());

            int package_id = order.getPackageId().getId();
            int period = order.getChosenValidityPeriod();
            BigDecimal price = pricesService.getBasePrice(package_id, period);
            orderInfo.getAsJsonObject().addProperty("total_cost", price);
            orderInfo.getAsJsonObject().addProperty("package_id", order.getPackageId().getId());
            orderInfo.getAsJsonObject().addProperty("validity_period", order.getChosenValidityPeriod());

            List <OptionalProduct> options = order.getOptionalServices();
            JsonArray allInfo = new JsonArray();
            allInfo.add(orderInfo);

            for(int i = 0; i <options.size(); i++) {
                JsonElement option = new JsonObject();
                option.getAsJsonObject().addProperty("name", options.get(i).getName());
                option.getAsJsonObject().addProperty("price", options.get(i).getPrice().toString());
                option.getAsJsonObject().addProperty("id", options.get(i).getId());
                allInfo.add(option);
            }
            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(allInfo));

        } catch (UserNotFoundException | EJBException e) { // TODO: change to correct exception...
            sendError(request, response, "UserNotFoundException", e.getCause().getMessage());
        }
    }
}
