package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "suspended_orders_view", schema = "telco_db")
@ReadOnly
/* TODO: can we delete this view?
* */

public class SuspendedOrdersView {

    @Id
    private int order_id;
    @Column
    private int user_id;
    @Column
    private int package_id;
    @Column
    private float delinquent_amount;

    public int getOrderId(){return order_id;}
    public int getUserId(){return user_id;}
    public int getPackageId(){return package_id;}
    public float getAmount() {return delinquent_amount;}
    /* Since this is a view, the data is read-only. No setters. */
}