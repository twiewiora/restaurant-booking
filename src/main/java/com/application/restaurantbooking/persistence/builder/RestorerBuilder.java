package com.application.restaurantbooking.persistence.builder;

import com.application.restaurantbooking.persistence.model.Authority;
import com.application.restaurantbooking.persistence.model.Restorer;

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

    public RestorerBuilder authorities(List<Authority> authorities) {
        restorer.setAuthorities(authorities);
        return this;
    }

    public Restorer build() {
        return restorer;
    }
}
