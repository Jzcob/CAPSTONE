package me.jacob.capstone.Utils;

import me.jacob.capstone.Model.Employee;

public class UserSession {
    private static UserSession instance;
    private Employee currentUser;

    private UserSession(Employee user) {
        this.currentUser = user;
    }

    public static void setSession(Employee user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public Employee getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
        instance = null;
    }
}
