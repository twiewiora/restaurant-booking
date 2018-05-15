package com.application.restaurantBooking.persistence.builder;

import com.application.restaurantBooking.persistence.model.Client;

public class ClientBuilder {

    private Client client;

    public ClientBuilder() {
        client = new Client();
    }

    public ClientBuilder login(String login) {
        client.setLogin(login);
        return this;
    }

    public ClientBuilder password(String password) {
        client.setPassword(password);
        return this;
    }

    public ClientBuilder phoneBumber(String phoneNumber) {
        client.setPhoneNumber(phoneNumber);
        return this;
    }

    public ClientBuilder email(String email) {
        client.setEmail(email);
        return this;
    }

    public Client build() {
        return client;
    }
}
