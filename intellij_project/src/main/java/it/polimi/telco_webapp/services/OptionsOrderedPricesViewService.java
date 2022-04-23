package it.polimi.telco_webapp.services;


import it.polimi.telco_webapp.entities.OptionsOrderedPricesView;
import it.polimi.telco_webapp.entities.PackagePricesView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless(name = "OptionsOrderedPricesView")
public class OptionsOrderedPricesViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public OptionsOrderedPricesViewService() {}

    public float getOptionsPrice(int order_id) {
        OptionsOrderedPricesView optionsPrices = em.find(OptionsOrderedPricesView.class, order_id);
        float price = optionsPrices.getSumOfSales();
        return price;
    }
}





