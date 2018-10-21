package com.application.restaurantbooking.jwt.jwtservice;

import com.application.restaurantbooking.jwt.jwtfactory.JwtUserFactory;
import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.service.RestorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtRestorerService implements UserDetailsService {

    private RestorerService restorerService;

    @Autowired
    public JwtRestorerService(RestorerService restorerService) {
        this.restorerService = restorerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Restorer restorer = restorerService.getByUsername(username);

        if (restorer != null) {
            return JwtUserFactory.create(restorer);
        }
        return null;
    }
}
