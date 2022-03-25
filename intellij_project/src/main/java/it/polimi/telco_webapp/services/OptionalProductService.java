package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Order;
import it.polimi.telco_webapp.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "OptionalProductService")

public class OptionalProductService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OptionalProductService() {
    }

    public OptionalProduct getOptionalProduct(int optionalProductID){
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

    public OptionalProduct addOptionalProduct(String name, BigDecimal price) {
        OptionalProduct prod = new OptionalProduct();
        //prod.setServicePackages(new ArrayList<ServicePackage>());
        prod.setQuantitySold(0);
        prod.setName(name);
        prod.setPrice(price);
        prod.setOrders(new ArrayList<Order>());
        em.persist(prod);
        return prod;
    }

    public List<OptionalProduct> getAllOptionalProduct() {
        List<OptionalProduct> options = em.createNamedQuery("OptionalProduct.getAllAvailableOptionalProduct", OptionalProduct.class).getResultList();
        return options;
    }
}
