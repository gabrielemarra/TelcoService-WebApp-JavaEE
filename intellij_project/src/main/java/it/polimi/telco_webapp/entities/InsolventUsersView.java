package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "insolvent_users_view", schema = "telco_db")
@ReadOnly
public class InsolventUsersView {

    @Id
    private int user_id;
    @Column
    private int num_orders;
    @Column
    private float delinquent_total;


    public int getUserId(){return user_id;}
    public int getNumOrders() {return num_orders;}
    public float getDelinquentTotal() {return delinquent_total;}
    /* Since this is a view, the data is read-only. No setters. */
}