package com.application.restaurantbooking.jwt;

import com.application.restaurantbooking.jwt.jwtModel.JwtAuthenticationRequest;

public class RegistrationValidation {

    public RegistrationValidation(){
        // TODO walidacja hasła przy zakładaniu konta
    }


    public boolean validate(JwtAuthenticationRequest registrationRequest) {
        return validatePassword(registrationRequest.getPassword());
    }

    private boolean validatePassword(String password) {
        return true;
    }

}
