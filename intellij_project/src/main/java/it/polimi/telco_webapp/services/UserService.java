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
     * Inserts a new user into the user table in database.
     *
     * @param user_id: User ID number of the user
     * @param name: Name of the user
     * @param email: Email of the user
     * @param password: Password of the user to log in
     * @param username: Username of the user to log in
     * @param insolvent: If external service rejects user's payment, user is flagged as insolvent
     * @return User: The user that was entered in the DB
     *
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

    /**
     * Retrieve user from user table in database.
     * @param username: Unique username of user.
     * @return User class retrieved from database
     * @throws InvalidParameterException when username is invalid or not found.
     */
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

    /**
     * Checks that the credentials are correct and match the info in the database.
     * @param username: Username entered by the user
     * @param password: Password entered by the user
     * @return user from the database that matches the username and password provided.
     */
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

    /**
     * Checks that the user associated with the provided user_id
     * @param user_id: User id of the user to check insolvency
     * @return True if the user is insolvent and false if the user is not insolvent.
     */
    public boolean isInsolvent(int user_id) {
        List<User> users = em.createNamedQuery("User.checkInsolvency", User.class).setParameter(1, user_id).getResultList();

        return !(users == null || users.isEmpty());

    }
}
