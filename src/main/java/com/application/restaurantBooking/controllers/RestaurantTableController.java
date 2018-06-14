package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantBooking.persistence.builder.RestaurantTableBuilder;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import com.application.restaurantBooking.persistence.service.RestorerService;
import com.application.restaurantBooking.utils.TableSearcher;
import com.application.restaurantBooking.utils.TableSearcherRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class RestaurantTableController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private RestorerService restorerService;

    private RestaurantTableService restaurantTableService;

    private TableSearcher tableSearcher;

    @Autowired
    public RestaurantTableController(JwtTokenUtil jwtTokenUtil,
                                     RestorerService restorerService,
                                     RestaurantTableService restaurantTableService,
                                     TableSearcher tableSearcher){
        this.jwtTokenUtil = jwtTokenUtil;
        this.restorerService = restorerService;
        this.restaurantTableService = restaurantTableService;
        this.tableSearcher = tableSearcher;
    }

    @RequestMapping(value = UrlRequests.GET_TABLES_FOR_RESTAURANT,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getAllTablesForRestaurant(HttpServletRequest request,
                                            HttpServletResponse response) {
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restorer.getRestaurant();

        try {
            if (restaurant != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                response.setStatus(HttpServletResponse.SC_OK);
                return objectMapper.writeValueAsString(restaurant.getRestaurantTables());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.GET_TABLES_BY_SEARCH,
            method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String getTablesBySearch(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody String json) {
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restorer.getRestaurant();

        try {
            if (restaurant != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(json);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
                TableSearcherRequest tableRequest = new TableSearcherRequest(sdf.parse(jsonNode.get("date").asText()),
                        jsonNode.get("length").asInt(), jsonNode.get("places").asInt());
                List<RestaurantTable> tables = tableSearcher.searchTableByRequest(restaurant, tableRequest);
                response.setStatus(HttpServletResponse.SC_OK);
                return objectMapper.writeValueAsString(tables);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.POST_TABLE_ADD,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String createRestaurantTable(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody String json){
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restorer.getRestaurant();

        try {
            if (restaurant != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode mainNode = objectMapper.readTree(json);
                RestaurantTable restaurantTable = new RestaurantTableBuilder()
                        .restaurant(restaurant)
                        .maxPlaces(mainNode.get("maxPlaces").asInt())
                        .build();
                restaurantTableService.createRestaurantTable(restaurantTable);
                response.setStatus(HttpServletResponse.SC_CREATED);
                return AcceptResponses.RESTAURANT_TABLE_CREATED;
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.POST_TABLE_UPDATE,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String updateRestaurantTable(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody String json){
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restorer.getRestaurant();

        try {
            if (restaurant != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(json);
                RestaurantTable restaurantTable = restorer.getRestaurant().getRestaurantTables().stream()
                        .filter(table -> table.getId().equals(jsonNode.get("tableId").asLong())).findFirst().orElse(null);
                if (restaurantTable != null) {
                    restaurantTable.setMaxPlaces(jsonNode.get("maxPlaces").asInt());
                    restaurantTableService.updateRestaurantTable(restaurantTable);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return AcceptResponses.RESTAURANT_TABLE_UPDATED;
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return ErrorResponses.RESTAURANT_TABLE_NOT_FOUND;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.DELETE_TABLE,
            method = RequestMethod.DELETE,
            produces = "application/json; charset=UTF-8")
    public String deleteRestaurantTable(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String id){
        Restorer restorer = getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        RestaurantTable restaurantTable = restorer.getRestaurant().getRestaurantTables().stream()
                .filter(table -> table.getId().equals(Long.decode(id))).findFirst().orElse(null);

        if (restaurantTable != null) {
            restaurantTableService.deleteRestaurantTable(Long.decode(id));
            response.setStatus(HttpServletResponse.SC_OK);
            return AcceptResponses.RESTAURANT_TABLE_DELETED;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorResponses.RESTAURANT_TABLE_NOT_FOUND;
        }
    }

    private Restorer getRestorerByJwt(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return restorerService.getByUsername(username);
    }

}
