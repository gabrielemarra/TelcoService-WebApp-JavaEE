package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.telco_webapp.entities.Employee;
import it.polimi.telco_webapp.entities.User;
import it.polimi.telco_webapp.services.EmployeeService;
import it.polimi.telco_webapp.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.InvalidParameterException;

@WebServlet(name = "Signup", value = "/Signup")
public class Signup extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;


    /**
     * Method to handle errors, send json with error info
     *
     * @param request   request
     * @param response  response
     * @param errorType type of error
     * @param errorInfo information about the error
     * @throws IOException if there are problems redirecting
     */
    protected void sendError(HttpServletRequest request, HttpServletResponse response, String errorType, String errorInfo) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("errorType", errorType);
        jsonObject.addProperty("errorInfo", errorInfo);

        response.getWriter().println(jsonObject);
    }

    /**
     * Method to check email validity
     *
     * @param email email to check
     * @return true if it's valid, false otherwise
     */
    boolean isEmailValid(String email) {
        return email != null && EmailValidator.getInstance().isValid(email);
    }

//    /**
//     * Method to check username validity
//     *
//     * @param username username to check
//     * @return true if it's valid, false otherwise
//     */
//    boolean isUsernameValid(String username) {
//        return username != null && username.length() < 32 && username.length() > 3;
//    }

    /**
     * Method to check password validity
     *
     * @param password password to check
     * @return true if it's valid, false otherwise
     */
    boolean isPasswordValid(String password) {
        return password != null && password.length() < 32 && password.length() > 3;
    }

    /**
     * Method to check username validity
     *
     * @param username username to check
     * @return true if it's valid, false otherwise
     */
    boolean isUsernameValid(String username) {
        return username != null && username.length() < 32 && username.length() > 3;
    }

    /**
     * Method to check name validity
     *
     * @param name username to check
     * @return true if it's valid, false otherwise
     */
    boolean isNameValid(String name) {
        return name != null && name.length() < 32 && name.length() > 1;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String name = StringEscapeUtils.escapeJava(request.getParameter("name"));

        if (!(isEmailValid(email) && isPasswordValid(password) && isUsernameValid(username))) {
            sendError(request, response, "Invalid Completion", "Invalid email, username or password format");
            return;
        }
        try {
            //check if email already exist
            User userFromDB = userService.getUserByEmail(email);

            //User found! This email is already used
            sendError(request, response, "Invalid email", "The email is already associated tu an user");
        } catch (InvalidParameterException | EJBException e) {
            try {
                //check if email already exist
                User userFromDB = userService.getUserByUsername(username);

                //User found! This email is already used
                sendError(request, response, "Invalid username", "The username is already associated tu an user");
            } catch (InvalidParameterException | EJBException f) {
                //No user found. We can create a new user!
                User newUser = userService.insertNewUser(name, email, password, username);

                request.getSession().setAttribute("user", newUser.getEmail());
                String url = "homepage.html";

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(newUser);
                jsonElement.getAsJsonObject().addProperty("employee", "false");
                jsonElement.getAsJsonObject().addProperty("message", "Account created, now please login.");
                jsonElement.getAsJsonObject().remove("password");
                jsonElement.getAsJsonObject().remove("id");

                response.getWriter().println(gson.toJson(jsonElement));
            }

        }
    }
}
