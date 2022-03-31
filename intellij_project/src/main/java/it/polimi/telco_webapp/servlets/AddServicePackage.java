package it.polimi.telco_webapp.servlets;

import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.*;
import it.polimi.telco_webapp.services.OptionalProductService;

import it.polimi.telco_webapp.services.ServiceService;
import it.polimi.telco_webapp.services.ServicePackageService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "AddServicePackage", value = "/AddServicePackage")
public class AddServicePackage extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ServicePackageService")
    private ServicePackageService servicePackageService;
    @EJB(name = "it.polimi.db2.entities.services/ServiceService")
    private ServiceService serviceService;
    @EJB(name = "it.polimi.db2.entities.services/OptionService")
    private OptionalProductService optionService;


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
        Integer period = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("period")));

        String serviceIds[] = request.getParameterValues("serviceIds[]");
        String optionIds[] = request.getParameterValues("optionIds[]");

        try{

        List<Service> services = new ArrayList<>();
        for(int i = 0; i < serviceIds.length; i++) {
            services.add(serviceService.getService(Integer.parseInt(serviceIds[i])));
        }

        List<OptionalProduct> options = new ArrayList<>();
        for(int i = 0; i < optionIds.length; i++) {
            options.add(optionService.getOption(Integer.parseInt(optionIds[i])));
        }

            ServicePackage servicePackage = servicePackageService.insertNewServicePackage(name, period, options, services);
        } catch (EJBException e) {
            sendError(request, response, "InternalDBErrorException", e.getCause().getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);


    }
}
