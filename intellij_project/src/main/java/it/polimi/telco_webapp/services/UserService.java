package it.polimi.telco_webapp.services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

@Stateless(name = "UserService")
public class UserService {
    public UserService() {
    }
}
