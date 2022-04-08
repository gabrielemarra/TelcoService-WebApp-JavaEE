package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.auxiliary.exceptions.UserNotFoundException;
import it.polimi.telco_webapp.entities.BestSellerView;
import it.polimi.telco_webapp.entities.Employee;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;


@Stateless(name = "BestSellerViewService")
public class BestSellerViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public BestSellerViewService() {}

    public BestSellerView getBestSeller() {
        List<BestSellerView> bestSeller = em.createNamedQuery("BestSeller.getBestSeller", BestSellerView.class).getResultList();
        if (bestSeller == null | bestSeller.isEmpty()) {
            throw new UserNotFoundException("InvalidID", "internal database error");
        }
        return bestSeller.get(0);
    }

}





