package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.persistence.service.RestorerService;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private ClientService clientService;

    private RestorerService restorerService;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil,
                          ClientService clientService,
                          RestorerService restorerService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientService = clientService;
        this.restorerService = restorerService;
    }

    @RequestMapping(value = UrlRequests.VALIDATE_RESTORER, method = RequestMethod.GET)
    public String validateRestorer(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader(tokenHeader) != null) {
            String token = request.getHeader(tokenHeader).substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (restorerService.getByUsername(username) != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                return AcceptResponses.CORRECT_VALIDATION;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return ErrorResponses.UNAUTHORIZED_ACCESS;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ErrorResponses.UNAUTHORIZED_ACCESS;
    }

    @RequestMapping(value = UrlRequests.VALIDATE_CLIENT, method = RequestMethod.GET)
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
            } catch (SignatureException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return ErrorResponses.UNAUTHORIZED_ACCESS;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ErrorResponses.UNAUTHORIZED_ACCESS;
    }
}
