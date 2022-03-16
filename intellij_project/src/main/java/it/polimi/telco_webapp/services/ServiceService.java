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
     * @param typeStr: String: Fixed_Phone, Mobile_Phone, Fixed_Internet, or Mobile_Internet
     * @param bp1,bp2, bp3: Base prices 1,2,3
     * @param *_incl: The amount of gig/sms/or min included in the plan
     * @param *_extra: The COST for each gig/sms/ or min exceeding the included amount in the plan
     *
     * @return Service that was entered into the database.
     * @throws PersistenceException
     * @throws IllegalArgumentException: When the service type is not specified correctly, three baseprices are not
     * correctly provided, or plan parameters are not correctly specified.
     */
    public Service insertNewService(String typeStr, double bp1, double bp2, double bp3, int gigIncl, int minIncl, int smsIncl, double gigExtra, double minExtra, double smsExtra) throws PersistenceException, IllegalArgumentException{

        ServiceType type;
        Service service;
        int gig_incl, sms_incl, min_incl;
        double gig_extra, sms_extra, min_extra;

        switch(typeStr) {
            case "Fixed_Phone":
                type = ServiceType.Fixed_Phone;
                sms_incl = min_incl = 0;
                sms_extra = min_extra = 0.0;
                service = new Service();
                break;
            case "Mobile_Internet":
                if (gigIncl < 1 || gigExtra < 0) {
                    throw new IllegalArgumentException("Correctly specify the parameters for the internet plan.");
                }
                /** Do we want to add a check if EXTRA/ erroneous fields are also entered?
                 *  if (smsIncl > 0 || smsExtra > 0 || minIncl > 0 || minExtra > 0) {
                 *      // throw exception?
                 *  }
                 */
                type = ServiceType.Mobile_Internet;
                gig_incl = gigIncl;
                gig_extra = gigExtra;
                service = insertInternet(gig_incl, gig_extra);
                break;
            case "Fixed_Internet":
                if (gigIncl < 1 || gigExtra < 0) {
                    throw new IllegalArgumentException("Correctly specify the parameters for the internet plan.");
                }
                type = ServiceType.Fixed_Internet;
                gig_incl = gigIncl;
                gig_extra = gigExtra;
                service = insertInternet(gig_incl, gig_extra);
                break;
            case "Mobile_Phone":
                if (smsIncl < 0 || smsExtra < 0 || minIncl < 0 || minExtra < 0) {
                    throw new IllegalArgumentException("Correctly specify the parameters for the mobile phone plan.");
                }
                type = ServiceType.Mobile_Phone;
                sms_incl = smsIncl;
                sms_extra = smsExtra;
                min_incl = minIncl;
                min_extra = minExtra;
                service = insertMobilePhone(sms_incl, sms_extra, min_incl, min_extra);
                break;
            default:
                throw new IllegalArgumentException("Invalid plan type.");
        }

        service.setType(type);
        service.setBasePrice1(bp1);
        service.setBasePrice2(bp2);
        service.setBasePrice3(bp3);
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
