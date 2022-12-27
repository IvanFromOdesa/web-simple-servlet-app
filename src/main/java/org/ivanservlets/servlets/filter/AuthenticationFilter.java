package org.ivanservlets.servlets.filter;

import org.ivanservlets.user.dao.UserDao;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    /**
     * Links that do not require the validation from this filter
     */
    private static final Set<String> allowedLinks = new HashSet<>(Arrays.asList(
            "logout", "styles.css", "app.js"
    ));

    private static ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        // Getting the DAO object from request (added by WebListener)
        @SuppressWarnings("unchecked")
        final AtomicReference<UserDao> dao =
                (AtomicReference<UserDao>) req.getServletContext().getAttribute("dao");

        final HttpSession session = req.getSession();

        // Log the client's IP
        context.log(">>> Requesting from: " + req.getRemoteAddr());

        // If the request is for logout, or it's the .css or .js files, pass it
        String requestURI = req.getRequestURI();
        context.log(">>> Requested URI: " + requestURI);
        if(allowedLinks.contains(requestURI.substring(requestURI.lastIndexOf("/") + 1))) {
            filterChain.doFilter(req, res);
        }
        // Else check for credentials
        else {
            checkCredentials(session, req, res, filterChain, login, password, dao);
        }
    }

    /**
     * Checks for password and login attributes from the current session. If the session
     * contains them, the desired resource is free to use. Otherwise, move to login page,
     * wait for user-input credentials, validate them and if correct, move to the main page.
     * @param session current HttpSession
     * @param req client request
     * @param res server response
     * @param filterChain chain of web filters (to pass the request)
     * @param login client's login
     * @param password client's password
     * @param dao DAO object (fake db)
     */
    private static void checkCredentials(HttpSession session,
                                         HttpServletRequest req,
                                         HttpServletResponse res,
                                         FilterChain filterChain,
                                         String login,
                                         String password,
                                         AtomicReference<UserDao> dao)
            throws ServletException, IOException {
        // If the User is already logged (credentials in session are present)
        if (Objects.nonNull(session.getAttribute("login"))
                && Objects.nonNull(session.getAttribute("password"))) {
            // Immediately move to the desired resource (servlet)
           context.log(">>> Authorized for the requested URI: " + session.getAttribute("login"));
           filterChain.doFilter(req, res);
        }
        else {
            UserDao userDao = dao.get();
            // User is logging in the first time with correct credentials
            if (userDao.isUserPresent(login, password)) {
                context.log(">>> Authenticated: " + login);
                // Setting credentials for every future request from this client
                req.getSession().setAttribute("password", password);
                req.getSession().setAttribute("login", login);
                // Move to main page (LoginServlet)
                filterChain.doFilter(req, res);
            }
            // Credentials are wrong or not present
            else {
                String errorMessage = userDao.getErrorMessage();
                checkFor_401(errorMessage, req, res);
                // Send error message
                req.setAttribute("errorMessage", errorMessage);
                // Stay at the login page
                req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, res);
            }
        }
    }

    /**
     * Checks if the user is trying to access any resource unauthorized. If so,
     * sets status code 401 to response. If user-input credentials are wrong,
     * also does that.
     * @param errorMessage message containing info about incorrect credentials
     * @param req client request
     * @param res server response
     */
    private static void checkFor_401(String errorMessage,
                                     HttpServletRequest req,
                                     HttpServletResponse res) {
        String requestURI = req.getRequestURI();
        if(errorMessage != null || !requestURI
                .substring(requestURI.lastIndexOf("/") + 1)
                .equals("")) {
            // If credentials wrong or user is trying to access any resource unauthorized
            context.log(">>> Unauthorized for the requested URI! Error message: " + errorMessage);
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
    }
}
