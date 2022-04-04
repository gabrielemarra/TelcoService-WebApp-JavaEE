package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.views.InsolventUsersView;
import it.polimi.telco_webapp.views.SuspendedOrdersView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless(name = "InsolventUsersViewService")
public class InsolventUsersViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public InsolventUsersViewService() {}

    public List<InsolventUsersView> getAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<InsolventUsersView> cq = cb.createQuery(InsolventUsersView.class);
        Root<InsolventUsersView> rootEntry = cq.from(InsolventUsersView.class);
        CriteriaQuery<InsolventUsersView> all = cq.select(rootEntry);
        TypedQuery<InsolventUsersView> allQuery = em.createQuery(all);
        return allQuery.getResultList();

    }

}





