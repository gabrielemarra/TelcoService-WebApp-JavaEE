package it.polimi.telco_webapp.entities;

// need to import an IDS folder

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "service", schema = "telco_db")
@Entity
@NamedQuery(name = "Service.getService", query = "SELECT r FROM Service r WHERE r.id = ?1")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Integer id;

    @Column(name = "type", nullable = false, length = 45)
    private ServiceType type;

    @Column(name = "base_price1", nullable = false, precision = 2)
    private BigDecimal basePrice1;

    @Column(name = "base_price2", nullable = false, precision = 2)
    private BigDecimal basePrice2;

    @Column(name = "base_price3", nullable = false, precision = 2)
    private BigDecimal basePrice3;

    @Column(name = "gig_included")
    private Integer gigIncluded;

    @Column(name = "min_included")
    private Integer minIncluded;

    @Column(name = "sms_included")
    private Integer smsIncluded;

    @Column(name = "gig_extra")
    private Integer gigExtra;

    @Column(name = "min_extra")
    private Integer minExtra;

    @Column(name = "sms_extra")
    private Integer smsExtra;

    @ManyToMany
    @JoinTable(name = "service_bundles", joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private List<ServicePackage> servicePackages;

    public Integer getSmsExtra() {
        return smsExtra;
    }

    public void setSmsExtra(Integer smsExtra) {
        this.smsExtra = smsExtra;
    }

    public Integer getMinExtra() {
        return minExtra;
    }

    public void setMinExtra(Integer minExtra) {
        this.minExtra = minExtra;
    }

    public Integer getGigExtra() {
        return gigExtra;
    }

    public void setGigExtra(Integer gigExtra) {
        this.gigExtra = gigExtra;
    }

    public Integer getSmsIncluded() {
        return smsIncluded;
    }

    public void setSmsIncluded(Integer smsJncluded) {
        this.smsIncluded = smsJncluded;
    }

    public Integer getMinIncluded() {
        return minIncluded;
    }

    public void setMinIncluded(Integer minJncluded) {
        this.minIncluded = minJncluded;
    }

    public Integer getGigIncluded() {
        return gigIncluded;
    }

    public void setGigIncluded(Integer gigIncluded) {
        this.gigIncluded = gigIncluded;
    }

    public BigDecimal getBasePrice3() {
        return basePrice3;
    }

    public void setBasePrice3(BigDecimal basePrice3) {
        this.basePrice3 = basePrice3;
    }

    public BigDecimal getBasePrice2() {
        return basePrice2;
    }

    public void setBasePrice2(BigDecimal basePrice2) {
        this.basePrice2 = basePrice2;
    }

    public BigDecimal getBasePrice1() {
        return basePrice1;
    }

    public void setBasePrice1(BigDecimal basePrice1) {
        this.basePrice1 = basePrice1;
    }

    /*
     * public String getType() {
     *    return type;
     * }
     */


    public ServiceType getServiceType() {return type; }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ServicePackage> getServicePackages() {
        return servicePackages;
    }

    public void setServicePackages(List<ServicePackage> servicePackages) {
        this.servicePackages = servicePackages;
    }
}