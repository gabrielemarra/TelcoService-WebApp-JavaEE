package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.services.ServiceService;
import it.polimi.telco_webapp.entities.Service;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AddService", value = "/AddService")
public class AddService extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServiceService serviceService;

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

        String type = (String) request.getSession().getAttribute("type");
        double bp1 = (double)request.getSession().getAttribute("bp1");
        double bp2 =(double) request.getSession().getAttribute("bp2");
        double bp3 =(double) request.getSession().getAttribute("bp3");
        int gig_incl =(int) request.getSession().getAttribute("gig_incl");
        int min_incl =(int) request.getSession().getAttribute("min_incl");
        int sms_incl =(int) request.getSession().getAttribute("sms_incl");
        int gig_extra =(int) request.getSession().getAttribute("gig_extra");
        int min_extra =(int) request.getSession().getAttribute("min_extra");
        int sms_extra =(int) request.getSession().getAttribute("sms_extra");


        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://34.65.160.235:3306/telco_db:3306");
            PreparedStatement statement = con.prepareStatement("INSERT INTO Service VALUES (?, ?, ?, ?,?, ?, ?, ?,?, ?, ?)");
            statement.setString(2, type);
            statement.setDouble(3, bp1);
            statement.setDouble(4, bp2);
            statement.setDouble(5, bp3);
            statement.setInt(6, gig_incl);
            statement.setInt(7, min_incl);
            statement.setInt(8, sms_incl);
            statement.setInt(9, gig_extra);
            statement.setInt(10, min_extra);
            statement.setInt(11, sms_extra);

            int i = statement.executeUpdate();
            if(i > 0) {
                //New Optional Service Successfully Added
            }
            //
        } catch (EJBException | SQLException e) {

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);


    }
}
