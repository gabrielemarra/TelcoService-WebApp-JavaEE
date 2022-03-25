package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Option;
import it.polimi.telco_webapp.entities.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "OptionService")

public class OptionService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OptionService() {
    }

    public Option getOption(int optionId){
        List<Option> options = em.createNamedQuery("Option.getOption", Option.class).setParameter(1, optionId).getResultList();
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("Invalid option ID");
        }
        else if(options.size()==1) {
            return options.get(0);
        }
        else {
            throw new IllegalArgumentException("Internal database error: too many results for a single ID");
        }
    }

    public Option insertNewOption(String name, BigDecimal price) {
        Option prod = new Option();
        prod.setQuantitySold(0);
        prod.setName(name);
        prod.setPrice(price);
        prod.setOrders(new ArrayList<Order>());
        em.persist(prod);
        return prod;
    }

    public List<Option> getAllOptions() {
        List<Option> options = em.createNamedQuery("Option.getAllAvailableOptions", Option.class).getResultList();
        return options;
    }
}
