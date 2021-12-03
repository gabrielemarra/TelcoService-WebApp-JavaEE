package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "service", schema = "telco_db")
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Integer id;

    @Column(name = "type", nullable = false, length = 45)
    private String type;

    @Column(name = "base_price1", nullable = false, precision = 2)
    private BigDecimal basePrice1;

    @Column(name = "base_price2", nullable = false, precision = 2)
    private BigDecimal basePrice2;

    @Column(name = "base_price3", nullable = false, precision = 2)
    private BigDecimal basePrice3;

    @Column(name = "gig_jncluded")
    private Integer gigJncluded;

    @Column(name = "min_jncluded")
    private Integer minJncluded;

    @Column(name = "sms_jncluded")
    private Integer smsJncluded;

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

    public Integer getSmsJncluded() {
        return smsJncluded;
    }

    public void setSmsJncluded(Integer smsJncluded) {
        this.smsJncluded = smsJncluded;
    }

    public Integer getMinJncluded() {
        return minJncluded;
    }

    public void setMinJncluded(Integer minJncluded) {
        this.minJncluded = minJncluded;
    }

    public Integer getGigJncluded() {
        return gigJncluded;
    }

    public void setGigJncluded(Integer gigJncluded) {
        this.gigJncluded = gigJncluded;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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