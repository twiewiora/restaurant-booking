package com.application.restaurantbooking.persistence.builder;

import com.application.restaurantbooking.persistence.model.Authority;
import com.application.restaurantbooking.persistence.model.Client;

import java.util.List;

public class ClientBuilder {

    private Client client;

    public ClientBuilder() {
        client = new Client();
    }

    public ClientBuilder username(String username) {
        client.setUsername(username);
        return this;
    }

    public ClientBuilder password(String password) {
        client.setPassword(password);
        return this;
    }

    public ClientBuilder authorities(List<Authority> authorities) {
        client.setAuthorities(authorities);
        return this;
    }

    public Client build() {
        return client;
    }
}
