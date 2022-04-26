package it.polimi.telco_webapp.services;


import it.polimi.telco_webapp.entities.PackagePricesView;
import it.polimi.telco_webapp.entities.ServicePackageView;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import java.math.BigDecimal;
import java.util.List;

@Stateless(name = "PackagePricesView")
public class PackagePricesViewService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public PackagePricesViewService() {}

    public BigDecimal getBasePrice(int package_id, int period) {
        BigDecimal price = BigDecimal.ZERO;
        PackagePricesView packagePrices = em.find(PackagePricesView.class, package_id);
        if (period == 1) {
            price = packagePrices.getPeriod1Total();
        } else if (period == 2) {
            price = packagePrices.getPeriod2Total();
        } else { // period == 3
            price = packagePrices.getPeriod3Total();
        }
        return price;

    }

}





