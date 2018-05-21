package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.jwt.jwtModel.JwtRestorer;
import com.application.restaurantBooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantBooking.persistence.service.RestorerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RestorerController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    private RestorerService restorerService;

    @Autowired
    public RestorerController(RestorerService restorerService) {
        this.restorerService = restorerService;
    }

    @RequestMapping(value = UrlRequests.GET_RESTORER_BY_ID,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getTaskById(@PathVariable String id){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(restorerService.getById(Long.decode(id)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }



    @RequestMapping(value = "restorer", method = RequestMethod.GET)
    public JwtRestorer getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtRestorer restorer = (JwtRestorer) userDetailsService.loadUserByUsername(username);
        return restorer;}



}
