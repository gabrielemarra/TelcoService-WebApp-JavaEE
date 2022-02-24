package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Service;
import it.polimi.telco_webapp.auxiliary.ServiceType;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;
import java.util.List;

@Stateless(name = "ServiceService")

public class ServiceService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public ServiceService() {
    }

    /**
     * Inserts a new service into the database.
     * @param service_id: Serivce id of the service
     * @param type: Enum Fixed_Phone, Mobile_Phone, Fixed_Internet, or Mobile_Internet
     * @param basePrices: Array with the three tiers of prices
     * @param incl: Array with the cost of the included amounts (of sms/min or gig)
     * @param extra: Array with the cost of extra services (of sms/min or gig) that exceed the amount included in the plan
     *
     * @return Service that was entered into the database.
     * @throws PersistenceException
     * @throws IllegalArgumentException: When the service type is not specified correctly, three baseprices are not
     * correctly provided, or plan parameters are not correctly specified.
     */
    public Service insertNewService(int service_id, ServiceType type, Double[] basePrices, int[] incl, double[] extra) throws PersistenceException, IllegalArgumentException{
        if(basePrices.length != 3) {
            throw new IllegalArgumentException("Correctly specify the base places for the plan.");
        }

        Service service;
        switch(type) {
            case Fixed_Phone:
                service = new Service();
                break;
            case Mobile_Internet: case Fixed_Internet:
                if (incl.length != 1 || extra.length != 1) {
                    throw new IllegalArgumentException("Correctly specify the parameters for the internet plan.");
                }
                int gig_incl = incl[0];
                double gig_extra = extra[0];
                service = insertInternet(gig_incl, gig_extra);
                break;
            case Mobile_Phone:
                if (incl.length != 2 || extra.length != 2) {
                    throw new IllegalArgumentException("Correctly specify the parameters for the mobile phone plan.");
                }
                int sms_incl = incl[0];
                double sms_extra = extra[0];
                int min_incl = incl[1];
                double min_extra = extra[1];
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

    /**
     * Helper method to interpret planParams[] as the amount of gig included from planParam[0] and the cost for extra
     * gig from planParams[1]
     * @param gig_incl: Amount of gig included in the internet plan
     * @param gig_extra: Cost of extra gig in the internet plan
     * @return The service with the gig inclu/extra parameters specified.
     */

    private Service insertInternet(int gig_incl, double gig_extra) {
        Service service = new Service();
        service.setGigIncluded(gig_incl);
        service.setGigExtra(gig_extra);

        return service;
    }

    /**
     * Helper method to interpret planParams[] as the amount of sms included, cost of extra sms, amount of min included,
     *  and cost of extra min
     * @param sms_incl: Amount of sms included in the plan
     * @param sms_extra: Cost of extra sms exceeding the amount allotted in the plan.
     * @param min_incl: Number of min included in the plan
     * @param min_extra: Cost of extra min exceeding the amount allotted in the plan.
     * @return The service with the plan parameters specified.
     */

    private Service insertMobilePhone(int sms_incl, double sms_extra, int min_incl, double min_extra) {
        Service service = new Service();
        service.setSmsIncluded(sms_incl);
        service.setSmsExtra(sms_extra);
        service.setMinIncluded(min_incl);
        service.setMinExtra(min_extra);
        return service;
    }

    /**
     * Get a service from database.
     * @param service_id: Service id from the service of interest to get from the database
     * @return: the service with the matching service id
     */
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
