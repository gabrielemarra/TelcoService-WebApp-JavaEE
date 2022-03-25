package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "optional_product", schema = "telco_db")
@NamedQuery(name = "Option.getOption", query = "SELECT o FROM Option o WHERE o.id = ?1")
@NamedQuery(name = "Option.getAllAvailableOptions", query = "SELECT o FROM Option o")

public class Option {
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

    @OneToMany(mappedBy = "option")
    private List<PackageOptionLink> packageOptionLinks; // better name: packagesLinkedToOption?

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

    public void setPackageOptionLinks(List<PackageOptionLink> packageOptionLinks) {this.packageOptionLinks = packageOptionLinks; }

    public List<PackageOptionLink> getPackageOptionLinks() {return packageOptionLinks; }
}