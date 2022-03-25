package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.auxiliary.exceptions.NoServicePackageFound;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.OptionsAvailable;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless(name = "OptionsAvailableService")

public class OptionsAvailableService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OptionsAvailableService() {}


    public List<OptionsAvailable> addServicePackageWithOptions(ServicePackage servicePackage, HashMap<OptionalProduct, Integer> optionsAndQuantities) {
        List<OptionsAvailable> cluster = new ArrayList<>();
        for(OptionalProduct option: optionsAndQuantities.keySet()) {
            OptionsAvailable optionsAvailable = new OptionsAvailable();

            optionsAvailable.setServicePackage(servicePackage);
            optionsAvailable.setOptionalProduct(option);
            optionsAvailable.setQuantity(optionsAndQuantities.get(option));
            em.persist(optionsAvailable);
            cluster.add(optionsAvailable);
        }
        return cluster;

    }

    public List<OptionalProduct> getOptionalProductsAvailable(ServicePackage servicePackage) {
        // do a query?
        List<OptionalProduct> dummy = new ArrayList<>();
        return dummy;
    }

    public Integer getQuantity(ServicePackage servicePackage, OptionalProduct option) {
        // do a query
        return 33;
    }



}


