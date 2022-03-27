package it.polimi.telco_webapp.services;

import java.util.HashMap;
import java.util.List;

import it.polimi.telco_webapp.auxiliary.exceptions.NoServicePackageFound;
import it.polimi.telco_webapp.entities.Option;
import it.polimi.telco_webapp.entities.PackageServiceLink;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;

@Stateless(name = "ServicePackageService")

public class ServicePackageService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public ServicePackageService() {}

    /**
     * Insert new service package into database.
     *
     * //@param id:       Service id for the service package
     * @param name:     Name of the service package
     * @param validity: Validity period for the service package.
     * //@param options:  The optional products associated with the service packaage.
     * @return The service packaged added to the database.
     * @throws PersistenceException
     * @throws IllegalArgumentException
     */
    public ServicePackage insertNewServicePackage(String name, int validity, List<Option> options) throws PersistenceException, IllegalArgumentException {
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setName(name);
        servicePackage.setValidityPeriod(validity);
        servicePackage.setOptions(options);

        em.persist(servicePackage);
        return servicePackage;
    }

    /**
     * Gets a service package from the database by the service package id.
     *
     * @param package_id: The package id of the service package
     * @return The service package retreived from the database.
     */
    public ServicePackage getServicePackage(int package_id) throws InvalidParameterException {
        ServicePackage servicePackage = em.find(ServicePackage.class, package_id);
        if (servicePackage == null) {
            throw new InvalidParameterException("Invalid Service Package ID");
        }
        return servicePackage;
    }

    public List<ServicePackage> getAllServicePackages() {
        List<ServicePackage> packages = em.createNamedQuery("ServicePackage.getAllServicePackages", ServicePackage.class).getResultList();
        if (packages == null || packages.isEmpty()) {
            throw new EntityNotFoundException("No service packages retrieved");
        }
        return packages;
    }

    /**
     * Sets the validity period for an existing service package.
     *
     * @param package_id:     The service package id of an existing service package in the database.
     * @param validityPeriod: The new validity period to set.
     * @throws IllegalArgumentException: Thrown when the validity period provided is invalid or when the packageid
     *                                   provided is invalid.
     */
    public void setValidityPeriod(int package_id, int validityPeriod) throws IllegalArgumentException {
        /* Check that the validity period provided is an acceptable one.*/
        if (validityPeriod > 3 || validityPeriod < 1) {
            throw new InvalidParameterException("Invalid validity period provided. Must be  12, 24, or 36");
        }
        ServicePackage servicePackage = getServicePackage(package_id);
        if(servicePackage == null) {
            throw new EntityNotFoundException("Service package not found with ID: " + package_id);
        } else {
            servicePackage.setValidityPeriod(validityPeriod);
            em.persist(servicePackage);
        }
    }

    /**
     * Only AFTER a new service package is created (ie, after the new package ID is generated) can we then define the
     * services linked to the new service package. This method takes in the service-package links, obtains the service
     * ID from any one of the links, and then associates the links to the specified package ID.
     * @param servicesLinkedToPackage
     */
    public void addServices(List <PackageServiceLink> servicesLinkedToPackage) {
        ServicePackage servicePackage = servicesLinkedToPackage.get(0).getServicePackage();
        servicePackage.setServicesLinkedToPackage(servicesLinkedToPackage);

    }

}
