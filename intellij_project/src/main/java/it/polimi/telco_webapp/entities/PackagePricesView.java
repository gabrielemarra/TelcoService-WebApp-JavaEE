package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "package_prices", schema = "telco_db")
@ReadOnly
/* The three price tiers for the packages are FIXED, therefore this view compiles them here for
* easy access.
*
* */
public class PackagePricesView {

    @Id
    private int package_id;
    @Column
    private BigDecimal period1_total;
    @Column
    private BigDecimal period2_total;
    @Column
    private BigDecimal period3_total;



    public int getPackageId(){return package_id;}
    public BigDecimal getPeriod1Total(){return period1_total;}
    public BigDecimal getPeriod2Total(){return period2_total;}
    public BigDecimal getPeriod3Total() {return period3_total;}

    /* Since this is a view, the data is read-only. No setters. */
}