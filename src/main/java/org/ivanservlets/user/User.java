package org.ivanservlets.user;

// Convenient way to represent User as we don't need setters (to reset attributes)
public record User(String name, String login, String password) {
}
