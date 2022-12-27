package org.ivanservlets.servlets.listener;

import org.ivanservlets.user.dao.UserDao;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@WebListener
public class ContextListener implements ServletContextListener {

    // Since we send requests in multithreading environment, ensure thread-safety
    private AtomicReference<UserDao> dao;

    // Initializing DAO
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        dao = new AtomicReference<>(new UserDao());

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        // Check if we can establish connection to db before the web-app starts
        try {
            // Creating table if not exist and inserting initial users
            UserDao.createTable(Objects.requireNonNull(UserDao.getConnection()));
        } catch (RuntimeException e) {
            servletContext.log(">>> ERROR CONNECTING TO DB: " + e.getMessage(), e);
            throw new RuntimeException("Unable to connect to the DB: " + e.getMessage());
        }

        servletContext.setAttribute("dao", dao);
    }

    // Closing the resource
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        dao = null;
    }
}
