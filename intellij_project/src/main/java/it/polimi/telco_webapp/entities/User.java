package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "user", schema = "telco_db", indexes = {
        @Index(name = "email_UNIQUE", columnList = "email", unique = true),
        @Index(name = "usercol_UNIQUE", columnList = "username", unique = true)
})
@NamedQuery(name = "User.checkCredentials", query = "SELECT c FROM User c WHERE c.email = ?1 AND c.password = ?2")
@NamedQuery(name = "User.getUserByEmail", query = "SELECT c FROM User c WHERE c.email = ?1")
@NamedQuery(name = "User.getUserByUsername", query = "SELECT c FROM User c WHERE c.username = ?1")
//@NamedQuery(name = "User.insolventUsers", query = "SELECT u FROM User u WHERE u.insolvent = 1 ")
//@NamedQuery(name = "User.checkInsolvency", query = "SELECT u FROM User u WHERE u.id = ?1 AND u.insolvent = 1 ")

//@NamedQuery(name = "User.rejectedOrders", query = "SELECT o FROM Order o WHERE ")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Column(name = "username", nullable = false, length = 45)
    private String username;

//    @Column(name = "insolvent", nullable = false, length = 45)
//    private boolean insolvent;

    /* I think we need this.
     * TODO: check the fetch type.
     * TODO: check that mappedby string is correct
     * TODO: check if we need to add targetEntity
     * @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
     * private List<Order> orders;
     *
     */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List <Order> orders;


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* Default: false [user is not insolvent]. True = user is insolvent */
//    public void setInsolvency(boolean insolvent) {this.insolvent = insolvent; }

//    public boolean getInsolvency() {return insolvent;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    /*
    public String toString() {
        return "[" + id + " " + name + " " + email + " " + password + " " + username + "]";
    }
    * */
}