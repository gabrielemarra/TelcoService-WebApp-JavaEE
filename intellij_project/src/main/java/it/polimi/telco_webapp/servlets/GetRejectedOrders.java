package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.auxiliary.exceptions.UserNotFoundException;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.entities.User;
import it.polimi.telco_webapp.services.OptionsPricesViewService;
import it.polimi.telco_webapp.services.PackagePricesViewService;
import it.polimi.telco_webapp.services.SuspendedOrdersViewService;
import it.polimi.telco_webapp.services.UserService;
import it.polimi.telco_webapp.entities.Order;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

@WebServlet(name = "GetRejectedOrders", value = "/GetRejectedOrders")
public class GetRejectedOrders extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

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

        String email = (String)request.getSession().getAttribute("user");
        User userObj = userService.getUserByEmail(email);
        List<Order> rejectedOrders = null;

        try {
            rejectedOrders = userService.getRejectedOrders(userObj);

        } catch (UserNotFoundException | EJBException e) {
            sendError(request, response, "UserNotFoundException", e.getCause().getMessage());
        }

        JsonArray rejectedOrdersJson = new JsonArray();
        for(int i = 0; i < rejectedOrders.size(); i++) {

            JsonElement jsonElement = new JsonObject();
            /* TODO: How to add LocalDateTime object as property */
            //jsonElement.getAsJsonObject().addProperty("timestamp", temp.getTimestamp());
            jsonElement.getAsJsonObject().addProperty("order_id", rejectedOrders.get(i).getId());
            jsonElement.getAsJsonObject().addProperty("service_package_name", rejectedOrders.get(i).getPackageId().getName());
            rejectedOrdersJson.add(jsonElement);

        }
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(rejectedOrdersJson));

    }
}
