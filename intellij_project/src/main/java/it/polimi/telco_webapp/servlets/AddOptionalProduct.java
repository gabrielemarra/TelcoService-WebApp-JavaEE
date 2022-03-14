package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.exceptions.InternalDBErrorException;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.User;
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
import java.util.List;

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
        String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
        String priceStr = StringEscapeUtils.escapeJava(request.getParameter("price"));

        OptionalProduct newOptionalProduct = new OptionalProduct();
        newOptionalProduct.setName(name);
        newOptionalProduct.setPrice(new BigDecimal(priceStr));
        newOptionalProduct.setQuantitySold(0);
        newOptionalProduct.setOrders(null);

        try {

        } catch (EJBException e) {

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);


    }
}
