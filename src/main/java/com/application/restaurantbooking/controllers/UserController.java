package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.jwt.jwttoken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.persistence.service.RestorerService;
import com.application.restaurantbooking.persistence.service.UserServiceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private ClientService clientService;

    private RestorerService restorerService;

    private UserServiceManager userServiceManager;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil,
                          ClientService clientService,
                          RestorerService restorerService,
                          UserServiceManager userServiceManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientService = clientService;
        this.restorerService = restorerService;
        this.userServiceManager = userServiceManager;
    }

    @GetMapping(value = UrlRequests.VALIDATE_RESTORER, produces = "application/json; charset=UTF-8")
    public String validateRestorer(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader(tokenHeader) != null) {
            String token = request.getHeader(tokenHeader).substring(7);
            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                if (restorerService.getByUsername(username) != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return AcceptResponses.CORRECT_VALIDATION;
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return ErrorResponses.UNAUTHORIZED_ACCESS;
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return ErrorResponses.JWT_TOKEN_EXPIRED;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ErrorResponses.UNAUTHORIZED_ACCESS;
    }

    @GetMapping(value = UrlRequests.VALIDATE_CLIENT, produces = "application/json; charset=UTF-8")
    public String validateClient(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader(tokenHeader) != null) {
            String token = request.getHeader(tokenHeader).substring(7);
            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                if (clientService.getByUsername(username) != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return AcceptResponses.CORRECT_VALIDATION;
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return ErrorResponses.UNAUTHORIZED_ACCESS;
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return ErrorResponses.JWT_TOKEN_EXPIRED;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ErrorResponses.UNAUTHORIZED_ACCESS;
    }

    @GetMapping(value = UrlRequests.GET_CLIENT_BY_ID, produces = "application/json; charset=UTF-8")
    public String getClientById(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable String id) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Client client = clientService.getById(Long.decode(id));
        try {
            if (client != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                response.setStatus(HttpServletResponse.SC_OK);
                return objectMapper.writeValueAsString(client);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.CLIENT_NOT_FOUND;
            }
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

}
