package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantBooking.persistence.builder.OpenHoursBuilder;
import com.application.restaurantBooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantBooking.persistence.model.*;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.application.restaurantBooking.persistence.service.RestorerService;
import com.application.restaurantBooking.utils.TableSearcher;
import com.application.restaurantBooking.utils.TableSearcherRequest;
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
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class RestaurantController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private RestorerService restorerService;

    private RestaurantService restaurantService;

    private TableSearcher tableSearcher;

    @Autowired
    public RestaurantController(JwtTokenUtil jwtTokenUtil,
                                RestorerService restorerService,
                                RestaurantService restaurantService,
                                TableSearcher tableSearcher) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
        this.tableSearcher = tableSearcher;
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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Map<DayOfWeek, OpenHours> openHoursMap = new HashMap<>();
            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                OpenHours day = new OpenHoursBuilder()
                        .openHour(sdf.parse("00:00"))
                        .closeHour(sdf.parse("00:00"))
                        .isClose(true)
                        .build();
                openHoursMap.put(dayOfWeek, day);
            }
            restaurantService.addOpenHours(restaurant, openHoursMap);
            restaurantService.createRestaurant(restaurant);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return AcceptResponses.RESTAURANT_CREATED;
        } catch (IOException | ParseException e) {
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
                restaurantService.updateRestaurantTags(restaurant, tags);
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
                return objectMapper.writeValueAsString(restaurant.getOpenHoursMap().entrySet().stream()
                        .filter(entry -> !entry.getValue().getIsClose()).collect(Collectors.toSet()));
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
                OpenHours openHours = restaurant.getOpenHoursMap().get(DayOfWeek.valueOf(day.toUpperCase()));
                return objectMapper.writeValueAsString(openHours);
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
                Map<DayOfWeek, OpenHours> openHoursMap = restaurant.getOpenHoursMap();
                for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                    JsonNode dayNode = jsonNode.get(dayOfWeek.toString().toLowerCase());
                    OpenHours openHour = openHoursMap.get(dayOfWeek);
                    openHour.setOpenHour(sdf.parse(dayNode.get(0).asText()));
                    openHour.setCloseHour(sdf.parse(dayNode.get(1).asText()));
                    openHour.setClose(dayNode.get(2).asBoolean());
                }
                restaurantService.updateOpenHours(restaurant);
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

    @RequestMapping(value = UrlRequests.GET_TAGS,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getTags(HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_OK);
            return objectMapper.writeValueAsString(Tag.values());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.GET_ALL_RESTAURANTS,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getAllRestaurants(HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_OK);
            ArrayNode restaurantArray = objectMapper.createArrayNode();
            for (Restaurant restaurant : restaurantService.getAll()) {
                restaurantArray.add(objectMapper.writeValueAsString(restaurant));
            }
            return objectMapper.createObjectNode().putPOJO("restaurants", restaurantArray).toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.GET_RESTAURANT_FREE_DATES,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getProposalHours(HttpServletResponse response,
                                   @PathVariable String id,
                                   @RequestParam String date,
                                   @RequestParam int length,
                                   @RequestParam int places) {
        Restaurant restaurant = restaurantService.getById(Long.decode(id));

        try {
            if (restaurant != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                TableSearcherRequest tableRequest = new TableSearcherRequest(sdf.parse(date), length, places);
                List<String> proposalHours = tableSearcher.getProposalStartHourReservation(restaurant, tableRequest);
                response.setStatus(HttpServletResponse.SC_OK);
                ArrayNode proposalArray = objectMapper.createArrayNode();
                proposalHours.forEach(proposalArray::add);
                return objectMapper.createObjectNode().putPOJO("proposalHours", proposalArray).toString();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (ParseException e) {
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
