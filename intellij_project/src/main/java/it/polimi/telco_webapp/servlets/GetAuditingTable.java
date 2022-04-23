package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.AuditView;
import it.polimi.telco_webapp.entities.BestSellerView;
import it.polimi.telco_webapp.services.AuditViewService;
import it.polimi.telco_webapp.services.BestSellerViewService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;


import java.io.IOException;

@WebServlet(name = "GetAuditingTable", value = "/GetAuditingTable")
public class GetAuditingTable extends HttpServlet {

    @EJB(name = "it.polimi.telco_webapp.services/AuditViewService")
    private AuditViewService viewService;


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
            JsonArray allAlerts = new JsonArray();

            List<AuditView> audit = viewService.getAll();
            for(int i = 0; i < audit.size(); i++) {
                AuditView oneRow = audit.get(i);
                JsonElement jsonElement = new JsonObject();
                jsonElement.getAsJsonObject().addProperty("user_id", oneRow.getUserId());
                jsonElement.getAsJsonObject().addProperty("username", oneRow.getUsername());
                jsonElement.getAsJsonObject().addProperty("email", oneRow.getEmail());
                jsonElement.getAsJsonObject().addProperty("delinq_amount", oneRow.getTotal());
                jsonElement.getAsJsonObject().addProperty("num_rej", oneRow.getNumRejected());
                allAlerts.add(jsonElement);
            }

            response.getWriter().println(gson.toJson(allAlerts));

        } catch (EJBException e) {
            sendError(request, response, "No Option Found", e.getCause().getMessage());
        }
    }
}


