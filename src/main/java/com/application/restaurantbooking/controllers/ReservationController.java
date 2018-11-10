package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.controllers.dto.ReservationDTO;
import com.application.restaurantbooking.controllers.dto.ReservationsDTO;
import com.application.restaurantbooking.persistence.model.*;
import com.application.restaurantbooking.persistence.service.ReservationService;
import com.application.restaurantbooking.persistence.service.RestaurantService;
import com.application.restaurantbooking.persistence.service.UserServiceManager;
import com.application.restaurantbooking.utils.TableSearcher;
import com.application.restaurantbooking.utils.TableSearcherRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class ReservationController {

    private static final Logger LOGGER = Logger.getLogger(ReservationController.class.getName());

    private UserServiceManager userServiceManager;

    private ReservationService reservationService;

    private RestaurantService restaurantService;

    private TableSearcher tableSearcher;

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd_HH:mm";

    private ModelMapper modelMapper;

    @Autowired
    public ReservationController(UserServiceManager userServiceManager,
                                 ReservationService reservationService,
                                 RestaurantService restaurantService,
                                 TableSearcher tableSearcher) {
        this.userServiceManager = userServiceManager;
        this.reservationService = reservationService;
        this.restaurantService = restaurantService;
        this.tableSearcher = tableSearcher;
        this.modelMapper = new ModelMapper();
    }

    @ApiOperation(value = "Create reservation", response = ReservationDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation created", response = ReservationDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_RESERVATION_ADD,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public ReservationDTO createReservation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody ReservationDTO reservationDTO) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Optional<RestaurantTable> restaurantTable = restorer.getRestaurant().getRestaurantTables().stream()
                .filter(table -> table.getId().equals(reservationDTO.getRestaurantTableId()))
                .findFirst();
        if (restaurantTable.isPresent()) {
            Reservation reservation = modelMapper.map(reservationDTO, Reservation.class);
            Reservation createdReservation = reservationService.createReservation(reservation);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return modelMapper.map(createdReservation, ReservationDTO.class);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Cancel reservation", response = ReservationDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation cancelled", response = ReservationDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_RESERVATION_CANCEL,
            produces = "application/json; charset=UTF-8")
    public ReservationDTO cancelReservation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Set<Reservation> reservations = new HashSet<>();
        restorer.getRestaurant().getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
        Optional<Reservation> reservation = reservations.stream()
                .filter(res -> res.getId().equals(Long.decode(id)))
                .findFirst();
        if (reservation.isPresent()) {
            reservationService.cancelReservation(reservation.get());
            response.setStatus(HttpServletResponse.SC_OK);
            return modelMapper.map(reservation, ReservationDTO.class);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Delete reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation deleted"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @DeleteMapping(value = UrlRequests.DELETE_RESERVATION)
    public void deleteReservation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id){
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Set<Reservation> reservations = new HashSet<>();
        restorer.getRestaurant().getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
        Optional<Reservation> reservation = reservations.stream()
                .filter(res -> res.getId().equals(Long.decode(id)))
                .findFirst();
        if (reservation.isPresent()) {
            reservationService.deleteReservation(Long.decode(id));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Get reservation for table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservations list"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_RESERVATIONS_ONE_TABLE,
            produces = "application/json; charset=UTF-8")
    public List<ReservationDTO> getReservationsForTable(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @PathVariable String id,
                                          @PathVariable String from,
                                          @PathVariable String to) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Optional<RestaurantTable> restaurantTable = restorer.getRestaurant().getRestaurantTables().stream()
                .filter(table -> table.getId().equals(Long.parseLong(id)))
                .findFirst();
        try {
            if (restaurantTable.isPresent()) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
                Date dateFrom = sdf.parse(from);
                Date dateTo = sdf.parse(to);
                Set<Reservation> reservations = restaurantTable.get().getReservation();
                Set<Reservation> filteredReservations = reservations.stream()
                        .filter(reservation -> reservation.getReservationDate().after(dateFrom)
                                && reservation.getReservationDate().before(dateTo))
                        .collect(Collectors.toSet());
                response.setStatus(HttpServletResponse.SC_OK);
                return filteredReservations.stream()
                        .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                        .collect(Collectors.toList());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @ApiOperation(value = "Get reservation for all tables")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservations list"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_RESERVATIONS_ALL_TABLES,
            produces = "application/json; charset=UTF-8")
    public List<ReservationDTO> getReservationsForAllTables(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @PathVariable String from,
                                              @PathVariable String to) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        try {
            Restaurant restaurant = restorer.getRestaurant();
            if (restaurant != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
                Date dateFrom = sdf.parse(from);
                Date dateTo = sdf.parse(to);
                Set<Reservation> reservations = new HashSet<>();
                restaurant.getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
                Set<Reservation> filteredReservations = reservations.stream()
                        .filter(reservation -> reservation.getReservationDate().after(dateFrom)
                                && reservation.getReservationDate().before(dateTo))
                        .collect(Collectors.toSet());
                response.setStatus(HttpServletResponse.SC_OK);
                return filteredReservations.stream()
                        .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                        .collect(Collectors.toList());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @ApiOperation(value = "Create reservation by client")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation created"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_RESERVATION_ADD_BY_CLIENT,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public List<ReservationDTO> createReservationByClient(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody ReservationDTO reservationDTO,
                                            @PathVariable String id) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restaurantService.getById(Long.decode(id));
        if (restaurant == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
            TableSearcherRequest tableSearcherRequest = new TableSearcherRequest(sdf.parse(reservationDTO.getDateReservation()),
                    reservationDTO.getReservationLength(), reservationDTO.getReservedPlaces());
            List<RestaurantTable> restaurantTables = tableSearcher.searchTableByRequest(restaurant, tableSearcherRequest);
            if (!restaurantTables.isEmpty()) {
                List<Reservation> createdReservations = new ArrayList<>();
                for (RestaurantTable restaurantTable : restaurantTables) {
                    Reservation reservation = modelMapper.map(reservationDTO, Reservation.class);
                    reservation.setClient(client);
                    reservation.setRestaurantTable(restaurantTable);
                    createdReservations.add(reservationService.createReservation(reservation));
                }
                response.setStatus(HttpServletResponse.SC_CREATED);
                return createdReservations.stream()
                        .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                        .collect(Collectors.toList());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @ApiOperation(value = "Get all reservations for client", response = ReservationsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservations list", response = ReservationsDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_RESERVATIONS_LIST,
            produces = "application/json; charset=UTF-8")
    public ReservationsDTO getReservationsForClient(HttpServletRequest request,
                                            HttpServletResponse response) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return ReservationsDTO.builder().reservations(client.getReservations().stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                .collect(Collectors.toList())).build();
    }

    @ApiOperation(value = "Cancel reservation by client", response = ReservationDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation cancelled", response = ReservationDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_RESERVATION_CANCEL_BY_CLIENT,
            produces = "application/json; charset=UTF-8")
    public ReservationDTO cancelReservationByClient(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id){
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Reservation reservation = reservationService.getById(Long.decode(id));
        if (reservation != null) {
            reservationService.cancelReservation(reservation);
            response.setStatus(HttpServletResponse.SC_OK);
            return modelMapper.map(reservation, ReservationDTO.class);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

}
