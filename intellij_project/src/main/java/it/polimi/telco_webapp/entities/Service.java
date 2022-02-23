package it.polimi.telco_webapp.entities;

// need to import an IDS folder

import jakarta.el.TypeConverter;
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

//    @Convert(converter = ServiceTypeConverter.class)
    @Column(name = "type", nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    private ServiceType type;

    @Column(name = "base_price1", nullable = false, precision = 2)
    private Double basePrice1;

    @Column(name = "base_price2", nullable = false, precision = 2)
    private Double basePrice2;

    @Column(name = "base_price3", nullable = false, precision = 2)
    private Double basePrice3;

    @Column(name = "gig_included")
    private Integer gigIncluded;

    @Column(name = "min_included")
    private Integer minIncluded;

    @Column(name = "sms_included")
    private Integer smsIncluded;

    @Column(name = "gig_extra")
    private Double gigExtra;

    @Column(name = "min_extra")
    private Double minExtra;

    @Column(name = "sms_extra")
    private Double smsExtra;

    @ManyToMany
    @JoinTable(name = "service_bundles", joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private List<ServicePackage> servicePackages;

    public Integer getSmsExtra() {
        return smsExtra;
    }

    public void setSmsExtra(Double smsExtra) {
        this.smsExtra = smsExtra;
    }

    public Double getMinExtra() {
        return minExtra;
    }

    public void setMinExtra(Double minExtra) {
        this.minExtra = minExtra;
    }

    public Double getGigExtra() {
        return gigExtra;
    }

    public void setGigExtra(Double gigExtra) {
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

    public Double getBasePrice3() {
        return basePrice3;
    }

    public void setBasePrice3(Double basePrice3) {
        this.basePrice3 = basePrice3;
    }

    public Double getBasePrice2() {
        return basePrice2;
    }

    public void setBasePrice2(Double basePrice2) {
        this.basePrice2 = basePrice2;
    }

    public Double getBasePrice1() {
        return basePrice1;
    }

    public void setBasePrice1(Double basePrice1) {
        this.basePrice1 = basePrice1;
    }

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