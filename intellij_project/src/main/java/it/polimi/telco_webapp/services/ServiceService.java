package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.entities.ServiceType;
import it.polimi.telco_webapp.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;

@Stateless(name = "ServiceService")

public class ServiceService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public ServiceService() {
    }

    public Service insertNewService(int service_id, ServiceType type, BigDecimal[] basePrices, int[] planParams) throws PersistenceException, IllegalArgumentException{
        if(basePrices.length != 3) {
            throw new IllegalArgumentException("Correctly specify the base places for the plan.");
        }

        Service service;
        switch(type) {

            case Fixed_Phone:
                service = new Service();
                break;
            case Mobile_Internet: case Fixed_Internet:
                if (planParams.length != 2) {
                    throw new IllegalArgumentException("Correctly specify the parameters for the internet plan.");
                }
                int gig_incl = planParams[0];
                int gig_extra = planParams[1];
                service = insertInternet(gig_incl, gig_extra);
                break;
            case Mobile_Phone:
                if (planParams.length != 4) {
                    throw new IllegalArgumentException("Correctly specify the parameters for the mobile phone plan.");
                }
                int sms_incl = planParams[0];
                int sms_extra = planParams[1];
                int min_incl = planParams[2];
                int min_extra = planParams[3];
                service = insertMobilePhone(sms_incl, sms_extra, min_incl, min_extra);

                break;

            default:
                throw new IllegalArgumentException("Invalid plan type.");
        }

        service.setType(type);
        service.setBasePrice1(basePrices[0]);
        service.setBasePrice2(basePrices[1]);
        service.setBasePrice3(basePrices[2]);
        service.setId(service_id);
        em.persist(service);

        return service;
    }
    private Service insertInternet(int gig_incl, int gig_extra) {
        Service service = new Service();
        service.setGigIncluded(gig_incl);
        service.setGigExtra(gig_extra);

        return service;
    }

    private Service insertMobilePhone(int sms_incl, int sms_extra, int min_incl, int min_extra) {
        Service service = new Service();
        service.setSmsIncluded(sms_incl);
        service.setSmsExtra(sms_extra);
        service.setMinIncluded(min_incl);
        service.setMinExtra(min_extra);
        return service;
    }

    public Service getService(int service_id) {
        List<Service> services = em.createNamedQuery("Service.getService", Service.class).setParameter(1, service_id).getResultList();

        if (services == null || services.isEmpty()) {
            throw new InvalidParameterException("Invalid service id.");
        } else if( services.size() != 1) {
            throw new InvalidParameterException("DB error.");
        } else {
            return services.get(0);

        }



    }




}
