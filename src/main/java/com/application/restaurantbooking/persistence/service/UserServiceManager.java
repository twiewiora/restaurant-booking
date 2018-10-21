package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.jwt.jwtservice.JwtClientService;
import com.application.restaurantbooking.jwt.jwtservice.JwtRestorerService;
import com.application.restaurantbooking.jwt.jwttoken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Restorer;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserServiceManager {

    @Value("${jwt.header}")
    private String tokenHeader;

    private ClientService clientService;

    private RestorerService restorerService;

    private JwtClientService jwtClientService;

    private JwtRestorerService jwtRestorerService;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserServiceManager(ClientService clientService,
                              RestorerService restorerService,
                              JwtClientService jwtClientService,
                              JwtRestorerService jwtRestorerService,
                              JwtTokenUtil jwtTokenUtil) {
        this.clientService = clientService;
        this.restorerService = restorerService;
        this.jwtClientService = jwtClientService;
        this.jwtRestorerService = jwtRestorerService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public RestorerService getRestorerService() {
        return restorerService;
    }

    public JwtClientService getJwtClientService() {
        return jwtClientService;
    }

    public JwtRestorerService getJwtRestorerService() {
        return jwtRestorerService;
    }

    public Restorer getRestorerByJwt(HttpServletRequest request) {
        try {
            if (request.getHeader(tokenHeader) != null) {
                String token = request.getHeader(tokenHeader).substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                return restorerService.getByUsername(username);
            }
        } catch (ExpiredJwtException e) {
            return null;
        }
        return null;
    }

    public Client getClientByJwt(HttpServletRequest request) {
        try {
            if (request.getHeader(tokenHeader) != null) {
                String token = request.getHeader(tokenHeader).substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                return clientService.getByUsername(username);
            }
        } catch (ExpiredJwtException e) {
            return null;
        }
        return null;
    }
}
