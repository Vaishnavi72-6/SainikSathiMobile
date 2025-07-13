package com.example.sainiksathimobile.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;

    // Constructor
    public LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    // Getter
    public String getDisplayName() {
        return displayName;
    }

    // Optional: for debugging/logging
    @Override
    public String toString() {
        return "LoggedInUserView{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
