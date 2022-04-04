package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.views.ServicePackageView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;


import java.util.List;

@Stateless(name = "ServicePackageViewService")
public class ServicePackageViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public ServicePackageViewService() {}

    public List<ServicePackageView> getAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<ServicePackageView> cq = cb.createQuery(ServicePackageView.class);
        Root<ServicePackageView> rootEntry = cq.from(ServicePackageView.class);
        CriteriaQuery<ServicePackageView> all = cq.select(rootEntry);
        TypedQuery<ServicePackageView> allQuery = em.createQuery(all);
        return allQuery.getResultList();

    }

}





