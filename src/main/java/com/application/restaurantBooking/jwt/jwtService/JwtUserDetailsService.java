package com.application.restaurantBooking.jwt.jwtService;

import com.application.restaurantBooking.jwt.jwtFactory.JwtRestorerFactory;
import com.application.restaurantBooking.persistence.model.Restorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.application.restaurantBooking.persistence.repository.RestorerRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private RestorerRepository restorerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Restorer restorer = restorerRepository.findByUsername(username);

        if (restorer == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtRestorerFactory.create(restorer);
        }
    }
}
