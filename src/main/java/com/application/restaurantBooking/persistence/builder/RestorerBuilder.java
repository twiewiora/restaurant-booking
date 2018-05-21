package com.application.restaurantBooking.persistence.builder;

import com.application.restaurantBooking.persistence.model.Authority;
import com.application.restaurantBooking.persistence.model.Restorer;

import java.util.List;

public class RestorerBuilder {

    private Restorer restorer;

    public RestorerBuilder() {
        restorer = new Restorer();
    }

    public RestorerBuilder username(String username) {
        restorer.setUsername(username);
        return this;
    }

    public RestorerBuilder password(String password) {
        restorer.setPassword(password);
        return this;
    }

    public RestorerBuilder phoneNumber(String phoneNumber) {
        restorer.setPhoneNumber(phoneNumber);
        return this;
    }

    public RestorerBuilder email(String email) {
        restorer.setEmail(email);
        return this;
    }

    public RestorerBuilder enabled(Boolean enabled) {
        restorer.setEnabled(enabled);
        return this;
    }

    public RestorerBuilder authorities(List<Authority> authorities) {
        restorer.setAuthorities(authorities);
        return this;
    }

    public Restorer build() {
        return restorer;
    }
}
