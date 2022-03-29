package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Table(name = "service_package", schema = "telco_db")
@Entity
@NamedQuery(name = "ServicePackage.getAllServicePackages", query = "SELECT s FROM ServicePackage s")

public class ServicePackage implements Serializable {
    private static final long serialVersionUID = 1L;
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod;

    @OneToMany(mappedBy = "packageId")
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "optional_product_available", joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "opt_id"))
    private List<Option> options;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "service_bundles", joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> services;


    /* Public Getters and Setters */
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getValidityPeriod() {
        return validityPeriod;
    }
    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Service> getServices() {return services;}
    public void setServices(List<Service> services) {this.services = services;}

    public List<Option> getOptions() {
        return options;
    }
    public void setOptions(List<Option> options) {
        this.options = options;
    }

}