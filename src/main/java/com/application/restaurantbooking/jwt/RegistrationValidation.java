package com.application.restaurantbooking.jwt;

import com.application.restaurantbooking.jwt.jwtmodel.JwtAuthenticationRequest;

public class RegistrationValidation {

    public RegistrationValidation(){
    }


    public boolean validate(JwtAuthenticationRequest registrationRequest) {
        return validatePassword(registrationRequest.getPassword());
    }

    private boolean validatePassword(String password) {
        return !password.isEmpty();
    }

}
