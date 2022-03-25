package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

@Table(name = "optional_product_available", schema="telco_db")
@Entity
@NamedQuery(name = "PackageOptionLink.getOptionsLinkedWithPackage", query = "SELECT o FROM PackageOptionLink o WHERE o.servicePackage = ?1")
@NamedQuery(name = "PackageOptionLink.getPackagesLinkedWithOption", query = "SELECT o FROM PackageOptionLink o WHERE o.option = ?1")
@NamedQuery(name = "PackageOptionLink.getQuantity", query = "SELECT o.quantity FROM PackageOptionLink o WHERE o.servicePackage = ?1 AND o.option = ?2")

public class PackageOptionLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "options_available_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "opt_id")
    private Option option;

    @Column(name = "option_quantities")
    private Integer quantity;

    public Integer getQuantity() {return quantity;}
    public ServicePackage getServicePackage() {return servicePackage;}
    public Option getOption() {return option;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}
    public void setServicePackage(ServicePackage service) {this.servicePackage = service;}
    public void setOption(Option option) {this.option = option;}




}

