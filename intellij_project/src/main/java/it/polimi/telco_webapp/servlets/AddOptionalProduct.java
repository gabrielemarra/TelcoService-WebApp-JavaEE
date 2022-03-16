package it.polimi.telco_webapp.servlets;

import com.google.gson.JsonObject;

import it.polimi.telco_webapp.services.OptionalProductService;
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
import java.sql.*;


@WebServlet(name = "AddOptionalProduct", value = "/AddOptionalProduct")
public class AddOptionalProduct extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/OptionalProductService")
    private OptionalProductService optionalProductService;

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
        /* QUESTION: difference between passing info via getParameter() versus getting from items put in sessionStorage() */
        String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
        String priceStr = StringEscapeUtils.escapeJava(request.getParameter("price"));
/** We do not need an instantiation of an OptionalProduct object to add to the datebase...
 * we can use use the user entries directly to populate the columns in the database
        OptionalProduct newOptionalProduct = new OptionalProduct();
        newOptionalProduct.setName(name);
        newOptionalProduct.setPrice(new BigDecimal(priceStr));
        newOptionalProduct.setQuantitySold(0);
        newOptionalProduct.setOrders(new ArrayList<Order>());
        newOptionalProduct.setServicePackages(new ArrayList<ServicePackage>());
 */

        try {
            /* TODO: use the employee's credentials here when establishing the connection */
            Connection con = DriverManager.getConnection("jdbc:mysql://34.65.160.235:3306/telco_db", "root", "db2project2021");
            PreparedStatement statement = con.prepareStatement("INSERT INTO optional_product VALUES (opt_id IS NULL, ?, ?, 0)");
            /** Columns of the OptionalProduct table
             *  Column 1: ID
             *  Column 2: Name
             *  Column 3: Price
             *  Column 4: Quantity Sold
             */
            /**
             *   statement.setString(1, newOptionalProduct.getName());
             *   statement.setBigDecimal(2, newOptionalProduct.getPrice());
             *
             */

            statement.setString(1, name);
            statement.setBigDecimal(2, new BigDecimal(priceStr));

            int i = statement.executeUpdate();
            if(i > 0) {
                //New Optional Service Successfully Added
            }
        } catch (EJBException | SQLException e) {
            sendError(request, response, "InternalDBErrorException", e.getCause().getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);


    }
}
