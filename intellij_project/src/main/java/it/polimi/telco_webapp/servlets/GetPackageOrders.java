package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.services.OptionsPricesViewService;
import it.polimi.telco_webapp.services.ServicePackageService;

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
import java.util.List;

@WebServlet(name = "GetPackageOrders", value = "/GetPackageOrders")
public class GetPackageOrders extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OrderService")
    private OrderService orderService;
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService packageService;

    @EJB(name = "it.polimi.db2.entities.services/PackagePricesViewService")
    private PackagePricesViewService packagePricesService;
    @EJB(name = "it.polimi.db2.entities.services/OptionsPricesView")
    private OptionsPricesViewService optionsPricesService;


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
        try {
            ServicePackage packageOfInterest = packageService.getServicePackage(Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("package_id"))));

            int package_id = packageOfInterest.getId();
            List<Order> orders = orderService.getAllOrdersByPackage(packageOfInterest);

            Gson gson = new Gson();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonArray jsonArray = new JsonArray();
            int avgNumOptions = 0;
            for (int i = 0; i < orders.size(); i++) {
                JsonElement jsonElement = new JsonObject();
                Order temp = orders.get(i);

                if(temp.getStatus() != OrderStatus.REJECTED) {
                    jsonElement.getAsJsonObject().addProperty("order_id", temp.getId());

                    int validity = temp.getChosenValidityPeriod();
                    float baseCost = packagePricesService.getBasePrice(package_id, validity);
                    jsonElement.getAsJsonObject().addProperty("order_baseCost", baseCost);

                    float total = baseCost + optionsPricesService.getOptionsCost(temp.getId());

                    jsonElement.getAsJsonObject().addProperty("order_totalCost", total);
                    jsonElement.getAsJsonObject().addProperty("order_validity", temp.getChosenValidityPeriod());
                    jsonElement.getAsJsonObject().addProperty("order_num_options", temp.getOptionalServices().size());
                }
                jsonArray.add(jsonElement);
            }

            response.getWriter().println(gson.toJson(jsonArray));
        } catch (EJBException e) {
            sendError(request, response, "NoService", e.getCause().getMessage());
        }

    }
}
