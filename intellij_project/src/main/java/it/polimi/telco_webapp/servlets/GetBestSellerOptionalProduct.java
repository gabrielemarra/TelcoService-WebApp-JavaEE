package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.BestSellerView;
import it.polimi.telco_webapp.entities.ServicePackageView;
import it.polimi.telco_webapp.services.BestSellerViewService;
import it.polimi.telco_webapp.services.ServicePackageService;
import it.polimi.telco_webapp.services.ServicePackageViewService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "GetBestSellerOptionalProduct", value = "/GetBestSellerOptionalProduct")
public class GetBestSellerOptionalProduct extends HttpServlet {

    @EJB(name = "it.polimi.telco_webapp.services/BestSellerViewService")
    private BestSellerViewService viewService;


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
            Gson gson = new Gson();

            BestSellerView bestSeller = viewService.getBestSeller();
            JsonElement jsonElement = new JsonObject();
            jsonElement.getAsJsonObject().addProperty("opt_id", bestSeller.getOptId());
            jsonElement.getAsJsonObject().addProperty("opt_name", bestSeller.getOptName());
            jsonElement.getAsJsonObject().addProperty("sales_value", bestSeller.getSalesValue().toString());

            response.getWriter().println(gson.toJson(jsonElement));

        } catch (EJBException e) {
            sendError(request, response, "No Option Found", e.getCause().getMessage());
        }
    }
}


