package it.polimi.telco_webapp.entities;

import it.polimi.telco_webapp.auxiliary.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer_order", schema = "telco_db", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@NamedQuery(name = "Order.getRejectedOrdersBySingleUser", query = "SELECT o FROM Order o WHERE o.user = ?1 AND o.status = it.polimi.telco_webapp.auxiliary.OrderStatus.REJECTED")
@NamedQuery(name = "Order.getAllRejectedOrders", query = "SELECT o FROM Order o WHERE o.status = it.polimi.telco_webapp.auxiliary.OrderStatus.REJECTED")
@NamedQuery(name = "Order.getAllOrdersByPackage", query = "SELECT o FROM Order o WHERE o.packageId = ?1")
@NamedQuery(name = "Order.getAllOrdersByOption", query = "SELECT o FROM Order o WHERE o.optionalProductOrderedList = ?1")
@NamedQuery(name = "Order.getAllOrdersByUser", query = "SELECT o FROM Order o WHERE o.user = ?1")

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


    @Column(name = "timestamp")
    private java.sql.Timestamp timestamp;
    //private LocalDateTime timestamp;

    //@Column(name = "timestamp", nullable = false)
    //private LocalDateTime timestamp;

    //@Column(name = "total_price", nullable = false, precision = 2)
    //private BigDecimal totalPrice;

    @Column(name = "chosen_validity_period", nullable = false)
    private Integer chosenValidityPeriod;

    /* TODO-NE: This used to be type User... it should be an integer.. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* TODO: should packageID be an integer? (1/3) */
    @ManyToOne(optional = false)
    @JoinColumn(name = "package_id", nullable = false)
    private ServicePackage packageId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "optional_product_ordered", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "opt_id"))
    private List<OptionalProduct> optionalProductOrderedList;

    /* TODO: should packageID be an integer? (2/3) */
    public ServicePackage getPackageId() {
        return packageId;
    }

    /* TODO: should packageID be an integer? (3/3) */
    public void setPackageId(ServicePackage packageId) {
        this.packageId = packageId;
    }

    public void setChosenValidityPeriod(int period) {
        this.chosenValidityPeriod = period;
    }

    public int getChosenValidityPeriod() {
        return chosenValidityPeriod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {

        //public BigDecimal getTotalPrice() {
        //    return totalPrice;
        //}

        //public void setTotalPrice(BigDecimal totalPrice) {
        //    this.totalPrice = totalPrice;
        //}

        //public LocalDateTime getTimestamp() {
        return timestamp;
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

    public Order() {
    }

    ;
}