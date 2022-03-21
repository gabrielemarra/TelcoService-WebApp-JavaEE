package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "optional_product", schema = "telco_db")
@NamedQuery(name = "OptionalProduct.getOptionalProduct", query = "SELECT o FROM OptionalProduct o WHERE o.id = ?1")
@NamedQuery(name = "OptionalProduct.getAllAvailableOptionalProduct", query = "SELECT o FROM OptionalProduct o")

public class OptionalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opt_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "price", nullable = false, precision = 2)
    private BigDecimal price;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    @ManyToMany
    @JoinTable(name = "optional_product_available", joinColumns = @JoinColumn(name = "opt_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private List<ServicePackage> servicePackages;

    public List<ServicePackage> getServicePackages() {
        return servicePackages;
    }
  
    public void setServicePackages(List<ServicePackage> servicePackages) {
        this.servicePackages = servicePackages;
    }

    @ManyToMany
    @JoinTable(name = "optional_product_ordered", joinColumns = @JoinColumn(name = "opt_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders;


    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}