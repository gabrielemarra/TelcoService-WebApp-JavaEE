package it.polimi.telco_webapp.servlets;

import com.google.gson.JsonObject;
import it.polimi.telco_webapp.entities.*;
import it.polimi.telco_webapp.services.OptionService;
import it.polimi.telco_webapp.services.PackageOptionLinkService;
import it.polimi.telco_webapp.services.PackageServiceLinkService;

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
    private OptionService optionService;
    @EJB(name = "it.polimi.db2.entities.services/PackageOptionLinkService")
    private PackageOptionLinkService packageAndOptionLinkService;
    @EJB(name = "it.polimi.db2.entities.services/PackageServiceLinkService")
    private PackageServiceLinkService packageAndServiceLinkService;



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
        String serviceQuantities[] = request.getParameterValues("serviceQuantities[]");

        String optionIds[] = request.getParameterValues("optionIds[]");
        String optionQuantities[] = request.getParameterValues("optionQuantities[]");

        HashMap<Service, Integer> servicesAndQuantities = new HashMap<>();
        for(int i = 0; i < serviceIds.length; i++) {
            servicesAndQuantities.put(serviceService.getService(Integer.parseInt(serviceIds[i])), Integer.parseInt(serviceQuantities[i]));
        }

        HashMap<Option, Integer> optionsAndQuantities = new HashMap<>();
        for(int i = 0; i < optionIds.length; i++) {
            optionsAndQuantities.put(optionService.getOption(Integer.parseInt(optionIds[i])), Integer.parseInt(optionQuantities[i]));
        }

        try{
            // Create the new Service Package
            ServicePackage servicePackage = servicePackageService.insertNewServicePackage(name, period);
            // Create the lists that defines the quantities per optional product/ per service
            List<PackageOptionLink> optionsLinkedToPackage = packageAndOptionLinkService.insertNewPackageAndOptionLinks(servicePackage, optionsAndQuantities);
            List<PackageServiceLink> servicesLinkedToPackage = packageAndServiceLinkService.insertNewPackageAndServiceLinks(servicePackage, servicesAndQuantities);
            // LINK the lists of -pkgID-[optionID/serviceID]-quantity- associations with the newly created service package
            servicePackageService.addOptions(optionsLinkedToPackage);
            servicePackageService.addServices(servicesLinkedToPackage);

        } catch (EJBException e) {
            sendError(request, response, "InternalDBErrorException", e.getCause().getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);


    }
}
