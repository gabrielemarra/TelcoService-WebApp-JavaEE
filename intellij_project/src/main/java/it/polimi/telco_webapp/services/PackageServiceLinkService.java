package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.PackageServiceLink;
import it.polimi.telco_webapp.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless(name = "PackageServiceLinkService")

public class PackageServiceLinkService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public PackageServiceLinkService() {}


    public List<PackageServiceLink> insertNewPackageAndServiceLinks(ServicePackage servicePackage, HashMap<Service, Integer> servicesAndQuantities) {
        List<PackageServiceLink> links = new ArrayList<>();
        for(Service service: servicesAndQuantities.keySet()) {
            PackageServiceLink packageAndServiceLink = new PackageServiceLink();

            packageAndServiceLink.setServicePackage(servicePackage);
            packageAndServiceLink.setService(service);
            packageAndServiceLink.setQuantity(servicesAndQuantities.get(service));
            // TODO: question: would it be safer to persist in another for-loop? to ensure that ALL the
            //  links could be created correctly first and THEN persist the links to the DB?
            em.persist(packageAndServiceLink);
            links.add(packageAndServiceLink);
        }
        return links;

    }

    public List<Service> getServices(int servicePackageId) {
        // do a query?
        List<Service> dummy = new ArrayList<>();
        return dummy;
    }

    public Integer getQuantityByObject(ServicePackage servicePackage, Service service) {
        // do a query
        return 33;
    }

    public Integer getQuantityById(int servicePackageId, int serviceId) {
        // do a query
        return 34;
    }



}


