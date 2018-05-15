package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.persistence.service.RestorerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestorerController {

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

}
