package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;
import java.util.List;
import java.math.BigDecimal;

@Table(name = "optional_service")
@Entity
public class OptionalService {
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
    @JoinTable(name = "optional_service_available", joinColumns = @JoinColumn(name = "opt_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private List<ServicePackage> servicePackages;

    public List<ServicePackage> getServicePackages() {
        return servicePackages;
    }
    public void setServicePackages(List<ServicePackage> servicePackages) {
        this.servicePackages = servicePackages;
    }

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
}