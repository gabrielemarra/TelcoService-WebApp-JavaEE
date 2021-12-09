package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;
import java.util.List;

import java.util.List;

@Table(name = "service_package", schema = "telco_db")
@Entity
public class ServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod;

    @ManyToMany
    @JoinTable(name = "optional_service_available", joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "opt_id"))
    private List<OptionalService> optionalServices;

    public List<OptionalService> getOptionalServices() {
        return optionalServices;
    }

    public void setOptionalServices(List<OptionalService> optionalServices) {
        this.optionalServices = optionalServices;
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
@OneToMany()


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
}