package it.polimi.telco_webapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.ReadOnly;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "orders_and_costs", schema = "telco_db")
@ReadOnly
/* This view shows the total (not monthly) amount that options add to the monthly cost of one order.
 * (IE, this amount + sales_base = sales_total)
 * */
public class OrdersAndCostsView {

    @Id
    private int order_id;
    @Column
    private String status;
    @Column
    private LocalDate subscription_start;
    @Column
    private LocalDateTime timestamp;
    @Column
    private int user_id;
    @Column
    private int package_id;
    @Column
    private int chosen_validity_period;
    @Column
    private BigDecimal baseCost;
    @Column
    private BigDecimal optionsCost;
    @Column
    private BigDecimal total;


    public int getOrderId() {return order_id;}
    public String getStatus() {return status;}
    public LocalDate getSubscriptionStart() {return subscription_start;}
    public LocalDateTime getTimestamp() {return timestamp;}
    public int getUserId() {return user_id;}
    public int getPackageId() {return package_id;}
    public int getValidity() {return chosen_validity_period;}
    public BigDecimal getBaseCost() {return baseCost;}
    public BigDecimal getOptionsCost() {return optionsCost;}
    public BigDecimal getTotalCost() {return total;}




    /* Since this is a view, the data is read-only. No setters. */
}