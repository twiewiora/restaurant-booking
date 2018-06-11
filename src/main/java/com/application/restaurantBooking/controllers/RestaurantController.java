package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantBooking.persistence.builder.OpenHoursBuilder;
import com.application.restaurantBooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantBooking.persistence.model.OpenHours;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.application.restaurantBooking.persistence.service.RestorerService;
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
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class RestaurantController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private RestorerService restorerService;

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(JwtTokenUtil jwtTokenUtil,
                                RestorerService restorerService,
                                RestaurantService restaurantService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
    }

    @RequestMapping(value = UrlRequests.GET_RESTAURANT_BY_RESTORER,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getRestaurantByRestorerJwt(HttpServletRequest request,
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
                return objectMapper.writeValueAsString(restaurant);
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

    @RequestMapping(value = UrlRequests.POST_RESTAURANT_ADD,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String createRestaurant(HttpServletRequest request,
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
            JsonNode tagsNode = jsonNode.get("tags");
            Set<Tag> tags = new HashSet<>();
            if (tagsNode.isArray()) {
                for (JsonNode tag : tagsNode) {
                    tags.add(Tag.valueOf(tag.asText().toUpperCase()));
                }
            }
            Restaurant restaurant = new RestaurantBuilder()
                    .name(jsonNode.get("name").asText())
                    .city(jsonNode.get("city").asText())
                    .street(jsonNode.get("street").asText())
                    .phoneNumber(jsonNode.get("phoneNumber").asText())
                    .restorer(restorer)
                    .tags(tags)
                    .build();
            restaurantService.createRestaurant(restaurant);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return AcceptResponses.RESTAURANT_CREATED;
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.POST_RESTAURANT_UPDATE,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String updateRestaurant(HttpServletRequest request,
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
                restaurant.setName(jsonNode.get("name").asText());
                restaurant.setCity(jsonNode.get("city").asText());
                restaurant.setStreet(jsonNode.get("street").asText());
                restaurant.setPhoneNumber(jsonNode.get("phoneNumber").asText());
                JsonNode tagsNode = jsonNode.get("tags");
                Set<Tag> tags = new HashSet<>();
                if (tagsNode.isArray()) {
                    for (JsonNode tag : tagsNode) {
                        tags.add(Tag.valueOf(tag.asText().toUpperCase()));
                    }
                }
                restaurant.setTags(tags);
                restaurantService.updateRestaurant(restaurant);
                restaurantService.updateRestaurantTags(restaurant.getId(), tags);
                response.setStatus(HttpServletResponse.SC_OK);
                return AcceptResponses.RESTAURANT_UPDATED;
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

    @RequestMapping(value = UrlRequests.GET_OPEN_HOURS_ALL,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getOpenHoursForAllDays(HttpServletRequest request,
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
                return objectMapper.writeValueAsString(restaurant.getOpenHoursMap());
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

    @RequestMapping(value = UrlRequests.GET_OPEN_HOURS_DAY,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getOpenHoursForDay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String day) {
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
                return objectMapper.writeValueAsString(restaurant.getOpenHoursMap()
                        .get(DayOfWeek.valueOf(day.toUpperCase())));
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

    @RequestMapping(value = UrlRequests.POST_OPEN_HOURS_UPDATE,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String updateOpenHours(HttpServletRequest request,
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
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Map<DayOfWeek, OpenHours> openHoursMap = new HashMap<>();
                for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                    JsonNode dayNode = jsonNode.get(dayOfWeek.toString().toLowerCase());
                    if (dayNode.isArray()) {
                        OpenHours day = new OpenHoursBuilder()
                                .openHour(sdf.parse(dayNode.get(0).asText()))
                                .closeHour(sdf.parse(dayNode.get(1).asText()))
                                .build();
                        openHoursMap.put(dayOfWeek, day);
                    }
                }
                restaurantService.addOpenHours(restaurant.getId(), openHoursMap);
                response.setStatus(HttpServletResponse.SC_OK);
                return AcceptResponses.OPEN_HOURS_UPDATED;
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

    private Restorer getRestorerByJwt(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return restorerService.getByUsername(username);
    }

}
