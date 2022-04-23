package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "service_package_view", schema = "telco_db")
@ReadOnly
/* This view centralizes the stats regarding package sales including total number of purchases,
* total number of purchases by period (1/2/3), total value in sales with optional products, and
* total value in sales without optional products
* */
public class ServicePackageView {

    @Id
    private int package_id;
    @Column
    private int purchases_total;
    @Column
    private int purchases_period1;
    @Column
    private int purchases_period2;
    @Column
    private int purchases_period3;
    @Column
    private float sales_base;
    @Column
    private float sales_total;
    @Column
    private float avg_options_ordered;

    public int getPackage_id(){return package_id;}
    public int getPurchasesTotal() {return purchases_total;}
    public int getPurchasesPeriod1() {return purchases_period1;}
    public int getPurchasesPeriod2() {return purchases_period2;}
    public int getPurchasesPeriod3() {return purchases_period3;}
    public float getSalesBase() {return sales_base;}
    public float getSalesTotal() {return sales_total;}
    public float getAvgOptionsOrdered(){return avg_options_ordered;}
    /* Since this is a view, the data is read-only. No setters. */
}