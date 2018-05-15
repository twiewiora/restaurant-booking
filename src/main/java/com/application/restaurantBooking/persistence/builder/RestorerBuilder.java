package com.application.restaurantBooking.persistence.builder;

import com.application.restaurantBooking.persistence.model.Restorer;

public class RestorerBuilder {

    private Restorer restorer;

    public RestorerBuilder() {
        restorer = new Restorer();
    }

    public RestorerBuilder login(String login) {
        restorer.setLogin(login);
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

    public Restorer build() {
        return restorer;
    }
}
