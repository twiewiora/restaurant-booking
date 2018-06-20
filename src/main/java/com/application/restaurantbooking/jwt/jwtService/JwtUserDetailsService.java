package com.application.restaurantbooking.jwt.jwtService;

import com.application.restaurantbooking.jwt.jwtFactory.JwtRestorerFactory;
import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.service.RestorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private RestorerService restorerService;

    @Autowired
    public JwtUserDetailsService(RestorerService restorerService) {
        this.restorerService = restorerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Restorer restorer = restorerService.getByUsername(username);

        if (restorer == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtRestorerFactory.create(restorer);
        }
    }
}
