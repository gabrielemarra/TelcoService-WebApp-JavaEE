package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.OptionalProduct;
import it.polimi.telco_webapp.entities.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "OptionalProductService")

public class OptionalProductService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OptionalProductService() {}

    public OptionalProduct insertNewOption(String name, BigDecimal price) {
        OptionalProduct newOption = new OptionalProduct();
        newOption.setName(name);
        newOption.setPrice(price);
        newOption.setQuantitySold(0);
        newOption.setOrders(new ArrayList<Order>());
        em.persist(newOption);
        return newOption;
    }

    public OptionalProduct getOption(int optionId){
        OptionalProduct option = em.find(OptionalProduct.class, optionId);
        if (option == null) {
            throw new EntityNotFoundException("Cannot find option with ID: " + optionId);
        }
        return option;
    }

    public List<OptionalProduct> getAllOptions() {
        List<OptionalProduct> options = em.createNamedQuery("OptionalProduct.getAllAvailableOptions", OptionalProduct.class).getResultList();
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("No options found.");
        } else {
            return options;
        }
    }
}
