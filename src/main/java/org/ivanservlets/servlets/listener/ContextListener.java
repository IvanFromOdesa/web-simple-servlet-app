package org.ivanservlets.servlets.listener;

import org.ivanservlets.user.UserDao;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
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

        servletContext.setAttribute("dao", dao);
    }

    // Closing the resource
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        dao = null;
    }
}
