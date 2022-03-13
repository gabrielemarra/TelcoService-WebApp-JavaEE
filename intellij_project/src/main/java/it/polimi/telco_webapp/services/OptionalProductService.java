package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.util.List;

@Stateless(name = "OrderService")
public class OptionalProductService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OptionalProductService() {
    }

    public OptionalProduct getOptionalProduct(String optionalProductID){
        List<OptionalProduct> products = em.createNamedQuery("OptionalProduct.getOptionalProduct", OptionalProduct.class).setParameter(1, optionalProductID).getResultList();
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Invalid optionalProductID");
        }
        else if(products.size()==1) {
            return products.get(0);
        }
        else {
            throw new IllegalArgumentException("Internal database error: too many result for a single ID");
        }
    }
}
