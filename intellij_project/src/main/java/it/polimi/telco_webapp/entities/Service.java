package it.polimi.telco_webapp.entities;

// need to import an IDS folder

import it.polimi.telco_webapp.auxiliary.ServiceType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "service", schema = "telco_db")
@Entity
@NamedQuery(name = "Service.getAllAvailableServices", query = "SELECT r FROM Service r")

public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Integer id;

    @Column(name = "type", nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    private ServiceType type;

    @Column(name = "base_price1", nullable = false)
    private BigDecimal basePrice1;

    @Column(name = "base_price2", nullable = false)
    private BigDecimal basePrice2;

    @Column(name = "base_price3", nullable = false)
    private BigDecimal basePrice3;

    @Column(name = "gig_included")
    private Integer gigIncluded;

    @Column(name = "min_included")
    private Integer minIncluded;

    @Column(name = "sms_included")
    private Integer smsIncluded;

    @Column(name = "gig_extra")
    private BigDecimal gigExtra;

    @Column(name = "min_extra")
    private BigDecimal minExtra;

    @Column(name = "sms_extra")
    private BigDecimal smsExtra;

    @ManyToMany
    @JoinTable(name = "service_bundles", joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private List<ServicePackage> servicePackages;



    public BigDecimal getSmsExtra() {return smsExtra;}
    public BigDecimal getMinExtra() {return minExtra;}
    public BigDecimal getGigExtra() {return gigExtra;}
    public void setSmsExtra(BigDecimal smsExtra) {this.smsExtra = smsExtra;}
    public void setMinExtra(BigDecimal minExtra) {this.minExtra = minExtra;}
    public void setGigExtra(BigDecimal gigExtra) {this.gigExtra = gigExtra;}

    public Integer getSmsIncluded() {return smsIncluded;}
    public Integer getMinIncluded() {return minIncluded;}
    public Integer getGigIncluded() {return gigIncluded;}
    public void setSmsIncluded(Integer smsIncluded) {this.smsIncluded = smsIncluded;}
    public void setMinIncluded(Integer minIncluded) {this.minIncluded = minIncluded;}
    public void setGigIncluded(Integer gigIncluded) {this.gigIncluded = gigIncluded;}

    public BigDecimal getBasePrice1() {return basePrice1;}
    public BigDecimal getBasePrice2() {return basePrice2;}
    public BigDecimal getBasePrice3() {return basePrice3;}
    public void setBasePrice1(BigDecimal basePrice1) {this.basePrice1 = basePrice1;}
    public void setBasePrice2(BigDecimal basePrice2) {this.basePrice2 = basePrice2;}
    public void setBasePrice3(BigDecimal basePrice3) {this.basePrice3 = basePrice3;}


    public ServiceType getServiceType() {return type; }
    public void setType(ServiceType type) {this.type = type;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public List<ServicePackage> getPackageServices() {return servicePackages;}
    public void setPackageServices(List<ServicePackage> servicePackages) {this.servicePackages = servicePackages; }
}