package org.ivanservlets.user.dao;

import org.ivanservlets.user.model.Role;
import org.ivanservlets.user.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation with PostgreSQL.
 * As the table is created (and users inserted) during compilation
 * (SQL statement) - so that you do not need to create it manually,
 * we use the Statement interface over PS for the queries
 * (there is no "vulnerable" queries where SQL injection can be used).
 * This is just for demonstration purposes.
 */
public class UserDao {

    private String errorMessage;

    /**
     * SQL query to create users table and insert initial users.
     * Workaround for not-yet existing table.
     * @return SQL query to create user table and insert initial users
     */
    private static String getSQLQueryToCreateInsertUsers() {
        return "CREATE TABLE IF NOT EXISTS users" +
                "(" +
                "id_user int NOT NULL," +
                "CONSTRAINT id_pk PRIMARY KEY (id_user)," +
                "full_name varchar(128)," +
                "login varchar(32)," +
                "user_password varchar(128)," +
                "user_role varchar(32)" +
                ");" +
                // Inserting users
                "INSERT INTO users" +
                "(id_user, full_name, login, user_password, user_role) " +
                "VALUES " +
                "(1, 'Dan Smith', 'dan123', 'pass2', 'ADMIN') " +
                "ON CONFLICT DO NOTHING;" + // If the table with inserted users already exists
                "INSERT INTO users" +
                "(id_user, full_name, login, user_password, user_role) " +
                "VALUES " +
                "(2, 'Linda Rozey', 'lindaRr', 'password123', 'USER')" +
                "ON CONFLICT DO NOTHING;" +
                "INSERT INTO users" +
                "(id_user, full_name, login, user_password, user_role) " +
                "VALUES " +
                "(3, 'Samuel Rezhinsky', 'sam78', 'strpass', 'EDITOR')" +
                "ON CONFLICT DO NOTHING;"
                ;
    }

    /**
     * SQL query to fetch all users from db.
     * @return SQL query to fetch all users from db
     */
    private static String getSQLQuerySelectAllUsers() {
        return "SELECT * FROM users";
    }

    /**
     * Tests if the connection to db can be established, and creates users table.
     * @return established connection with db
     */
    public static Connection getConnection() {

        Connection connection;
        String url = "jdbc:postgresql://localhost:5432/banking"; // URL to db
        String user = "postgres"; // Your db login goes here
        String password = "postgres"; // Your password

        try {
            // We're using PostgreSQL
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable connect to DB: no driver found");
        } catch (SQLException e) {
            throw new RuntimeException("DB denied access or it is down!");
        }
        return connection;
    }

    /**
     * Creates users table (DDL query) and inserts initial users into it.
     * @param connection established connection with db
     */
    public static void createTable(Connection connection) {
        // No need for PS as there is nothing to input from client
        try {
            // Creating users table
            Statement statement = connection.createStatement();
            statement.execute(getSQLQueryToCreateInsertUsers());
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create DB table!" + e.getErrorCode());
        } finally {
            if(connection != null) {
                // Closing the connection opened in getConnection()
                try {
                    connection.close();
                } catch (SQLException ignored) {

                }
            }
        }
    }

    public List<User> getUsers() {

        List<User> userList = new ArrayList<>();
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getSQLQuerySelectAllUsers());

            while (resultSet.next()) {
                User user = new User();
                // Fetching only required fields to display
                // (password to check in isUserPresent())
                user.setName(resultSet.getString(2));
                user.setLogin(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                user.setRole(Role.valueOf(resultSet.getString(5)));

                userList.add(user);
            }

            connection.close();

        } catch (SQLException ignored) {
           // Connection is already established - db is accessible
        }
        return userList;
    }

    public boolean isUserPresent(String login, String password) {
        // Nothing was submitted yet
        if(login == null && password == null) {
            errorMessage = null;
            return false;
        }
        // Get users from db
        List<User> users = getUsers();
        for (User user : users) {
            // Check for login
            if(user.getLogin().equals(login)) {
                // Login and password are correct
                if(user.getPassword().equals(password)) {
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

    /**
     * Returns errorMessage for login form. Either, null
     * (if credentials are valid or not present yet), "Incorrect password!"
     * or "Incorrect login!" to indicate where the validation failed.
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
