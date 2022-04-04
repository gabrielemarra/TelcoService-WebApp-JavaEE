package it.polimi.telco_webapp.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "suspended_orders_view", schema = "telco_db")
@ReadOnly
public class SuspendedOrdersView {

    @Id
    private int order_id;
    @Column
    private int user_id;
    @Column
    private int package_id;
    @Column
    private float total;

    public int getOrderId(){return order_id;}
    public int getUserId(){return user_id;}
    public int getPackageId(){return package_id;}
    public float getTotal() {return total;}

    /* Since this is a view, the data is read-only. No setters. */
}