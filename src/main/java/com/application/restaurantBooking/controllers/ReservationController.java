package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.persistence.builder.ReservationBuilder;
import com.application.restaurantBooking.persistence.model.Reservation;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.service.ReservationService;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
public class ReservationController {

    private ReservationService reservationService;

    private RestaurantTableService restaurantTableService;

    @Autowired
    public ReservationController(ReservationService reservationService,
                                 RestaurantTableService restaurantTableService) {
        this.reservationService = reservationService;
        this.restaurantTableService = restaurantTableService;
    }

    @RequestMapping(value = UrlRequests.POST_RESERVATION_ADD,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8")
    public void createReservation(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            RestaurantTable restaurantTable = restaurantTableService
                    .getById(jsonNode.get("tableId").asLong());
            Reservation reservation;
            if (restaurantTable != null) {
                reservation = new ReservationBuilder()
                        .restaurantTable(restaurantTable)
                        .reservationDate(sdf.parse(jsonNode.get("date").asText()))
                        .reservationLength(jsonNode.get("length").asInt())
                        .reservedPlaces(jsonNode.get("places").asInt())
                        .comment(jsonNode.get("comment").asText())
                        .cancelled(false)
                        .build();

                reservationService.createReservation(reservation);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = UrlRequests.DELETE_RESERVATION,
            method = RequestMethod.DELETE)
    public void deleteReservation(@PathVariable String id){
        Reservation reservation = reservationService.getById(Long.decode(id));
        if (reservation != null) {
            reservationService.deleteReservation(Long.decode(id));
        }
    }

}
