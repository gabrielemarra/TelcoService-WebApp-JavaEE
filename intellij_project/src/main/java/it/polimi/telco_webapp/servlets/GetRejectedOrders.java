package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.auxiliary.exceptions.UserNotFoundException;
import it.polimi.telco_webapp.entities.User;
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
        User user = null;
        try {
            //boolean insolvent = false;
            //user = userService.getUserByUsername("dummy");

        } catch (UserNotFoundException | EJBException e) {
            sendError(request, response, "UserNotFoundException", e.getCause().getMessage());
        }
/*
        List<Order> ordersFromUser = user.getOrders();
        List<Order> rejectedOrders = new ArrayList<Order>();

        for(int i = 0; i < ordersFromUser.size(); i++) {
            Order temp = ordersFromUser.get(i);
            if(temp.getStatus() == OrderStatus.REJECTED) {
                rejectedOrders.add(temp);
                //insolvent = true;
            }
        }

        Gson gson = new Gson();
        if(!rejectedOrders.isEmpty()) {

            RejectedOrdersContent rejectedOrdersContent = new GetRejectedOrdersContent(rejectedOrders);
            String jsonHome - new Gson().toJson(rejectedOrdersContent);
            out.write(jsonHome);


            //String jsonElement = gson.toJson(rejectedOrders);



        }
* */


    }
}
