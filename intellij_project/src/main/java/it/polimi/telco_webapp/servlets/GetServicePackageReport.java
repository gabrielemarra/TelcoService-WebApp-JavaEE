package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.entities.ServicePackageView;
import it.polimi.telco_webapp.services.ServicePackageService;
import it.polimi.telco_webapp.services.ServicePackageViewService;
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

@WebServlet(name = "GetServicePackageReport", value = "/GetServicePackageReport")
public class GetServicePackageReport extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageViewService")
    private ServicePackageViewService viewService;
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

            List<ServicePackageView> rowItems = viewService.getAll();
            for(int i = 0; i < rowItems.size(); i++) {
                JsonElement jsonElement = new JsonObject();
                ServicePackageView row = rowItems.get(i);
                jsonElement.getAsJsonObject().addProperty("package_id", row.getPackage_id());
                jsonElement.getAsJsonObject().addProperty("package_name", packageService.getServicePackage(row.getPackage_id()).getName());
                jsonElement.getAsJsonObject().addProperty("purchases_period1", row.getPurchasesPeriod1());
                jsonElement.getAsJsonObject().addProperty("purchases_period2", row.getPurchasesPeriod2());
                jsonElement.getAsJsonObject().addProperty("purchases_period3", row.getPurchasesPeriod3());
                jsonElement.getAsJsonObject().addProperty("sales_base", row.getSalesBase());
                jsonElement.getAsJsonObject().addProperty("sales_total", row.getSalesTotal());
                jsonArray.add(jsonElement);
            }
            response.getWriter().println(gson.toJson(jsonArray));

        } catch (EJBException e) {
            sendError(request, response, "No Option Found", e.getCause().getMessage());
        }
    }
}


