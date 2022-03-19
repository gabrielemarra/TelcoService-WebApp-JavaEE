package it.polimi.telco_webapp.services;

import java.util.List;

import it.polimi.telco_webapp.auxiliary.exceptions.NoServicePackageFound;
import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import javax.swing.text.html.Option;
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
     * @param id:       Service id for the service package
     * @param name:     Name of the service package
     * @param validity: Validity period for the service package.
     * @param options:  The optional products associated with the service packaage.
     * @return The service packaged added to the database.
     * @throws PersistenceException
     * @throws IllegalArgumentException
     */
    public ServicePackage insertServicePackage(int id, String name, int validity, List<OptionalProduct> options) throws PersistenceException, IllegalArgumentException {
        ServicePackage bundle = new ServicePackage();

        bundle.setId(id);
        bundle.setName(name);
        bundle.setValidityPeriod(validity);
        bundle.setOptionalServices(options);

        em.persist(bundle);
        return bundle;
    }

    /**
     * Gets a service package from the database by the service package id.
     *
     * @param package_id: The package id of the service package
     * @return The service package retreived from the database.
     */
    public ServicePackage getServicePackage(int package_id) throws InvalidParameterException {
        List<ServicePackage> bundles = em.createNamedQuery("ServicePackage.getOneServicePackage", ServicePackage.class).setParameter(1, package_id).getResultList();
        if (bundles == null || bundles.isEmpty()) {
            throw new InvalidParameterException("Invalid Service Package ID");
        } else if (bundles.size() == 1) {
            return bundles.get(0);
        } else {
            throw new InvalidParameterException("DB Error");
        }
    }

    /**
     * Get all the optional products available for the service package associated with the provided package id.
     *
     * @param package_id: Service package id of the service package.
     * @return List of OptionalProduct associated with the service package.
     */
    public List<OptionalProduct> getOptionalProductsAvailable(int package_id) throws InvalidParameterException {
        List<ServicePackage> packages = em.createNamedQuery("ServicePackage.getOneServicePackage", ServicePackage.class).setParameter(1, package_id).getResultList();
        if (packages == null || packages.isEmpty()) {
            throw new InvalidParameterException("Invalid Service Package ID");
        }
        
        ServicePackage pack = packages.get(0);
        return pack.getOptionalProducts();
        // cleaner: return packages.get(0).getOptionalProducts();

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
