package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.services.InsolventUsersViewService;
import it.polimi.telco_webapp.services.UserService;
import it.polimi.telco_webapp.entities.InsolventUsersView;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetInsolventUsersReport", value = "/GetInsolventUsersReport")
public class GetInsolventUsersReport extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/InsolventUsersViewService")
    private InsolventUsersViewService viewService;
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;




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

            List<InsolventUsersView> rowItems = viewService.getAll();
            for(int i = 0; i < rowItems.size(); i++) {
                JsonElement jsonElement = new JsonObject();
                InsolventUsersView row = rowItems.get(i);

                jsonElement.getAsJsonObject().addProperty("user_id", row.getUserId());
                jsonElement.getAsJsonObject().addProperty("user_name", userService.getUserById(row.getUserId()).getName());
                jsonElement.getAsJsonObject().addProperty("num_rej_orders", row.getNumOrders());
                jsonElement.getAsJsonObject().addProperty("delinquent_total", row.getDelinquentTotal());

                jsonArray.add(jsonElement);
            }
            response.getWriter().println(gson.toJson(jsonArray));

        } catch (EJBException e) {
            sendError(request, response, "No Option Found", e.getCause().getMessage());
        }
    }
}


