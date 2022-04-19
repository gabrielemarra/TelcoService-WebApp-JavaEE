package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.ServiceType;
import it.polimi.telco_webapp.auxiliary.exceptions.UserNotFoundException;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.services.ServiceService;
import it.polimi.telco_webapp.services.OrderService;
import it.polimi.telco_webapp.services.UserService;
import it.polimi.telco_webapp.services.ServicePackageService;
import it.polimi.telco_webapp.services.OptionalProductService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.json.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetAllOrders", value = "/GetAllOrders")
public class GetAllOrders extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OrderService")
    private OrderService orderService;

    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService packageService;

    @EJB(name = "it.polimi.db2.entities.services/ServiceService")
    private ServiceService serviceService;

    @EJB(name = "it.polimi.db2.entities.services/OptionalProductService")
    private OptionalProductService optionalProductService;


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

        try {
            //Integer userId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("user_id")));


            JsonArray allOrders = new JsonArray();

            List<Order> orders = orderService.getAllOrdersByUser(userService.getUserByEmail(request.getSession().getAttribute("user").toString()));

            if (orders != null) {
                // For each order associated with the user:
                for (Order order : orders) {
                    JsonArray oneOrder = new JsonArray();
                    JsonElement orderInfo = new JsonObject();
                    JsonArray servicesList = new JsonArray();
                    JsonArray optionsList = new JsonArray();
                    // Get the other info - JSON ELEMENT
                    orderInfo.getAsJsonObject().addProperty("status", order.getStatus().toString());
                    orderInfo.getAsJsonObject().addProperty("start_date", order.getSubscriptionStart().toString());
                    orderInfo.getAsJsonObject().addProperty("validity_period", order.getChosenValidityPeriod());
                    orderInfo.getAsJsonObject().addProperty("package_name", order.getPackageId().getName());

                    // Collect info for each service - JSON ARRAY
                    List<Service> services = order.getPackageId().getServices();
                    if (services != null) {
                        for (Service service : services) {
                            JsonElement serviceJSON = new JsonObject();
                            serviceJSON.getAsJsonObject().addProperty("type", service.getServiceType().toString());
                            if (service.getServiceType().equals(ServiceType.Fixed_Internet) | service.getServiceType().equals(ServiceType.Mobile_Internet)) {
                                serviceJSON.getAsJsonObject().addProperty("gig_incl", service.getGigIncluded());
                                serviceJSON.getAsJsonObject().addProperty("gig_extra", service.getGigExtra());
                            } else if (service.getServiceType().equals(ServiceType.Mobile_Phone)) {
                                serviceJSON.getAsJsonObject().addProperty("sms_incl", service.getGigIncluded());
                                serviceJSON.getAsJsonObject().addProperty("sms_extra", service.getGigExtra());
                                serviceJSON.getAsJsonObject().addProperty("min_incl", service.getGigIncluded());
                                serviceJSON.getAsJsonObject().addProperty("min_extra", service.getGigExtra());
                            } else {
                                // nothing?
                            }
                            servicesList.add(serviceJSON);
                        }
                    }

                    // Collect info for each option - JSON ARRAY
                    List<OptionalProduct> options = order.getOptionalServices();
                    if (options != null) {
                        for (OptionalProduct optionalProduct : options) {
                            JsonElement optionJSON = new JsonObject();
                            optionJSON.getAsJsonObject().addProperty("name", optionalProduct.getName());
                            optionsList.add(optionJSON);
                        }
                    }

                    // oneOrder is a JSON array for all the info for ONE order
                    // (order info, all services info, all options info)
                    oneOrder.add(orderInfo);
                    oneOrder.add(servicesList);
                    oneOrder.add(optionsList);

                    allOrders.add(oneOrder);
                }
            }

            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(allOrders));

        } catch (UserNotFoundException | EJBException e) { // TODO: change to correct exception...
            sendError(request, response, "UserNotFoundException", e.getCause().getMessage());
        }
    }
}
