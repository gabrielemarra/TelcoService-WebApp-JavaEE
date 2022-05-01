package it.polimi.telco_webapp.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "employee", schema = "telco_db", indexes = {
        @Index(name = "email_UNIQUE", columnList = "email", unique = true)
})
@NamedQuery(name = "Employee.checkCredentials", query = "SELECT e FROM Employee e WHERE e.email = ?1 and e.password = ?2 and e.authorized = 1")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Column(name = "authorized", nullable = false)
    private Integer authorized;

    public Integer getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Integer authorized) {
        this.authorized = authorized;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}