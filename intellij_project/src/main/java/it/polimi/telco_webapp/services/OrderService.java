package it.polimi.telco_webapp.services;
import it.polimi.telco_webapp.auxiliary.OrderStatus;
import it.polimi.telco_webapp.entities.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Stateless(name = "OrderService")

public class OrderService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;

    public OrderService() {}

    /**
     * Create a new Order and store in the Persistence Context
     *
     * @param subscriptionStartDate The starting date of the subscription
     * @param user                  User entity (NOT ID) that place the order
     * @param servicePackage        The service package included in the order
     * @param optionalProductList   The optional products included in the order
     * @return The new created order
     */
    public Order insertNewOrder(LocalDate subscriptionStartDate, User user, ServicePackage servicePackage, List<OptionalProduct> optionalProductList) {
        Order newOrder = new Order();
        newOrder.setPackageId(servicePackage);
        newOrder.setUser(user);
        newOrder.setSubscriptionStart(subscriptionStartDate);
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setOptionalProductOrderedList(optionalProductList);







        em.persist(newOrder);
        em.flush();
        return newOrder;
    }

    public Order getOrder(int orderId) {
        Order order = em.find(Order.class, orderId);
        if (order == null) {
            throw new EntityNotFoundException("Cannot find order with ID: " + orderId);
        }
        return order;
    }

    public List<Order> getAllOrdersByPackage(ServicePackage servicePackage) {
        List<Order> orders = em.createNamedQuery("Order.getAllOrdersByPackage", Order.class).setParameter(1, servicePackage).getResultList();
        if (orders == null || orders.isEmpty()) {
            // orders CAN be empty....
            throw new EntityNotFoundException("No orders have been placed for service package with ID:" + servicePackage.getId());
        }
        return orders;
    }

    public List<Order> getAllOrdersByOption(OptionalProduct option) {
        List<Order> orders = em.createNamedQuery("Order.getAllOrdersByOption", Order.class).setParameter(1, option).getResultList();
        if (orders == null || orders.isEmpty()) {
            // orders CAN be empty....
            throw new EntityNotFoundException("No orders have included option with ID:" + option.getId());
        }
        return orders;
    }

    public List<Order> getAllOrdersByUser(User user) {
        List<Order> orders = em.createNamedQuery("Order.getAllOrdersByUser", Order.class).setParameter(1, user).getResultList();
        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("No orders have been placed by user or the userID is invalid ID: " + user.getId());
        }
        return orders;
    }

    public List<Order> getAllRejectedOrders() {
        List <Order> orders = em.createNamedQuery("Order.getAllRejectedOrders",Order.class).getResultList();
        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("No rejected orders exist");
        }
        return orders;
    }

    public void changeOrderStatus(int order_id, OrderStatus status) {
        Order order = this.getOrder(order_id);
        order.setStatus(status);
        // TODO: maybe add some checks here...
    }

}
