package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.ReadOnly;


@Entity
@Table(name = "best_seller_optional_product", schema = "telco_db")
@ReadOnly
@NamedQuery(name = "BestSeller.getBestSeller", query = "SELECT b FROM BestSellerView b")
////         BestSellerView bestSeller = em.createNamedQuery("BestSeller.getBestSeller", BestSellerView.class);
public class BestSellerView {

    @Id
    private int opt_id;
    @Column
    private String optname;
    @Column
    private float sales_value;


    public int getOptId(){return opt_id;}
    public String getOptName() {return optname;}
    public float getSalesValue() {return sales_value;}
    /* Since this is a view, the data is read-only. No setters. */
}