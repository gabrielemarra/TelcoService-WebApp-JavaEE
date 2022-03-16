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
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AddService", value = "/AddService")
public class AddService extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ServiceService")
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

        String type = StringEscapeUtils.escapeJava(request.getParameter("planType"));
        // Doubles
        Double bp1 = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("bp1")));
        Double bp2 = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("bp2")));
        Double bp3 = Double.parseDouble(StringEscapeUtils.escapeJava(request.getParameter("bp3")));
        // Integers
        Integer gigIncl = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("gigIncl")));
        Integer minIncl = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("minIncl")));
        Integer smsIncl = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("smsIncl")));
        // TODO: These should be changed to doubles. Must change the corresponding columns in the database to type double before we change the type HERE
        Integer gigExtra = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("gigExtra")));
        Integer minExtra = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("minExtra")));
        Integer smsExtra = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("smsExtra")));

        try{
            /* TODO: use the employee's credentials here when establishing the connection */
            Connection con = DriverManager.getConnection("jdbc:mysql://34.65.160.235:3306/telco_db", "root", "db2project2021");
            //PreparedStatement statement = con.prepareStatement("INSERT INTO service VALUES (service_id IS NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement statement = con.prepareStatement("INSERT INTO service VALUES (service_id IS NULL, 'Fixed_Internet', 11, 2.3, 6.7, 22, 33, 44, 55, 66, 77)");
            /**
             *
            statement.setDouble(2, bp1);
            statement.setDouble(3, bp2);
            statement.setDouble(4, bp3);
            statement.setInt(5, gigIncl);
            statement.setInt(6, minIncl);
            statement.setInt(7, smsIncl);
            statement.setInt(8, gigExtra);
            statement.setInt(9, minExtra);
            statement.setInt(10, smsExtra);
            statement.setString(1, type);
             */

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
