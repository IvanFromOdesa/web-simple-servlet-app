package org.ivanservlets.servlets.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res) throws ServletException, IOException {
        // Removing session
        req.getSession().invalidate();
        // Redirecting to login page (for check from AuthenticationFilter)
        res.sendRedirect(super.getServletContext().getContextPath());
    }
}