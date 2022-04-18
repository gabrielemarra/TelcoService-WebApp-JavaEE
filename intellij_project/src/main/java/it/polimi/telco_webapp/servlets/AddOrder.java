package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.User;
import it.polimi.telco_webapp.services.OrderService;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.services.ServicePackageService;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AddOrder", value = "/AddOrder")
public class AddOrder extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OrderService")
    private OrderService orderService;

    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService servicePackageService;

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
/*
            let postRequest = $.post("AddOrder", {
                    email: sessionStorage.getItem("email", ),
                    package_id: package_id,
                    validity_period:validity_period,
                    total_cost: total_cost,
                    optionalProducts: optionalProducts,
                    start_date: startDate});
            */


            String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
            String package_id = StringEscapeUtils.escapeJava(request.getParameter("package_id"));
            Integer validity = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("validity_period")));
            Float total = Float.parseFloat(StringEscapeUtils.escapeJava(request.getParameter("total_cost")));
            //String options = StringEscapeUtils.escapeJava(request.getParameter("optionalProducts"));
            //LocalDate start = LocalDate.parse(StringEscapeUtils.escapeJava(request.getParameter("start_date")));
            User user = userService.getUserByEmail(email);
            // do a check here that the email gotten from the request is the same user that is saved in the session?

            Integer packageId = Integer.parseInt(package_id);
            ServicePackage servicePackage = servicePackageService.getServicePackage(packageId);


            // TODO: get the optional products from the get request...
            // public Order insertNewOrder(LocalDate subscriptionStartDate, User user, ServicePackage servicePackage, List<OptionalProduct> optionalProductList) {
            Order pendingOrder = orderService.insertNewOrder(LocalDate.now(), user, servicePackage, new ArrayList<OptionalProduct>(), validity);
            request.getSession().setAttribute("order_id", pendingOrder.getId());
            request.getSession().setAttribute("pendingOrder_S", true);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Gson gson = new Gson();

            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("order_id", pendingOrder.getId());

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
