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
/* This view is the auditing table for the employee user. When a user is has three or more
* rejected orders, the user is added to this auditing table.
* */
public class AuditView {

    @Id
    private int user_id;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private LocalDateTime last;
    @Column
    private int num_rejected;
    @Column
    private float totalAmount;


    public int getUserId(){return user_id;}
    public String getUsername(){return username;}
    public String getEmail(){return email;}
    public LocalDateTime getLastTimestamp() {return last;}
    public int getNumRejected() {return num_rejected;}
    public float getTotal() {return totalAmount;}


    /* Since this is a view, the data is read-only. No setters. */
}