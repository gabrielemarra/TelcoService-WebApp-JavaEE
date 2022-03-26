package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Option;
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

    public Integer getQuantity(ServicePackage servicePackage, Service service) {
        Integer quantity = em.createNamedQuery("PackageServiceLink.getQuantity", Integer.class).setParameter(1, servicePackage).setParameter(2, service).getSingleResult();
        return quantity;
    }

    public Integer getQuantity2(int servicePackageId, int serviceId) {
        Integer quantity = em.createNamedQuery("PackageServiceLink.getQuantity", Integer.class).setParameter(1, servicePackageId).setParameter(2, serviceId).getSingleResult();
        return quantity;
    }



}


