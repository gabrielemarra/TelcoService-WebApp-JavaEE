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

    // should we do this with the ServicePackageID instead of being given the service package object?
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
        List<Option> options =  em.createNamedQuery("PackageOptionLink.getOptionsLinkedWithPackage", Option.class).setParameter(1, servicePackageId).getResultList();
        return options;
    }

    public Integer getQuantity(ServicePackage servicePackage, Option option) {
        Integer quantity = em.createNamedQuery("PackageOptionLink.getQuantity", Integer.class).setParameter(1, servicePackage).setParameter(2, option).getSingleResult();
        return quantity;
    }

    public Integer getQuantity2(int servicePackageId, int optionId) {
        Integer quantity = em.createNamedQuery("PackageOptionLink.getQuantity", Integer.class).setParameter(1, servicePackageId).setParameter(2, optionId).getSingleResult();
        return quantity;
    }



}


