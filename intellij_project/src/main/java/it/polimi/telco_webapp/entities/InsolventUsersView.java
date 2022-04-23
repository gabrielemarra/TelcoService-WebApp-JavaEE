package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "insolvent_users_view", schema = "telco_db")
@ReadOnly
/* This view shows all the insolvent users (users with 1 or more rejected orders) and shows the
* TOTAL amount that the user has not paid across all of their rejected orders.
*
* */
public class InsolventUsersView {

    @Id
    private int user_id;
    @Column
    private int num_orders;
    @Column
    private float total;

    public int getUserId(){return user_id;}
    public int getNumOrders() {return num_orders;}
    public float getTotal() {return total;}
    /* Since this is a view, the data is read-only. No setters. */
}