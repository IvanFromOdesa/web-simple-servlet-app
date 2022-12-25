package org.ivanservlets.user;

import java.util.List;

/**
 * No db source required. We use in-memory HashSet to store all present users.
 */
public class UserDao {

    private static final List<User> users = List.of(
            new User("Dan Smith", "dan123", "pass2"),
            new User("Linda Rozey", "lindaRr", "password123"),
            new User("Samuel Rezhinsky", "sam78", "strpass")
    );

    private String errorMessage;

    public List<User> getUsers() {
        return users;
    }

    public boolean isUserPresent(String login, String password) {
        // Nothing was submitted yet
        if(login == null && password == null) {
            errorMessage = null;
            return false;
        }
        for (User user : users) {
            // Check for login
            if(user.login().equals(login)) {
                // Login and password are correct
                if(user.password().equals(password)) {
                    return true;
                }
                // Incorrect password
                errorMessage = "Incorrect password!";
                return false;
            }
        }
        // Login not found
        errorMessage = "Incorrect login!";
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
