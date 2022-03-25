package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Option;
import it.polimi.telco_webapp.entities.PackageOptionLink;
import it.polimi.telco_webapp.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless(name = "PackageOptionLinkService")

public class PackageOptionLinkService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public PackageOptionLinkService() {}

    // shouldwe do this with the servicepackageID instead of being given the service package object?
    public List<PackageOptionLink> insertNewPackageAndOptionLinks(ServicePackage servicePackage, HashMap<Option, Integer> optionsAndQuantities) {
        List<PackageOptionLink> links = new ArrayList<>();
        for(Option option: optionsAndQuantities.keySet()) {
            PackageOptionLink packageAndOptionLink = new PackageOptionLink();

            packageAndOptionLink.setServicePackage(servicePackage);
            packageAndOptionLink.setOption(option);
            packageAndOptionLink.setQuantity(optionsAndQuantities.get(option));
            // TODO: question: would it be safer to persist in another for-loop? to ensure that ALL the
            //  links could be created correctly first and THEN persist the links to the DB?
            em.persist(packageAndOptionLink);
            links.add(packageAndOptionLink);
        }
        return links;

    }

    public List<Option> getOptions(int servicePackageId) {
        // do a query?
        List<Option> dummy = new ArrayList<>();
        return dummy;
    }

    public Integer getQuantity(ServicePackage servicePackage, Option option) {
        // do a query
        return 33;
    }



}


