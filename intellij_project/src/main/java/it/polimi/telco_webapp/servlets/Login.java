package it.polimi.telco_webapp.servlets;

import it.polimi.telco_webapp.entities.Employee;
import it.polimi.telco_webapp.entities.User;
import it.polimi.telco_webapp.services.EmployeeService;
import it.polimi.telco_webapp.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
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
     * Method to handle errors, redirects to an error page
     *
     * @param request   request
     * @param response  response
     * @param errorType type of error
     * @param errorInfo information about the error
     * @throws IOException if there are problems redirecting
     */
    protected void sendError(HttpServletResponse response, HttpServletRequest request, String errorType, String errorInfo) throws IOException {
        request.getSession().setAttribute("errorType", errorType);
        request.getSession().setAttribute("errorInfo", errorInfo);
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/error.html").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
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
            sendError(response, request, "Invalid Completion", "invalid email or password format");
            return;
        }
        try {
            User credentialCheckResultUser = userService.checkCredentials(email, password);
            request.getSession().setAttribute("user", credentialCheckResultUser.getEmail());

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(email);
        } catch (InvalidParameterException | EJBException e) {
            try {
                Employee employee = employeeService.checkEmployeeCredentials(email, password);
                request.getSession().setAttribute("employee", employee.getId());
                String path = getServletContext().getContextPath() + "/Employee/index.html";
                response.sendRedirect(path);
            } catch (InvalidParameterException | EJBException f) {
                sendError(response, request, "Invalid Completion", f.getCause().getMessage());
            }
        }
    }
}
