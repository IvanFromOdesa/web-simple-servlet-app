package org.ivanservlets.servlets.servlet;

import org.ivanservlets.user.User;
import org.ivanservlets.user.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@WebServlet(urlPatterns = "/users")
public class UserTableServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res) throws ServletException, IOException {
        // Get the DAO Object
        @SuppressWarnings("unchecked")
        final AtomicReference<UserDao> dao =
                (AtomicReference<UserDao>) req.getServletContext().getAttribute("dao");
        // Get all Users
        List<User> usersList = dao.get().getUsers();
        // Add set as attribute to the request
        req.setAttribute("usersList", usersList);
        // Move to the page with users table
        req.getRequestDispatcher("/WEB-INF/view/users.jsp").forward(req, res);
    }
}
