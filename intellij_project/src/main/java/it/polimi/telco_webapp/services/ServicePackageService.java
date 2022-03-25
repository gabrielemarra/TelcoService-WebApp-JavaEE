package it.polimi.telco_webapp.services;

import java.util.List;

import it.polimi.telco_webapp.auxiliary.exceptions.NoServicePackageFound;
import it.polimi.telco_webapp.entities.PackageOptionLink;
import it.polimi.telco_webapp.entities.PackageServiceLink;
import it.polimi.telco_webapp.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;

@Stateless(name = "ServicePackageService")

public class ServicePackageService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public ServicePackageService() {
    }

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
    public ServicePackage insertNewServicePackage(String name, int validity) throws PersistenceException, IllegalArgumentException {
        ServicePackage servicePackage = new ServicePackage();

        servicePackage.setName(name);
        servicePackage.setValidityPeriod(validity);

        em.persist(servicePackage);
        return servicePackage;
    }

    public void addOptions(List<PackageOptionLink> optionsLinkedToPackage) {
        //TODO: maybe do a check here to ensure that all the OptionsAvailable-s in the list refer to the same service package
        ServicePackage servicePackage = optionsLinkedToPackage.get(0).getServicePackage();
        servicePackage.setOptionsLinkedToPackage(optionsLinkedToPackage);
    }

    public void addServices(List <PackageServiceLink> servicesLinkedToPackage) {
        ServicePackage servicePackage = servicesLinkedToPackage.get(0).getServicePackage();
        servicePackage.setServicesLinkedToPackage(servicesLinkedToPackage);
    }

    /**
     * Gets a service package from the database by the service package id.
     *
     * @param package_id: The package id of the service package
     * @return The service package retreived from the database.
     */
    public ServicePackage getServicePackage(int package_id) throws InvalidParameterException {
        List<ServicePackage> servicePackages = em.createNamedQuery("ServicePackage.getOneServicePackage", ServicePackage.class).setParameter(1, package_id).getResultList();
        if (servicePackages == null || servicePackages.isEmpty()) {
            throw new InvalidParameterException("Invalid Service Package ID");
        } else if (servicePackages.size() == 1) {
            return servicePackages.get(0);
        } else {
            throw new InvalidParameterException("DB Error");
        }
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
        if (validityPeriod >3 || validityPeriod < 1) {
            throw new InvalidParameterException("Invalid validity period provided. Must be  12, 24, or 36");
        }
        List<ServicePackage> packages = em.createNamedQuery("ServicePackage.getOneServicePackage", ServicePackage.class).setParameter(1, package_id).getResultList();
        /* Check that the query returns at least one service package from the package id provided. */
        if (packages == null || packages.isEmpty()) {
            throw new InvalidParameterException("Invalid service package ID.");
        }
        /* Check that the query returns no more than one service package from the package id provided. */
        if (packages.size() > 1) {
            throw new InvalidParameterException("DB returned more than one service package from the provided package ID.");
        }
        ServicePackage servicePackage = packages.get(0);
        servicePackage.setValidityPeriod(validityPeriod);
        em.persist(servicePackage);
    }

    public List<ServicePackage> getAllServicePackages() {
        List<ServicePackage> packages = em.createNamedQuery("ServicePackage.getAllServicePackages", ServicePackage.class).getResultList();
        if (packages == null || packages.isEmpty()) {
            throw new NoServicePackageFound("No service package retrieved");
        }
        return packages;
    }
}
