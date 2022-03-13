package it.polimi.telco_webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.telco_webapp.auxiliary.exceptions.CredentialsNotValidException;
import it.polimi.telco_webapp.auxiliary.exceptions.InternalDBErrorException;
import it.polimi.telco_webapp.entities.Employee;
import it.polimi.telco_webapp.entities.User;
import it.polimi.telco_webapp.services.EmployeeService;
import it.polimi.telco_webapp.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.json.Json;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.InvalidParameterException;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.db2.entities.services/EmployeeService")
    private EmployeeService employeeService;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
//        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));

        if (!(isEmailValid(email) && isPasswordValid(password))) {
            sendError(request, response, "Invalid Completion", "Invalid email or password format");
            return;
        }
        try {
            User credentialCheckResultUser = userService.checkCredentials(email, password);
            request.getSession().setAttribute("user", credentialCheckResultUser.getEmail());
            String url = "homepage.html";

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Gson gson = new Gson();

            JsonElement jsonElement = new JsonObject();

            jsonElement.getAsJsonObject().addProperty("new_url", url);
            jsonElement.getAsJsonObject().addProperty("name", credentialCheckResultUser.getName());
            jsonElement.getAsJsonObject().addProperty("email", credentialCheckResultUser.getEmail());

            response.getWriter().println(gson.toJson(jsonElement));
        } catch (CredentialsNotValidException e) {
            try {
                Employee employee = employeeService.checkEmployeeCredentials(email, password);
                request.getSession().setAttribute("employee", employee.getId());
                String url = "Employee/index.html";

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(employee);
                jsonElement.getAsJsonObject().addProperty("new_url", url);
                jsonElement.getAsJsonObject().addProperty("employee", "true");
                jsonElement.getAsJsonObject().remove("password");
                jsonElement.getAsJsonObject().remove("id");

                response.getWriter().println(gson.toJson(jsonElement));
            } catch (CredentialsNotValidException f) {
                sendError(request, response, f.getErrorCode(), f.getCause().getMessage());
            } catch (InternalDBErrorException | EJBException f) {
                sendError(request, response, "Internal Error", f.getCause().getMessage());
            }

        } catch (InternalDBErrorException | EJBException e) {
            sendError(request, response, "Internal Error", e.getCause().getMessage());
        }
    }
}
