package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.ExternalPaymentService;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.services.OrderService;
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


@WebServlet(name = "Transact", value = "/Transact")
public class Transact extends HttpServlet {
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

        try{
            ExternalPaymentService externalService = new ExternalPaymentService();
            boolean isOrderRejected = Boolean.parseBoolean(StringEscapeUtils.escapeJava(request.getParameter("isOrderRejected")));

            int orderId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("order_id")));
            if (!externalService.call(isOrderRejected)) {
                orderService.changeOrderStatus(orderId, OrderStatus.CONFIRMED);
            } else {
                orderService.changeOrderStatus(orderId, OrderStatus.REJECTED);
            }

            Order order = orderService.getOrder(orderId);
            Gson gson = new Gson();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");


            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("order_id", order.getId());
            jsonElement.getAsJsonObject().addProperty("order_status", order.getStatus().toString());

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
