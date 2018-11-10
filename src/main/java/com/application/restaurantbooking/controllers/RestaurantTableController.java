package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.controllers.dto.RestaurantTableDTO;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.RestaurantTable;
import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.service.RestaurantTableService;
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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class RestaurantTableController {

    private static final Logger LOGGER = Logger.getLogger(RestaurantTableController.class.getName());

    private UserServiceManager userServiceManager;

    private RestaurantTableService restaurantTableService;

    private TableSearcher tableSearcher;

    private ModelMapper modelMapper;

    @Autowired
    public RestaurantTableController(UserServiceManager userServiceManager,
                                     RestaurantTableService restaurantTableService,
                                     TableSearcher tableSearcher){
        this.userServiceManager = userServiceManager;
        this.restaurantTableService = restaurantTableService;
        this.tableSearcher = tableSearcher;
        this.modelMapper = new ModelMapper();
    }

    @ApiOperation(value = "Get tables for restaurant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tables list"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_TABLES_FOR_RESTAURANT,
            produces = "application/json; charset=UTF-8")
    public List<RestaurantTableDTO> getAllTablesForRestaurant(HttpServletRequest request,
                                            HttpServletResponse response) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        if (restaurant != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            return restaurant.getRestaurantTables().stream()
                    .map(table -> modelMapper.map(table, RestaurantTableDTO.class))
                    .collect(Collectors.toList());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Get tables by search query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tables list"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_TABLES_BY_SEARCH,
            produces = "application/json; charset=UTF-8")
    public List<RestaurantTableDTO> getTablesBySearch(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestParam String date,
                                    @RequestParam int length,
                                    @RequestParam int places) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        try {
            if (restaurant != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
                TableSearcherRequest tableRequest = new TableSearcherRequest(sdf.parse(date), length, places);
                List<RestaurantTable> tables = tableSearcher.searchTableByRequest(restaurant, tableRequest);
                response.setStatus(HttpServletResponse.SC_OK);
                return tables.stream().map(table -> modelMapper.map(table, RestaurantTableDTO.class))
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

    @ApiOperation(value = "Get all free tables for restaurant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tables list"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_FREE_TABLES,
            produces = "application/json; charset=UTF-8")
    public List<RestaurantTableDTO> getFreeTables(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestParam String date,
                                    @RequestParam int length) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        try {
            if (restaurant != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
                TableSearcherRequest tableRequest = new TableSearcherRequest(sdf.parse(date), length, null);
                List<RestaurantTable> tables = tableSearcher.getFreeTables(restaurant, tableRequest);
                response.setStatus(HttpServletResponse.SC_OK);
                return tables.stream().map(table -> modelMapper.map(table, RestaurantTableDTO.class))
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

    @ApiOperation(value = "Create table", response = RestaurantTableDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Table created", response = RestaurantTableDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_TABLE_ADD,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public RestaurantTableDTO createRestaurantTable(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody RestaurantTableDTO restaurantTableDTO){
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        if (restaurant != null) {
            RestaurantTable restaurantTable = modelMapper.map(restaurantTableDTO, RestaurantTable.class);
            restaurantTable.setRestaurant(restaurant);
            RestaurantTable createdRestaurantTable = restaurantTableService.createRestaurantTable(restaurantTable);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return modelMapper.map(createdRestaurantTable, RestaurantTableDTO.class);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Update table", response = RestaurantTableDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Table updated", response = RestaurantTableDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_TABLE_UPDATE,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public RestaurantTableDTO updateRestaurantTable(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody RestaurantTableDTO restaurantTableDTO){
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        if (restaurant != null) {
            Optional<RestaurantTable> restaurantTableOptional = restorer.getRestaurant().getRestaurantTables().stream()
                    .filter(table -> table.getId().equals(restaurantTableDTO.getId())).findFirst();
            if (restaurantTableOptional.isPresent()) {
                RestaurantTable restaurantTable = restaurantTableOptional.get();
                restaurantTable.setMaxPlaces(restaurantTableDTO.getMaxPlaces());
                restaurantTable.setComment(restaurantTableDTO.getComment());
                restaurantTable.setIdentifier(restaurantTableDTO.getIdentifier());
                restaurantTableService.updateRestaurantTable(restaurantTable);
                response.setStatus(HttpServletResponse.SC_OK);
                return modelMapper.map(restaurantTable, RestaurantTableDTO.class);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Delete table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Table deleted"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @DeleteMapping(value = UrlRequests.DELETE_TABLE)
    public void deleteRestaurantTable(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String id){
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Optional<RestaurantTable> restaurantTable = restorer.getRestaurant().getRestaurantTables().stream()
                .filter(table -> table.getId().equals(Long.decode(id))).findFirst();
        if (restaurantTable.isPresent()) {
            restaurantTableService.deleteRestaurantTable(Long.decode(id));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
