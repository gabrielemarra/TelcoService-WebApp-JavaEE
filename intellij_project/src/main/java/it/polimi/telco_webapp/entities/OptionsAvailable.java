package it.polimi.telco_webapp.entities;

import it.polimi.telco_webapp.services.OptionsAvailableService;
import jakarta.persistence.*;

import javax.persistence.criteria.CriteriaBuilder;

@Table(name = "optional_product_available", schema="telco_db")
@Entity
public class OptionsAvailable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "options_available_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "opt_id")
    private OptionalProduct optionalProduct;

    @Column(name = "option_quantities")
    private Integer quantity;

    public Integer getQuantity() {return quantity;}
    public ServicePackage getServicePackage() {return servicePackage;}
    public OptionalProduct getOptionalProduct() {return optionalProduct;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}
    public void setServicePackage(ServicePackage service) {this.servicePackage = service;}
    public void setOptionalProduct(OptionalProduct option) {this.optionalProduct = option;}




}


/*
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Column(name = "authorized", nullable = false)
    private Integer authorized;

    public Integer getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Integer authorized) {
        this.authorized = authorized;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
*
*
*
*
* */