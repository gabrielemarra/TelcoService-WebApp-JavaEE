package it.polimi.telco_webapp.services;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import it.polimi.telco_webapp.entities.User;

import java.security.InvalidParameterException;

@Stateless(name = "UserService")
public class UserService {
    @PersistenceContext(unitName = "telco_webapp")
    private EntityManager em;
    public UserService() {
    }

    /**
     * @param user_id: User ID number of the user
     * @param name: Name of the user
     * @param email: Email of the user
     * @param password: Password of the user to log in
     * @param username: Username of the user to log in
     * @param insolvent: If external service rejects user's payment, user is flagged as insolvent
     * @return User: The user that was entered in the DB
     *
     * @throws
     * @throws
     */
    public User insertUser(int user_id, String name, String email, String password, String username, boolean insolvent) throws PersistenceException, IllegalArgumentException {
        User user = new User();

        user.setId(user_id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.setInsolvency(insolvent);

        em.persist(user);
        /* QUESTION: does this method *need* to return anything? */
        return user;
    }

    public User getUser(String username) throws InvalidParameterException {
        List<User> users = em.createNamedQuery("User.getUser", User.class).setParameter(1, username).getResultList();

        if (users == null || users.isEmpty()) {
            throw new InvalidParameterException("Invalid username.");
        } else if( users.size() == 1) {
            return users.get(0);
        } else {
            throw new InvalidParameterException("DB error.");
        }
    }

    public User checkCredentials(String username, String password) {
        List<User> users = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2, password).getResultList();

        if (users == null || users.isEmpty()) {
            throw new InvalidParameterException("Invalid credentials.");
        } else if( users.size() == 1) {
            return users.get(0);
        } else {
            throw new InvalidParameterException("DB error.");
        }
    }




}
