package it.polimi.telco_webapp.services;

import java.util.List;

import it.polimi.telco_webapp.auxiliary.exceptions.CredentialsNotValidException;
import it.polimi.telco_webapp.auxiliary.exceptions.InternalDBErrorException;
import it.polimi.telco_webapp.auxiliary.exceptions.UserNotFoundException;
import it.polimi.telco_webapp.entities.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import it.polimi.telco_webapp.entities.User;

import javax.persistence.criteria.CriteriaBuilder;
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
     * @param name: Name of the user
     * @param email: Email of the user
     * @param password: Password of the user to log in
     * @param username: Username of the user to log in
     * @return User: The user that was entered in the DB
     *
     */
    public User insertUser(String name, String email, String password, String username) throws PersistenceException, IllegalArgumentException {
        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
//        user.setInsolvency(insolvent);

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
    public User getUserByUsername(String username) throws InvalidParameterException {
        List<User> users = em.createNamedQuery("User.getUserByUsername", User.class).setParameter(1, username).getResultList();

        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("InvalidUsername","Invalid username.");
        } else if( users.size() == 1) {
            return users.get(0);
        } else {
            throw new InvalidParameterException("DB error.");
        }
    }

    /**
     * Retrieve user from user table in database.
     * @param email: Unique email of user.
     * @return User class retrieved from database
     * @throws InvalidParameterException when username is invalid or not found.
     */
    public User getUserByEmail(String email) throws InvalidParameterException {
        List<User> users = em.createNamedQuery("User.getUserByEmail", User.class).setParameter(1, email).getResultList();

        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("InvalidEmail","Invalid email.");
        } else if( users.size() == 1) {
            return users.get(0);
        } else {
            throw new InternalDBErrorException("InternalDBError","Too many entries for the credentials");
        }
    }

    /**
     * Checks that the credentials are correct and match the info in the database.
     * @param email: Username entered by the user
     * @param password: Password entered by the user
     * @return user from the database that matches the username and password provided.
     */
    public User checkCredentials(String email, String password) {
        List<User> users = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, email).setParameter(2, password).getResultList();

        if (users == null || users.isEmpty()) {
            throw new CredentialsNotValidException("InvalidCredentials","The credentials do not correspond to any user.", false);
        } else if( users.size() == 1) {
            return users.get(0);
        } else {
            throw new InternalDBErrorException("InternalDBError","Too many entries for the credentials");
        }
    }

    /**
     * Checks if the user associated with the provided user_id is insolvent or not
     * TODO: Should this be in OrderService.java instead of here?
     * @param user_id: User id of the user to check insolvency
     * @return True if the user is insolvent and false if the user is not insolvent.
     */
    public boolean isInsolvent(int user_id) {
        List<Order> rejectedOrders = em.createNamedQuery("Order.getRejectedOrdersBySingleUser").setParameter(1, user_id).getResultList();
        return !(rejectedOrders.isEmpty() || rejectedOrders == null);
    }

    /**
     * Checks if the user associated with the provided user_id is insolvent or not
     * TODO: Should this be in OrderService.java instead of here?
     * @param user_id: User id of the user to check insolvency
     * @return True if the user is insolvent and false if the user is not insolvent.
     */
    public List<Order> getRejectedOrders(int user_id) {
        List<Order> rejectedOrders = em.createNamedQuery("Order.getRejectedOrdersBySingleUser").setParameter(1, user_id).getResultList();
        return rejectedOrders;
    }


}
