package org.ivanservlets.servlets.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/main")
public class LoginServlet extends HttpServlet {

    // Post method from login form
    @Override
    protected void doPost(HttpServletRequest req,
                         HttpServletResponse res) throws ServletException, IOException {
        // Move to main page on login
        req.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(req, res);
    }

    // Back to Main Menu
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(req, res);
    }
}
