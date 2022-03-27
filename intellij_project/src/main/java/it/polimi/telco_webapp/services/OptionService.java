package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Option;
import it.polimi.telco_webapp.entities.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "OptionService")

public class OptionService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OptionService() {}

    public Option insertNewOption(String name, BigDecimal price) {
        Option newOption = new Option();
        newOption.setName(name);
        newOption.setPrice(price);
        newOption.setQuantitySold(0);
        newOption.setOrders(new ArrayList<Order>());
        em.persist(newOption);
        return newOption;
    }

    public Option getOption(int optionId){
        Option option = em.find(Option.class, optionId);
        if (option == null) {
            throw new EntityNotFoundException("Cannot find option with ID: " + optionId);
        }
        return option;
    }

    public List<Option> getAllOptions() {
        List<Option> options = em.createNamedQuery("Option.getAllAvailableOptions", Option.class).getResultList();
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("No options found.");
        } else {
            return options;
        }
    }
}
