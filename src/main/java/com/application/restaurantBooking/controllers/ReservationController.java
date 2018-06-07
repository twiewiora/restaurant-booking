package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantBooking.persistence.builder.ReservationBuilder;
import com.application.restaurantBooking.persistence.model.Reservation;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.service.ReservationService;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import com.application.restaurantBooking.persistence.service.RestorerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ReservationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private ReservationService reservationService;

    private RestaurantTableService restaurantTableService;

    private RestorerService restorerService;

    @Autowired
    public ReservationController(JwtTokenUtil jwtTokenUtil,
                                 ReservationService reservationService,
                                 RestaurantTableService restaurantTableService,
                                 RestorerService restorerService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.reservationService = reservationService;
        this.restaurantTableService = restaurantTableService;
        this.restorerService = restorerService;
    }

    @RequestMapping(value = UrlRequests.POST_RESERVATION_ADD,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8")
    public void createReservation(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
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

    @RequestMapping(value = UrlRequests.GET_RESERVATIONS_ONE_TABLE,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getReservationsForTable(@PathVariable String id,
                                          @PathVariable String from,
                                          @PathVariable String to) {
        ObjectMapper objectMapper = new ObjectMapper();
        RestaurantTable restaurantTable = restaurantTableService.getById(Long.parseLong(id));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        try {
            Date dateFrom = sdf.parse(from);
            Date dateTo = sdf.parse(to);
            if (restaurantTable != null) {
                Set<Reservation> reservations = restaurantTable.getReservation();
                Set<Reservation> filteredReservations = reservations.stream()
                        .filter(reservation -> reservation.getReservationDate().after(dateFrom)
                                && reservation.getReservationDate().before(dateTo))
                        .collect(Collectors.toSet());
                return objectMapper.writeValueAsString(filteredReservations);
            } else {
                return UrlRequests.ERROR;
            }
        } catch (JsonProcessingException | ParseException e) {
            e.printStackTrace();
            return UrlRequests.ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.GET_RESERVATIONS_ALL_TABLES,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getReservationsForAllTables(HttpServletRequest request,
                                              @PathVariable String from,
                                              @PathVariable String to) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurant restaurant = getRestorerByJwt(request).getRestaurant();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        try {
            Date dateFrom = sdf.parse(from);
            Date dateTo = sdf.parse(to);
            if (restaurant != null) {
                Set<Reservation> reservations = new HashSet<>();
                restaurant.getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
                Set<Reservation> filteredReservations = reservations.stream()
                        .filter(reservation -> reservation.getReservationDate().after(dateFrom)
                                && reservation.getReservationDate().before(dateTo))
                        .collect(Collectors.toSet());
                return objectMapper.writeValueAsString(filteredReservations);
            } else {
                return UrlRequests.ERROR;
            }
        } catch (JsonProcessingException | ParseException e) {
            e.printStackTrace();
            return UrlRequests.ERROR;
        }
    }

    private Restorer getRestorerByJwt(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return restorerService.getByUsername(username);
    }

}
