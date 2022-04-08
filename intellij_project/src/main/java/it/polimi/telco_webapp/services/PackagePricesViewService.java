package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.AuditView;
import it.polimi.telco_webapp.entities.SuspendedOrdersView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless(name = "AuditViewService")
public class AuditViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public AuditViewService() {}

    public List<AuditView> getAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<AuditView> cq = cb.createQuery(AuditView.class);
        Root<AuditView> rootEntry = cq.from(AuditView.class);
        CriteriaQuery<AuditView> all = cq.select(rootEntry);
        TypedQuery<AuditView> allQuery = em.createQuery(all);
        return allQuery.getResultList();

    }

}





