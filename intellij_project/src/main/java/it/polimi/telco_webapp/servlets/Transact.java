package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

            Integer orderId = (Integer) request.getSession().getAttribute("order_id");
            if (true) {// externalService.call(isOrderRejected) == false {
                orderService.changeOrderStatus(orderId, OrderStatus.CONFIRMED);
            } else {
                orderService.changeOrderStatus(orderId, OrderStatus.REJECTED);
            }

        } catch (EJBException e) {
            sendError(request, response, "InternalDBErrorException", e.getCause().getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
