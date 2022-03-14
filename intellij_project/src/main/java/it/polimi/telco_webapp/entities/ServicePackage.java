package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Table(name = "service_package", schema = "telco_db")
@Entity
@NamedQuery(name = "ServicePackage.getOneServicePackage", query = "SELECT s FROM ServicePackage s WHERE s.id = ?1")
@NamedQuery(name = "ServicePackage.getAllServicePackages", query = "SELECT s FROM ServicePackage s")
//         List<ServicePackage> bundles = em.createNamedQuery("ServicePackage.getServicePackages", ServicePackage.class).setParameter(1, package_id).getResultList();
//        List<OptionalProduct> options = em.createNamedQuery("OptionalProduct.getOptionalProducts", OptionalProduct.class).setParameter(1, package_id).getResultList();

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
    private List <Order> orders;

    @ManyToMany
    @JoinTable(name = "optional_product_available", joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "opt_id"))
    private List<OptionalProduct> optionalProducts;

    public List<OptionalProduct> getOptionalProducts() {
        return optionalProducts;
    }

    public void setOptionalServices(List<OptionalProduct> optionalProducts) {
        this.optionalProducts = optionalProducts;
    }

    @ManyToMany
    @JoinTable(name = "service_bundles", joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> services;

    /* I think we also need this
     * TODO: check fetch type
     * TODO: check that mappedby string is correct
     * TODO: check if we need to add targetEntity
     * @OneToMany(fetch = FetchType.LAZY, mappedBy = "service_package", cascade = CascadeType.ALL)
     * private List<Order> orders;
     */


    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
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

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}