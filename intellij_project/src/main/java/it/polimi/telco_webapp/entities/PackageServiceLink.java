package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

@Table(name = "service_bundles", schema="telco_db")
@Entity
@NamedQuery(name = "PackageServiceLink.getServicesLinkedWithPackage", query = "SELECT s FROM PackageServiceLink s WHERE s.servicePackage = ?1")
@NamedQuery(name = "PackageServiceLink.getPackagesLinkedWithService", query = "SELECT s FROM PackageServiceLink s WHERE s.service = ?1")
@NamedQuery(name = "PackageServiceLink.getQuantity", query = "SELECT s.quantity FROM PackageServiceLink s WHERE s.servicePackage = ?1 AND s.service = ?2")

public class PackageServiceLink {
    @Id // not sure if we need this?
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_bundles_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "service_quantities")
    private Integer quantity;

    public Integer getQuantity() {return quantity;}
    public ServicePackage getServicePackage() {return servicePackage;}
    public Service getService() {return service;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}
    public void setServicePackage(ServicePackage servicePackage) {this.servicePackage = servicePackage;}
    public void setService(Service service) {this.service = service;}
}
