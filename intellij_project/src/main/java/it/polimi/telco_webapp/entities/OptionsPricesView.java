package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;

import java.math.BigDecimal;


@Entity
@Table(name = "options_ordered_prices", schema = "telco_db")
@ReadOnly
public class OptionsPricesView {

    @Id
    private int order_id;
    @Column
    private BigDecimal sum_of_options;

    public int getOrderId(){return order_id;}
    public BigDecimal getSumOfOptionsPurchased(){return sum_of_options;}
    /* Since this is a view, the data is read-only. No setters. */
}