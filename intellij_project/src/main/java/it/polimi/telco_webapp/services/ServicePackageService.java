package it.polimi.telco_webapp.services;

import java.util.List;

import it.polimi.telco_webapp.entities.OptionalProduct;
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
     * @param id: Service id for the service package
     * @param name: Name of the service package
     * @param validity: Validity period for the service package.
     * @param options: The optional products associated with the service packaage.
     * @return The service packaged added to the database.
     * @throws PersistenceException
     * @throws IllegalArgumentException
     */
    public ServicePackage insertServicePackage(int id, String name, int validity, List<OptionalProduct> options) throws PersistenceException, IllegalArgumentException{
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
     * @param package_id: The package id of the service package
     * @return The service package retreived from the database.
     */
    public ServicePackage getServicePackage(int package_id){
        List<ServicePackage> bundles = em.createNamedQuery("ServicePackage.getServicePackages", ServicePackage.class).setParameter(1, package_id).getResultList();
        if (bundles == null || bundles.isEmpty()) {
            throw new InvalidParameterException("Invalid Service Package ID");
        }
        else if(bundles.size()==1) {
            return bundles.get(0);
        }
        else {
            throw new InvalidParameterException("DB Error");
        }
    }

    /**
     * Get all the optional products available for the service package associated with the provided package id.
     * @param package_id: Service package id of the service package.
     * @return List of OptionalProduct associated with the service package.
     */
    public List<OptionalProduct> getOptionalProductsAvailable(int package_id) {
        List<OptionalProduct> options = em.createNamedQuery("OptionalProduct.getOptionalProductsAvailable", OptionalProduct.class).setParameter(1, package_id).getResultList();
        if (options == null || options.isEmpty()) {
            throw new InvalidParameterException("Invalid Service Package ID");
        }
        return options;
    }
}
