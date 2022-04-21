package it.polimi.telco_webapp.services;


import it.polimi.telco_webapp.entities.OrdersAndCostsView;
import it.polimi.telco_webapp.entities.PackagePricesView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless(name = "OrdersAndCostsView")
public class OrdersAndCostsViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public OrdersAndCostsViewService() {}

    public float getBasePrice(int order_id) {
        OrdersAndCostsView order = em.find(OrdersAndCostsView.class, order_id);
        return order.getBaseCost();
    }

    public float getOptionsCost(int order_id) {
        OrdersAndCostsView order = em.find(OrdersAndCostsView.class, order_id);
        return order.getOptionsCost();
    }

    public float getTotalCost(int order_id) {
        OrdersAndCostsView order = em.find(OrdersAndCostsView.class, order_id);
        return order.getTotalCost();
    }

}





