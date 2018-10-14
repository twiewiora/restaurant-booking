package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.builder.ReservationBuilder;
import com.application.restaurantbooking.persistence.model.*;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.persistence.service.ReservationService;
import com.application.restaurantbooking.persistence.service.RestaurantService;
import com.application.restaurantbooking.persistence.service.RestorerService;
import com.application.restaurantbooking.utils.TableSearcher;
import com.application.restaurantbooking.utils.TableSearcherRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class ReservationController {

    private static final Logger LOGGER = Logger.getLogger(ReservationController.class.getName());

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private RestorerService restorerService;

    private ClientService clientService;

    private ReservationService reservationService;

    private RestaurantService restaurantService;

    private TableSearcher tableSearcher;

    private static final String dateTimeFormat = "yyyy-MM-dd_HH:mm";

    @Autowired
    public ReservationController(JwtTokenUtil jwtTokenUtil,
                                 RestorerService restorerService,
                                 ClientService clientService,
                                 ReservationService reservationService,
                                 RestaurantService restaurantService,
                                 TableSearcher tableSearcher) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.restorerService = restorerService;
        this.clientService = clientService;
        this.reservationService = reservationService;
        this.restaurantService = restaurantService;
        this.tableSearcher = tableSearcher;
    }

    @RequestMapping(value = UrlRequests.POST_RESERVATION_ADD,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String createReservation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody String json) {
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            RestaurantTable restaurantTable = restorer.getRestaurant().getRestaurantTables().stream()
                    .filter(table -> table.getId().equals(jsonNode.get("tableId").asLong()))
                    .findFirst()
                    .orElse(null);
            if (restaurantTable != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                Reservation reservation = new ReservationBuilder()
                        .restaurantTable(restaurantTable)
                        .reservationDate(sdf.parse(jsonNode.get("date").asText()))
                        .reservationLength(jsonNode.get("length").asInt())
                        .reservedPlaces(jsonNode.get("places").asInt())
                        .comment(jsonNode.get("comment").asText())
                        .build();

                reservationService.createReservation(reservation);
                response.setStatus(HttpServletResponse.SC_CREATED);
                return AcceptResponses.RESERVATION_CREATED;
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_TABLE_NOT_FOUND;
            }
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.POST_RESERVATION_CANCEL,
            method = RequestMethod.POST,
            produces = "application/json; charset=UTF-8")
    public String cancelReservation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id) {
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Set<Reservation> reservations = new HashSet<>();
        restorer.getRestaurant().getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
        Reservation reservation = reservations.stream()
                .filter(res -> res.getId().equals(Long.decode(id)))
                .findFirst()
                .orElse(null);

        if (reservation != null) {
            reservationService.cancelReservation(reservation);
            response.setStatus(HttpServletResponse.SC_OK);
            return AcceptResponses.RESERVATION_CANCELLED;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorResponses.RESERVATION_NOT_FOUND;
        }
    }

    @RequestMapping(value = UrlRequests.DELETE_RESERVATION,
            method = RequestMethod.DELETE,
            produces = "application/json; charset=UTF-8")
    public String deleteReservation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id){
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Set<Reservation> reservations = new HashSet<>();
        restorer.getRestaurant().getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
        Reservation reservation = reservations.stream()
                .filter(res -> res.getId().equals(Long.decode(id)))
                .findFirst()
                .orElse(null);

        if (reservation != null) {
            reservationService.deleteReservation(Long.decode(id));
            response.setStatus(HttpServletResponse.SC_OK);
            return AcceptResponses.RESERVATION_DELETED;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorResponses.RESERVATION_NOT_FOUND;
        }
    }

    @RequestMapping(value = UrlRequests.GET_RESERVATIONS_ONE_TABLE,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getReservationsForTable(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @PathVariable String id,
                                          @PathVariable String from,
                                          @PathVariable String to) {
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        RestaurantTable restaurantTable = restorer.getRestaurant().getRestaurantTables().stream()
                .filter(table -> table.getId().equals(Long.parseLong(id)))
                .findFirst()
                .orElse(null);

        try {
            if (restaurantTable != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                Date dateFrom = sdf.parse(from);
                Date dateTo = sdf.parse(to);
                Set<Reservation> reservations = restaurantTable.getReservation();
                Set<Reservation> filteredReservations = reservations.stream()
                        .filter(reservation -> reservation.getReservationDate().after(dateFrom)
                                && reservation.getReservationDate().before(dateTo))
                        .collect(Collectors.toSet());
                response.setStatus(HttpServletResponse.SC_OK);
                return objectMapper.writeValueAsString(filteredReservations);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_TABLE_NOT_FOUND;
            }
        } catch (JsonProcessingException | ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.GET_RESERVATIONS_ALL_TABLES,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getReservationsForAllTables(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @PathVariable String from,
                                              @PathVariable String to) {
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        try {
            Restaurant restaurant = restorer.getRestaurant();
            if (restaurant != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                Date dateFrom = sdf.parse(from);
                Date dateTo = sdf.parse(to);
                Set<Reservation> reservations = new HashSet<>();
                restaurant.getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
                Set<Reservation> filteredReservations = reservations.stream()
                        .filter(reservation -> reservation.getReservationDate().after(dateFrom)
                                && reservation.getReservationDate().before(dateTo))
                        .collect(Collectors.toSet());
                response.setStatus(HttpServletResponse.SC_OK);
                return objectMapper.writeValueAsString(filteredReservations);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (JsonProcessingException | ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.POST_RESERVATION_ADD_BY_CLIENT,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String createReservationByClient(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody String json,
                                            @PathVariable String id) {
        Client client = getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restaurantService.getById(Long.decode(id));
        if (restaurant == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorResponses.RESTAURANT_NOT_FOUND;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
            TableSearcherRequest tableSearcherRequest = new TableSearcherRequest(sdf.parse(jsonNode.get("date").asText()),
                    jsonNode.get("length").asInt(), jsonNode.get("places").asInt());
            List<RestaurantTable> restaurantTables = tableSearcher.searchTableByRequest(restaurant, tableSearcherRequest);
            if (!restaurantTables.isEmpty()) {
                for (RestaurantTable restaurantTable : restaurantTables) {
                    Reservation reservation = new ReservationBuilder()
                            .client(client)
                            .restaurantTable(restaurantTable)
                            .reservationDate(sdf.parse(jsonNode.get("date").asText()))
                            .reservationLength(jsonNode.get("length").asInt())
                            .reservedPlaces(jsonNode.get("places").asInt())
                            .comment(jsonNode.get("comment").asText())
                            .build();
                    reservationService.createReservation(reservation);
                }
                response.setStatus(HttpServletResponse.SC_CREATED);
                return AcceptResponses.RESERVATION_CREATED;
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_TABLE_RESERVED;
            }
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.GET_RESERVATIONS_LIST,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getReservationsForClient(HttpServletRequest request,
                                            HttpServletResponse response) {
        Client client = getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode restaurantArray = objectMapper.createArrayNode();
            for (Reservation reservation : client.getReservations()) {
                restaurantArray.add(objectMapper.writeValueAsString(reservation));
            }
            return objectMapper.createObjectNode().putPOJO("reservations", restaurantArray).toString();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.DELETE_RESERVATION_BY_CLIENT,
            method = RequestMethod.DELETE,
            produces = "application/json; charset=UTF-8")
    public String deleteReservationByClient(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id){
        Client client = getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Reservation reservation = reservationService.getById(Long.decode(id));
        if (reservation != null) {
            reservationService.deleteReservation(Long.decode(id));
            response.setStatus(HttpServletResponse.SC_OK);
            return AcceptResponses.RESERVATION_DELETED;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorResponses.RESERVATION_NOT_FOUND;
        }
    }

    private Restorer getRestorerByJwt(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return restorerService.getByUsername(username);
    }

    private Client getClientByJwt(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return clientService.getByUsername(username);
    }

}
