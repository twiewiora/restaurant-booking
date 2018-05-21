package com.application.restaurantBooking.jwt;

import javax.servlet.Registration;

public class RegistrationValidation {

    public RegistrationValidation(){}


    public boolean validate(RegistrationRequest registrationRequest) {
        return validatePassword(registrationRequest.getPassword());
    }

    private boolean validatePassword(String password) {
        return true;
    }

}
