package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "service_package", schema = "telco_db")
@Entity
@NamedQuery(name = "ServicePackage.getOneServicePackage", query = "SELECT s FROM ServicePackage s WHERE s.id = ?1")
@NamedQuery(name = "ServicePackage.getAllServicePackages", query = "SELECT s FROM ServicePackage s")

public class ServicePackage {

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

    @OneToMany(mappedBy = "servicePackage")
    private List<PackageOptionLink> optionsLinkedToPackage;

    @OneToMany(mappedBy = "servicePackage")
    private List<PackageServiceLink> servicesLinkedToPackage;

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

    // this getter is should be implemented in  packageservicelink/ packageservicelinkservice
    //public List<Service> getServices() {return services;}
    // this setter is should be implemented in  packageservicelink/ packageservicelinkservice
    //public void setServices(List<Service> services) {this.services = services;}

    public void setOptionsLinkedToPackage(List<PackageOptionLink> optionsLinkedToPackage) {this.optionsLinkedToPackage = optionsLinkedToPackage;}

    public List<PackageOptionLink> getOptionsLinkedToPackage() {return optionsLinkedToPackage;}

    public void setServicesLinkedToPackage(List<PackageServiceLink> servicesLinkedToPackage) {this.servicesLinkedToPackage = servicesLinkedToPackage;}

    public List<PackageServiceLink> getServicesLinkedToPackage() {return servicesLinkedToPackage;}

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}