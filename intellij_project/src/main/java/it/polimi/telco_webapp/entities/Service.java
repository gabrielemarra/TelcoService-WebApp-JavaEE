package it.polimi.telco_webapp.entities;

// need to import an IDS folder

import it.polimi.telco_webapp.auxiliary.ServiceType;
import jakarta.persistence.*;

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

    @Column(name = "gig_extra", precision = 2)
    private Double gigExtra;

    @Column(name = "min_extra", precision = 2)
    private Double minExtra;

    @Column(name = "sms_extra", precision = 2)
    private Double smsExtra;

    @ManyToMany
    @JoinTable(name = "service_bundles", joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private List<ServicePackage> servicePackages;



    public Double getSmsExtra() {return smsExtra;}
    public Double getMinExtra() {return minExtra;}
    public Double getGigExtra() {return gigExtra;}
    public void setSmsExtra(Double smsExtra) {this.smsExtra = smsExtra;}
    public void setMinExtra(Double minExtra) {this.minExtra = minExtra;}
    public void setGigExtra(Double gigExtra) {this.gigExtra = gigExtra;}

    public Integer getSmsIncluded() {return smsIncluded;}
    public Integer getMinIncluded() {return minIncluded;}
    public Integer getGigIncluded() {return gigIncluded;}
    public void setSmsIncluded(Integer smsIncluded) {this.smsIncluded = smsIncluded;}
    public void setMinIncluded(Integer minIncluded) {this.minIncluded = minIncluded;}
    public void setGigIncluded(Integer gigIncluded) {this.gigIncluded = gigIncluded;}

    public Double getBasePrice1() {return basePrice1;}
    public Double getBasePrice2() {return basePrice2;}
    public Double getBasePrice3() {return basePrice3;}
    public void setBasePrice1(Double basePrice1) {this.basePrice1 = basePrice1;}
    public void setBasePrice2(Double basePrice2) {this.basePrice2 = basePrice2;}
    public void setBasePrice3(Double basePrice3) {this.basePrice3 = basePrice3;}


    public ServiceType getServiceType() {return type; }
    public void setType(ServiceType type) {this.type = type;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public List<ServicePackage> getPackageServices() {return servicePackages;}
    public void setPackageServices(List<ServicePackage> servicePackages) {this.servicePackages = servicePackages; }
}