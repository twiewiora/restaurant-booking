package com.application.restaurantBooking.jwt;

import java.io.Serializable;

/**
 * Created by stephan on 20.03.16.
 */
public class  RegistrationRequest implements Serializable {

    private String username;
    private String password;

    public RegistrationRequest() {
        super();
    }

    public RegistrationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
