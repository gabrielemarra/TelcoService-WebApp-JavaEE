package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Stateless(name = "OrderService")
public class OrderService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OrderService() {
    }

    /**
     * Create a new Order and store in the Persistence Context
     *
     * @param subscriptionStartDate The starting date of the subscription
     * @param user                  User entity (NOT ID) that place the order
     * @param servicePackage        The service package included in the order
     * @param optionalProductList   The optional products included in the order
     * @return The new created order
     */
    public Order insertOrder(LocalDate subscriptionStartDate, User user, ServicePackage servicePackage, List<OptionalProduct> optionalProductList) {
        Order newOrder = new Order();
        newOrder.setPackageId(servicePackage);
        newOrder.setUser(user);
        newOrder.setSubscriptionStart(subscriptionStartDate);
        newOrder.setStatus(OrderStatus.PENDING);

        //Check if the OptionalProducts are available for the selected ServicePackage OR null, then add the opt. prod. list to the order
        if (optionalProductList == null || !servicePackage.getOptionalProducts().containsAll(optionalProductList)) {
            throw new IllegalArgumentException("Some selected Optional Products are not compatible with the selected Service Package");
        }
        newOrder.setOptionalProductOrderedList(optionalProductList);

        //Extract the prices from the selected optional products
        List<BigDecimal> optionalProductPriceList = new ArrayList<>();
        for (OptionalProduct optionalProduct : optionalProductList) {
            optionalProductPriceList.add(optionalProduct.getPrice());
        }

        //Sum all the priceI don't know if this works, it's a mess working with BigDecimal
        BigDecimal optionalProductsPrice = optionalProductPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        //Extract the correct prices from the services included in the service package
        List<BigDecimal> servicesPriceList = new ArrayList<>();
        for (Service service : servicePackage.getServices()) {
            switch (servicePackage.getValidityPeriod()) {
                case 1:
                    servicesPriceList.add(service.getBasePrice1());
                case 2:
                    servicesPriceList.add(service.getBasePrice2());
                case 3:
                    servicesPriceList.add(service.getBasePrice3());
            }
        }
        //Sum all the price of the services included in the service package
        BigDecimal servicesPrice = servicesPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPrice = servicesPrice.add(optionalProductsPrice);

        newOrder.setTotalPrice(totalPrice);

        em.persist(newOrder);
        return newOrder;
    }

    //TODO use FIND instead of NamedQuery
    public Order getOrder(int orderId) {
        List<Order> orders = em.createNamedQuery("Order.getOrder", Order.class).setParameter(1, orderId).getResultList();
        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("Invalid orderID");
        } else if (orders.size() != 1) {
            throw new IllegalArgumentException("Internal database error: too many result for a single ID");
        } else {
            return orders.get(0);
        }
    }

    public List<Order> getAllOrderCreatedByUser(int userId) {
        List<Order> orders = em.createNamedQuery("Order.getOrder", Order.class).setParameter(1, userId).getResultList();
        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("User has no orders or the userID is invalid");
        } else {
            return orders;
        }
    }

}
