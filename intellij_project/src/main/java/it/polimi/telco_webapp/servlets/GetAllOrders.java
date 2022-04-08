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

            List <Order> orders = orderService.getAllOrdersByUser(userService.getUserByEmail(request.getSession().getAttribute("user").toString()));
            // For each order associated with the user:
            for(int i = 0; i < orders.size(); i++) {
                JsonArray oneOrder = new JsonArray();
                JsonElement orderInfo = new JsonObject();
                JsonArray servicesList = new JsonArray();
                JsonArray optionsList = new JsonArray();
                // Get the other info - JSON ELEMENT
                orderInfo.getAsJsonObject().addProperty("status", orders.get(i).getStatus().toString());
                orderInfo.getAsJsonObject().addProperty("start_date", orders.get(i).getSubscriptionStart().toString());
                orderInfo.getAsJsonObject().addProperty("validity_period", orders.get(i).getChosenValidityPeriod());
                orderInfo.getAsJsonObject().addProperty("package_name", orders.get(i).getPackageId().getName());

                // Collect info for each service - JSON ARRAY
                List<Service> services = orders.get(i).getPackageId().getServices();
                for(int j = 0; j < services.size(); j++) {
                    JsonElement serviceJSON = new JsonObject();
                    serviceJSON.getAsJsonObject().addProperty("type",services.get(i).getServiceType().toString());
                    if(services.get(i).getServiceType().equals(ServiceType.Fixed_Internet) | services.get(i).getServiceType().equals(ServiceType.Mobile_Internet)) {
                        serviceJSON.getAsJsonObject().addProperty("gig_incl", services.get(i).getGigIncluded());
                        serviceJSON.getAsJsonObject().addProperty("gig_extra", services.get(i).getGigExtra());
                    } else if(services.get(i).getServiceType().equals(ServiceType.Mobile_Phone) ) {
                        serviceJSON.getAsJsonObject().addProperty("sms_incl", services.get(i).getGigIncluded());
                        serviceJSON.getAsJsonObject().addProperty("sms_extra", services.get(i).getGigExtra());
                        serviceJSON.getAsJsonObject().addProperty("min_incl", services.get(i).getGigIncluded());
                        serviceJSON.getAsJsonObject().addProperty("min_extra", services.get(i).getGigExtra());
                    } else {
                        // nothing?
                    }
                    servicesList.add(serviceJSON);
                }

                // Collect info for each option - JSON ARRAY
                List<OptionalProduct> options = orders.get(i).getOptionalServices();
                for(int j = 0; j < options.size(); j++) {
                    JsonElement optionJSON = new JsonObject();
                    optionJSON.getAsJsonObject().addProperty("name",options.get(i).getName());
                    optionsList.add(optionJSON);
                }

                // oneOrder is a JSON array for all the info for ONE order
                // (order info, all services info, all options info)
                oneOrder.add(orderInfo);
                oneOrder.add(servicesList);
                oneOrder.add(optionsList);

                allOrders.add(oneOrder);
            }

            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(allOrders));

        } catch (UserNotFoundException | EJBException e) { // TODO: change to correct exception...
            sendError(request, response, "UserNotFoundException", e.getCause().getMessage());
        }
    }
}
