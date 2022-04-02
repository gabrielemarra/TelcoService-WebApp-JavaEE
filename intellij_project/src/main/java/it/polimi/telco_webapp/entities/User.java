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

    // This is set/updated/changed completely by a trigger in the DB
    @Column(name = "insolvent")
    private boolean insolvent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List <Order> orders;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}