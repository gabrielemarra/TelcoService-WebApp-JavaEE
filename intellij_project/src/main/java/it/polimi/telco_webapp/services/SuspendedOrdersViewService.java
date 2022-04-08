package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.SuspendedOrdersView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless(name = "SuspendedOrdersViewService")
public class SuspendedOrdersViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public SuspendedOrdersViewService() {}

    public List<SuspendedOrdersView> getAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<SuspendedOrdersView> cq = cb.createQuery(SuspendedOrdersView.class);
        Root<SuspendedOrdersView> rootEntry = cq.from(SuspendedOrdersView.class);
        CriteriaQuery<SuspendedOrdersView> all = cq.select(rootEntry);
        TypedQuery<SuspendedOrdersView> allQuery = em.createQuery(all);
        return allQuery.getResultList();

    }

}





