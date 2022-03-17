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
        Service service = new Service();

        service.setBasePrice1(bp1);
        service.setBasePrice2(bp2);
        service.setBasePrice3(bp3);

        switch(typeStr) {
            case "Fixed_Phone":
                service.setType(ServiceType.Fixed_Phone);
                /**
                 * These parameters are already initialized to zero in AddService.java servlet. This is done in the
                 * doPost function so that the unused parameters are still initialized. By commenting these setfunctions
                 * however, we ensure that parameters that are irrelevant to the selected plan are entered as NULL in
                 * the DB.
                 * service.setGigIncluded(0);
                 * service.setGigExtra(0.0);
                 * service.setMinIncluded(0);
                 * service.setMinExtra(0.0);
                 * service.setSmsIncluded(0);
                 * service.setSmsExtra(0.0);
                 *
                 */

                break;
            case "Mobile_Internet":
                if (gigIncl < 1 || gigExtra < 0) { // Internet plans need to offer Gig greater than 1!
                    throw new IllegalArgumentException("Correctly specify the parameters for the internet plan.");
                }
                /** Do we want to add a check if EXTRA/ erroneous fields are also entered?
                 *  if (smsIncl > 0 || smsExtra > 0 || minIncl > 0 || minExtra > 0) {
                 *      // throw exception?
                 *  }
                 */
                service.setType(ServiceType.Mobile_Internet);
                service.setGigIncluded(gigIncl);
                service.setGigExtra(gigExtra);
                /**
                 * These parameters are already initialized to zero in AddService.java servlet. This is done in the
                 * doPost function so that the unused parameters are still initialized.  By commenting these setfunctions
                 * however, we ensure that parameters that are irrelevant to the selected plan are entered as NULL in
                 * the DB.
                 * service.setMinIncluded(0);
                 * service.setMinExtra(0.0);
                 * service.setSmsIncluded(0);
                 * service.setSmsExtra(0.0);
                 *
                 */
                break;
            case "Fixed_Internet":
                if (gigIncl < 1 || gigExtra < 0) { // Internet plans need to offer Gig greater than 1!
                    throw new IllegalArgumentException("Correctly specify the parameters for the internet plan.");
                }
                service.setType(ServiceType.Fixed_Internet);
                service.setGigIncluded(gigIncl);
                service.setGigExtra(gigExtra);
                /**
                 * These parameters are already initialized to zero in AddService.java servlet. This is done in the
                 * doPost function so that the unused parameters are still initialized. By commenting these setfunctions
                 * however, we ensure that parameters that are irrelevant to the selected plan are entered as NULL in
                 * the DB.
                 * service.setMinIncluded(0);
                 * service.setMinExtra(0.0);
                 * service.setSmsIncluded(0);
                 * service.setSmsExtra(0.0);
                 *
                 */

                break;
            case "Mobile_Phone":
                if (smsIncl < 0 || smsExtra < 0 || minIncl < 0 || minExtra < 0) { // Phone plans CAN have included minutes or included sms set to zero:  imagine a sms-only plan
                    throw new IllegalArgumentException("Correctly specify the parameters for the mobile phone plan.");
                }
                service.setType(ServiceType.Mobile_Phone);
                service.setMinIncluded(minIncl);
                service.setMinExtra(minExtra);
                service.setSmsIncluded(smsIncl);
                service.setSmsExtra(smsExtra);
                /**
                 * These parameters are already initialized to zero in AddService.java servlet. This is done in the
                 * doPost function so that the unused parameters are still initialized. By commenting these setfunctions
                 * however, we ensure that parameters that are irrelevant to the selected plan are entered as NULL in
                 * the DB.
                 * service.setGigIncluded(0);
                 * service.setGigExtra(0.0);
                 *
                 */
                break;
            default:
                throw new IllegalArgumentException("Invalid plan type.");
        }

        em.persist(service);
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
