package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Table(name = "`order`", schema = "telco_db", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @Column(name = "status", nullable = false, length = 45)
    private String status;

    @Column(name = "subscription_start", nullable = false)
    private LocalDate subscriptionStart;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "total_price", nullable = false, precision = 2)
    private BigDecimal totalPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "package_id", nullable = false)
    private ServicePackage packageId;

    @ManyToMany
    @JoinTable(name = "optional_product_ordered", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "opt_id"))
    private List<OptionalProduct> optionalProducts;

    public ServicePackage getPackageId() {
        return packageId;
    }

    public void setPackageId(ServicePackage packageId) {
        this.packageId = packageId;
    }

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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDate getSubscriptionStart() {
        return subscriptionStart;
    }

    public void setSubscriptionStart(LocalDate subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<OptionalProduct> getOptionalServices() {
        return optionalProducts;
    }

    public void setOptionalServices(List<OptionalProduct> optionalProducts) {
        this.optionalProducts = optionalProducts;
    }
}