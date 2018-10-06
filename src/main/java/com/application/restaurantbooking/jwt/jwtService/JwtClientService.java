package com.application.restaurantbooking.jwt.jwtService;

import com.application.restaurantbooking.jwt.jwtFactory.JwtUserFactory;
import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtClientService implements UserDetailsService {

    private ClientService clientService;

    @Autowired
    public JwtClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientService.getByUsername(username);

        if (client != null) {
            return JwtUserFactory.create(client);
        }
        return null;
    }
}
