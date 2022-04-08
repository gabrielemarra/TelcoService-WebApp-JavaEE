package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;

import java.time.LocalDateTime;


@Entity
@Table(name = "audit", schema = "telco_db")
@ReadOnly
public class PackagePricesView {

    @Id
    private int package_id;
    @Column
    private float period1_total;
    @Column
    private float period2_total;
    @Column
    private float period3_total;



    public int getPackageId(){return package_id;}
    public float getPeriod1Total(){return period1_total;}
    public float getPeriod2Total(){return period2_total;}
    public float getPeriod3Total() {return period3_total;}

    /* Since this is a view, the data is read-only. No setters. */
}