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

    public ServicePackage insertServicePackage(int id, String name, int validity, List<OptionalProduct> options) throws PersistenceException, IllegalArgumentException{
        ServicePackage bundle = new ServicePackage();

        bundle.setId(id);
        bundle.setName(name);
        bundle.setValidityPeriod(validity);
        bundle.setOptionalServices(options);

        em.persist(bundle);
        return bundle;
    }
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

    public List<OptionalProduct> getOptionalProductsAvailable(int package_id) {
        List<OptionalProduct> options = em.createNamedQuery("OptionalProduct.getOptionalProductsAvailable", OptionalProduct.class).setParameter(1, package_id).getResultList();

        if (options == null || options.isEmpty()) {
            throw new InvalidParameterException("Invalid Service Package ID");
        }

        return options;
    }




}
