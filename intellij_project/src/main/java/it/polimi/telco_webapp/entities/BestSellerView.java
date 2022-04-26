package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.ReadOnly;

import java.math.BigDecimal;


@Entity
@ReadOnly
@NamedQuery(name = "BestSeller.getBestSeller", query = "SELECT b FROM BestSellerView b")
@Table(name = "best_seller_optional_product", schema = "telco_db")
/* This view lists the optional products in order from greatest to least total sales made in purchases across all
* orders. To get the best-selling optional product, just retrieve the first entry in the view.
* */
public class BestSellerView {

    @Id
    private int opt_id;
    @Column
    private String optname;
    @Column
    private BigDecimal sales_value;


    public int getOptId(){return opt_id;}
    public String getOptName() {return optname;}
    public BigDecimal getSalesValue() {return sales_value;}
    /* Since this is a view, the data is read-only. No setters. */
}