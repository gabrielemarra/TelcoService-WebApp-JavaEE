package it.polimi.telco_webapp.entities;

import it.polimi.telco_webapp.auxiliary.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer_order", schema = "telco_db", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@NamedQuery(name = "Order.getOrder", query = "SELECT o FROM Order o WHERE o.id = ?1")
@NamedQuery(name = "Order.getOrderBySingleUser", query = "SELECT o FROM Order o WHERE o.user = ?1")
@NamedQuery(name = "Order.getRejectedOrdersBySingleUser", query = "SELECT o FROM Order o WHERE o.user = ?1 AND o.status = 'REJECTED'")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 45)
    private OrderStatus status;

    @Column(name = "subscription_start", nullable = false)
    private LocalDate subscriptionStart;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "total_price", nullable = false, precision = 2)
    private BigDecimal totalPrice;

    @Column(name = "base_cost", nullable = false, precision = 2)
    private BigDecimal baseCost;

    @Column(name = "chosen_validity_period", nullable = false)
    private Integer chosenValidityPeriod;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "package_id", nullable = false)
    private ServicePackage packageId;

    @ManyToMany
    @JoinTable(name = "optional_product_ordered", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "opt_id"))
    private List<OptionalProduct> optionalProductOrderedList;

    public ServicePackage getPackageId() {
        return packageId;
    }

    public void setPackageId(ServicePackage packageId) {
        this.packageId = packageId;
    }

    public void setChosenValidityPeriod(int period) { this.chosenValidityPeriod = period; }

    public int getChosenValidityPeriod() { return chosenValidityPeriod; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getBaseCost() {
        return baseCost;
    }

    public void setBaseCost() {
        BigDecimal baseCost = this.totalPrice;

        List<OptionalProduct> optionalProducts = this.getOptionalServices();
        for (OptionalProduct prod: optionalProducts) {
            baseCost = baseCost.subtract(prod.getPrice());
        }


    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDate getSubscriptionStart() {
        return subscriptionStart;
    }

    public void setSubscriptionStart(LocalDate subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<OptionalProduct> getOptionalServices() {
        return optionalProductOrderedList;
    }

    public void setOptionalProductOrderedList(List<OptionalProduct> optionalProducts) {
        this.optionalProductOrderedList = optionalProducts;
    }

    public Order(){};
}