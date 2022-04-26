package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.exceptions.UserNotFoundException;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "GetAllRejectedOrders", value = "/GetAllRejectedOrders")
public class GetAllRejectedOrders extends HttpServlet {
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
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        try {
            List<Order> rejectedOrders = orderService.getAllRejectedOrders();
            JsonArray rejectedOrdersJson = new JsonArray();
            for(int i = 0; i < rejectedOrders.size(); i++) {

                JsonElement jsonElement = new JsonObject();
                /* TODO: How to add LocalDateTime object as property */
                //jsonElement.getAsJsonObject().addProperty("timestamp", temp.getTimestamp());
                int package_id = rejectedOrders.get(i).getPackageId().getId();
                int validity = rejectedOrders.get(i).getChosenValidityPeriod();
                BigDecimal total_price = pricesService.getBasePrice(package_id, validity);
                jsonElement.getAsJsonObject().addProperty("order_id", rejectedOrders.get(i).getId());
                jsonElement.getAsJsonObject().addProperty("service_package_name", rejectedOrders.get(i).getPackageId().getName());
                // TODO: we care are TOTAL price, not base price
                jsonElement.getAsJsonObject().addProperty("total_price", total_price);
                jsonElement.getAsJsonObject().addProperty("user_id", rejectedOrders.get(i).getUser().getId());

                rejectedOrdersJson.add(jsonElement);

            }
            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(rejectedOrdersJson));

        } catch (UserNotFoundException | EJBException e) { // TODO: change to correct exception...
            sendError(request, response, "UserNotFoundException", e.getCause().getMessage());
        }
    }
}
