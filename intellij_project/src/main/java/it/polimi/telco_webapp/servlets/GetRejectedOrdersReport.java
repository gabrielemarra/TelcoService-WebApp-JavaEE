package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.services.*;
import it.polimi.telco_webapp.views.ServicePackageView;
import it.polimi.telco_webapp.views.SuspendedOrdersView;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetRejectedOrdersReport", value = "/GetRejectedOrdersReport")
public class GetRejectedOrdersReport extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageViewService")
    private SuspendedOrdersViewService viewService;
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService packageService;



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
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            List<SuspendedOrdersView> rowItems = viewService.getAll();
            for(int i = 0; i < rowItems.size(); i++) {
                JsonElement jsonElement = new JsonObject();
                SuspendedOrdersView row = rowItems.get(i);

                jsonElement.getAsJsonObject().addProperty("order_id", row.getOrderId());
                jsonElement.getAsJsonObject().addProperty("user_id", row.getUserId());
                jsonElement.getAsJsonObject().addProperty("user_name", userService.getUserById(row.getUserId()).getName());
                jsonElement.getAsJsonObject().addProperty("package_id", row.getPackageId());
                jsonElement.getAsJsonObject().addProperty("package_name", packageService.getServicePackage(row.getPackageId()).getName());
                jsonElement.getAsJsonObject().addProperty("total", row.getTotal());
                jsonArray.add(jsonElement);
            }
            response.getWriter().println(gson.toJson(jsonArray));

        } catch (EJBException e) {
            sendError(request, response, "No Option Found", e.getCause().getMessage());
        }
    }
}


