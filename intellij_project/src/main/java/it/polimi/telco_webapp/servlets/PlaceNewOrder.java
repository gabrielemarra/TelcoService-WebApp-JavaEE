package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.telco_webapp.auxiliary.ExternalPaymentService;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.entities.User;
import it.polimi.telco_webapp.services.OptionalProductService;
import it.polimi.telco_webapp.services.OrderService;
import it.polimi.telco_webapp.services.ServicePackageService;
import it.polimi.telco_webapp.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;

@WebServlet(name = "PlaceNewOrder", value = "/PlaceNewOrder")
public class PlaceNewOrder extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OrderService")
    private OrderService orderService;

    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService servicePackageService;

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
        try {
            /*let postRequest = $.post("PlaceNewOrder", {
                email: sessionStorage.getItem("email",),
                package_id: package_id,
                validity_period: validity_period,
                total_cost: total_cost,
                optionalProducts: JSON.stringify(optionalProducts),
                start_date: startDate,
                is_order_rejected: isOrderRejected */

            Gson gson = new Gson();

            String email = escapeJava(request.getParameter("email"));
            Integer package_id = Integer.parseInt(escapeJava(request.getParameter("package_id")));
            Integer validity = Integer.parseInt(escapeJava(request.getParameter("validity_period")));
            LocalDate startDate = LocalDate.parse(escapeJava(request.getParameter("start_date")));

            String optionsProdJSON = escapeJava(request.getParameter("optionalProducts"));
            Type listType = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            List<Integer> optionalProductsIDs = new Gson().fromJson(optionsProdJSON, listType);

            List<OptionalProduct> optionalProductList = new ArrayList<>();

            for (Integer optionalProductsID : optionalProductsIDs) {
                optionalProductList.add(optionService.getOption(optionalProductsID));
            }

            User user = userService.getUserByEmail(email);
            //todo do a check here that the email gotten from the request is the same user that is saved in the session?

            ServicePackage servicePackage = servicePackageService.getServicePackage(package_id);


            // TODO: Caused by: java.lang.NullPointerException: Cannot invoke "it.polimi.telco_webapp.entities.OptionsPricesView.getSumOfOptionsPurchased()" because the return value of "jakarta.persistence.EntityManager.find(java.lang.Class, Object)" is null

            Order pendingOrder = orderService.insertNewOrder(startDate, user, servicePackage, new ArrayList<OptionalProduct>(), validity);

            int pendingOrderId = pendingOrder.getId();

            request.getSession().setAttribute("order_id", pendingOrderId);

            ExternalPaymentService externalPaymentService = new ExternalPaymentService();
            boolean isOrderRejected = Boolean.parseBoolean(escapeJava(request.getParameter("is_order_rejected")));

            if (!externalPaymentService.call(isOrderRejected)) {
                orderService.changeOrderStatus(pendingOrderId, OrderStatus.CONFIRMED);
            } else {
                orderService.changeOrderStatus(pendingOrderId, OrderStatus.REJECTED);
            }

            Order newOrder = orderService.getOrder(pendingOrderId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");


            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("order_id", newOrder.getId());
            jsonElement.getAsJsonObject().addProperty("order_status", newOrder.getStatus().toString());

            response.getWriter().println(gson.toJson(jsonElement));
        } catch (EJBException e) {
            sendError(request, response, "NoService", e.getCause().getMessage());

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
