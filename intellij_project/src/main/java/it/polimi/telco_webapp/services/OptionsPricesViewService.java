package it.polimi.telco_webapp.services;


import it.polimi.telco_webapp.entities.OptionsPricesView;
import it.polimi.telco_webapp.entities.PackagePricesView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless(name = "OptionsPricesView")
public class OptionsPricesViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public OptionsPricesViewService() {}

    public float getOptionsCost(int order_id) {
        float price = em.find(OptionsPricesView.class, order_id).getSumOfOptionsPurchased();
        // check... if order not found it is because this order did not order any optional products...
        // TODO: check if order ordered optional products. If not, it is OK, it should not break...

        return price;

    }

}





