package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.telco_webapp.auxiliary.exceptions.NoServicePackageFound;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.OptionsAvailable;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.ServicePackage;
import it.polimi.telco_webapp.services.OptionalProductService;
import it.polimi.telco_webapp.services.OptionsAvailableService;
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
    @EJB(name = "it.polimi.db2.entities.services/OptionalProductService")
    private OptionalProductService optionalProductService;
    @EJB(name = "it.polimi.db2.entities.services/OptionsAvailableService")
    private OptionsAvailableService optionsAvailableService;



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



        List<Service> servicesList = new ArrayList<Service>();
        for(int i = 0; i < serviceIds.length; i++) {
            Service temp = serviceService.getService(Integer.parseInt(serviceIds[i]));
            servicesList.add(temp);
        }

        List<OptionalProduct> optionsList = new ArrayList<OptionalProduct>();
        HashMap<OptionalProduct, Integer> optionalProductQuantities = new HashMap<>();
        for(int i = 0; i < optionIds.length; i++) {
            //optionsList.add(optionalProductService.getOptionalProduct(Integer.parseInt(optionIds[i])));
            optionalProductQuantities.put(optionalProductService.getOptionalProduct(Integer.parseInt(optionIds[i])), Integer.parseInt(optionQuantities[i]));
        }

        try{
            // Create the new Service Package
            ServicePackage servicePackage = servicePackageService.insertServicePackage(name, period);
            // Create the list that defines the quantities per optional product
            List<OptionsAvailable> optionsAvailableForPackage = optionsAvailableService.addServicePackageWithOptions(servicePackage, optionalProductQuantities);
            // LINK the list of -pkgID-optionID-quantity- associations with the newly created service package
            servicePackage.setOptionalProductsAvailable(optionsAvailableForPackage);


            //public List<OptionsAvailable> addServicePackageWithOptions(ServicePackage servicePackage, HashMap<OptionalProduct, Integer> optionsAndQuantities) {



                // use the packageID to set the pkg-availableservices and pkg-availableoptoions associations
        } catch (EJBException e) {
            sendError(request, response, "InternalDBErrorException", e.getCause().getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);


    }
}
