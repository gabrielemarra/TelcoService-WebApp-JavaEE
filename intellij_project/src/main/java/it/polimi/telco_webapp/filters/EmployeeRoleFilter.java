package it.polimi.telco_webapp.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@jakarta.servlet.annotation.WebFilter(filterName = "UserRoleFilter")
public class EmployeeRoleFilter implements jakarta.servlet.Filter {
    public void destroy() {
    }

    public void doFilter(jakarta.servlet.ServletRequest req, jakarta.servlet.ServletResponse resp, jakarta.servlet.FilterChain chain) throws jakarta.servlet.ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String loginPath = req.getServletContext().getContextPath() + "/index.html";

        HttpSession session = request.getSession();

        //if it's not logged it
        final boolean condition = session.isNew() ||
                session.getAttribute("employee") == null;
        if (condition) {
            session.invalidate();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Error: unauthorized user, please log in as employee.");
            return;
        }

        chain.doFilter(req, resp);
    }

    public void init(jakarta.servlet.FilterConfig config) throws jakarta.servlet.ServletException {

    }

}
