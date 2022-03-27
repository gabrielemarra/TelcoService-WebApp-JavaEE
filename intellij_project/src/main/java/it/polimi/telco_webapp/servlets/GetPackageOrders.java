package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.Option;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.services.ServicePackageService;

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
import java.util.List;

@WebServlet(name = "GetPackageOrders", value = "/GetPackageOrders")
public class GetPackageOrders extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OrderService")
    private OrderService orderService;
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService packageService;

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
            ServicePackage packageOfInterest = packageService.getServicePackage(Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("package_id"))));

            List<Order> orders = orderService.getAllOrdersByPackage(packageOfInterest);
            //if(orders != null) {


            Gson gson = new Gson();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < orders.size(); i++) {
                JsonElement jsonElement = new JsonObject();
                Order temp = orders.get(i);
                if(temp.getStatus() != OrderStatus.REJECTED) {
                    jsonElement.getAsJsonObject().addProperty("order_id", temp.getId());
                    jsonElement.getAsJsonObject().addProperty("order_baseCost", temp.getBaseCost());
                    jsonElement.getAsJsonObject().addProperty("order_totalCost", temp.getTotalPrice());
                    jsonElement.getAsJsonObject().addProperty("order_validity", temp.getChosenValidityPeriod());
                }
                jsonArray.add(jsonElement);
            }
            //}

            response.getWriter().println(gson.toJson(jsonArray));
        } catch (EJBException e) {
            sendError(request, response, "NoService", e.getCause().getMessage());
        }

    }
}
