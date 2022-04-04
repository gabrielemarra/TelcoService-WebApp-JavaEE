package it.polimi.telco_webapp.views;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "service_package_view", schema = "telco_db")
@ReadOnly
public class ServicePackageView {

    @Id
    private int package_id;
    @Column
    private float purchases_total;
    @Column
    private float purchases_period1;
    @Column
    private float purchases_period2;
    @Column
    private float purchases_period3;
    @Column
    private float sales_base;
    @Column
    private float sales_total;

    public int getPackage_id(){return package_id;}
    public float getPurchasesTotal() {return purchases_total;}
    public float getPurchasesPeriod1() {return purchases_period1;}
    public float getPurchasesPeriod2() {return purchases_period2;}
    public float getPurchasesPeriod3() {return purchases_period3;}
    public float getSalesBase() {return sales_base;}
    public float getSalesTotal() {return sales_total;}

    /* Since this is a view, the data is read-only. No setters. */
}