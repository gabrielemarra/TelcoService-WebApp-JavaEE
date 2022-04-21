package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "options_ordered_prices", schema = "telco_db")
@ReadOnly
/* This view shows the total (not monthly) amount that options add to the monthly cost of one order.
 * (IE, this amount + sales_base = sales_total)
 * */
public class OptionsOrderedPricesView {

    @Id
    private int package_id;
    @Column
    private int order_id;
    @Column
    private float sum_sales_of_options;


    public int getPackageId(){return package_id;}
    public int getOrderId(){return order_id;}
    public float getSumOfSales(){return sum_sales_of_options;}
    /* Since this is a view, the data is read-only. No setters. */
}