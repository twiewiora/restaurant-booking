package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.persistence.builder.OpenHoursBuilder;
import com.application.restaurantbooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantbooking.persistence.model.*;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.persistence.service.RestaurantService;
import com.application.restaurantbooking.persistence.service.UserServiceManager;
import com.application.restaurantbooking.utils.RestaurantSearcher;
import com.application.restaurantbooking.utils.RestaurantSearcherRequest;
import com.application.restaurantbooking.utils.TableSearcher;
import com.application.restaurantbooking.utils.TableSearcherRequest;
import com.application.restaurantbooking.utils.geocoding.GeocodeUtil;
import com.application.restaurantbooking.utils.geocoding.Localization;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RestaurantController {

    private static final Logger LOGGER = Logger.getLogger(RestaurantController.class.getName());

    private UserServiceManager userServiceManager;

    private ClientService clientService;

    private RestaurantService restaurantService;

    private TableSearcher tableSearcher;

    private RestaurantSearcher restaurantSearcher;

    private GeocodeUtil geocodeUtil;

    @Autowired
    public RestaurantController(@NotNull UserServiceManager userServiceManager,
                                RestaurantService restaurantService,
                                TableSearcher tableSearcher,
                                RestaurantSearcher restaurantSearcher,
                                GeocodeUtil geocodeUtil) {
        this.userServiceManager = userServiceManager;
        this.clientService = userServiceManager.getClientService();
        this.restaurantService = restaurantService;
        this.tableSearcher = tableSearcher;
        this.restaurantSearcher = restaurantSearcher;
        this.geocodeUtil = geocodeUtil;
    }

    @GetMapping(value = UrlRequests.GET_RESTAURANT_BY_RESTORER,
            produces = "application/json; charset=UTF-8")
    public String getRestaurantByRestorerJwt(HttpServletRequest request,
                                             HttpServletResponse response) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restorer.getRestaurant();
        return returnRestaurantJson(restaurant, response);
    }

    @PostMapping(value = UrlRequests.POST_RESTAURANT_ADD,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String createRestaurant(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody String json) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
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
                    .streetNumber(jsonNode.get("streetNumber").asText())
                    .phoneNumber(jsonNode.get("phoneNumber").asText())
                    .website(jsonNode.get("website").asText())
                    .price(Price.valueOf(jsonNode.get("restaurantPrice").get(0).asText().toUpperCase()))
                    .restorer(restorer)
                    .tags(tags)
                    .build();

            Localization localization = geocodeUtil.getLocalizationByAddress(restaurant.getCity(),
                    restaurant.getStreet(), restaurant.getStreetNumber());
            restaurant.setLongitude(localization.getLongitude());
            restaurant.setLatitude(localization.getLatitude());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Map<DayOfWeek, OpenHours> openHoursMap = new EnumMap<>(DayOfWeek.class);
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
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @PostMapping(value = UrlRequests.POST_RESTAURANT_UPDATE,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String updateRestaurant(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody String json) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restorer.getRestaurant();

        try {
            if (restaurant != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(json);
                restaurant.setName(StringUtils.stripAccents(jsonNode.get("name").asText()));
                restaurant.setCity(StringUtils.stripAccents(jsonNode.get("city").asText()));
                restaurant.setStreet(StringUtils.stripAccents(jsonNode.get("street").asText()));
                restaurant.setStreetNumber(jsonNode.get("streetNumber").asText());
                restaurant.setPhoneNumber(jsonNode.get("phoneNumber").asText());
                restaurant.setWebsite(jsonNode.get("website").asText());
                restaurant.setPrice(Price.valueOf(jsonNode.get("restaurantPrice").get(0).asText().toUpperCase()));
                JsonNode tagsNode = jsonNode.get("tags");
                Set<Tag> tags = new HashSet<>();
                if (tagsNode.isArray()) {
                    for (JsonNode tag : tagsNode) {
                        tags.add(Tag.valueOf(tag.asText().toUpperCase()));
                    }
                }
                restaurant.setTags(tags);

                Localization localization = geocodeUtil.getLocalizationByAddress(restaurant.getCity(),
                        restaurant.getStreet(), restaurant.getStreetNumber());
                restaurant.setLongitude(localization.getLongitude());
                restaurant.setLatitude(localization.getLatitude());
                restaurantService.updateRestaurant(restaurant);
                restaurantService.updateRestaurantTags(restaurant, tags);
                response.setStatus(HttpServletResponse.SC_OK);
                return AcceptResponses.RESTAURANT_UPDATED;
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @GetMapping(value = UrlRequests.GET_OPEN_HOURS_ALL,
            produces = "application/json; charset=UTF-8")
    public String getOpenHoursForAllDays(HttpServletRequest request,
                                         HttpServletResponse response) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
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
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @GetMapping(value = UrlRequests.GET_OPEN_HOURS_DAY,
            produces = "application/json; charset=UTF-8")
    public String getOpenHoursForDay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String day) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
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
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @PostMapping(value = UrlRequests.POST_OPEN_HOURS_UPDATE,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public String updateOpenHours(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestBody String json) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
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
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @GetMapping(value = UrlRequests.GET_TAGS,
            produces = "application/json; charset=UTF-8")
    public String getTags(HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_OK);
            return objectMapper.writeValueAsString(Tag.values());
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @GetMapping(value = UrlRequests.GET_PRICES,
            produces = "application/json; charset=UTF-8")
    public String getPrices(HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_OK);
            return objectMapper.writeValueAsString(Price.values());
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @GetMapping(value = UrlRequests.GET_SURROUNDING_RESTAURANTS,
            produces = "application/json; charset=UTF-8")
    public String getSurroundingRestaurants(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestParam double lat,
                                            @RequestParam double lon,
                                            @RequestParam int radius,
                                            @RequestParam(required = false) String[] tags) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_OK);

            if (tags == null) {
                tags = new String[0];
            }
            Set<Tag> tagsSet = new HashSet<>();
            for (String tag : tags) {
                tagsSet.add(Tag.valueOf(tag.toUpperCase()));
            }
            Localization clientLocalization = new Localization(lat, lon);
            RestaurantSearcherRequest restaurantSearcherRequest = new RestaurantSearcherRequest(clientLocalization,
                    radius, tagsSet);
            String clientCityName = geocodeUtil.getCityByLocalization(clientLocalization);
            List<Restaurant> restaurantsInCity = restaurantService.getRestaurantByCity(clientCityName);
            List<Restaurant> result = restaurantSearcher.getSurroundingRestaurant(restaurantsInCity,
                    restaurantSearcherRequest, client);

            ArrayNode restaurantArray = objectMapper.createArrayNode();
            for (Restaurant restaurant : result) {
                restaurantArray.add(objectMapper.writeValueAsString(restaurant));
            }

            return objectMapper.createObjectNode().putPOJO("restaurants", restaurantArray).toString();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @GetMapping(value = UrlRequests.GET_RESTAURANT_FREE_DATES,
            produces = "application/json; charset=UTF-8")
    public String getProposalHours(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @PathVariable String id,
                                   @RequestParam String date,
                                   @RequestParam int length,
                                   @RequestParam int places) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restaurantService.getById(Long.decode(id));
        try {
            if (restaurant != null) {
                saveClientPreferences(client, restaurant);
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
                TableSearcherRequest tableRequest = new TableSearcherRequest(sdf.parse(date), length, places);
                List<String> proposalHours = tableSearcher.getNearHoursReservation(restaurant, tableRequest);
                response.setStatus(HttpServletResponse.SC_OK);
                ArrayNode proposalArray = objectMapper.createArrayNode();
                proposalHours.forEach(proposalArray::add);
                return objectMapper.createObjectNode().putPOJO("proposalHours", proposalArray).toString();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorResponses.RESTAURANT_NOT_FOUND;
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    @GetMapping(value = UrlRequests.GET_RESTAURANT_BY_ID,
            produces = "application/json; charset=UTF-8")
    public String getRestaurantById(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ErrorResponses.UNAUTHORIZED_ACCESS;
        }
        Restaurant restaurant = restaurantService.getById(Long.decode(id));
        restaurantSearcher.calculateRestaurantPriority(client, restaurant);
        return returnRestaurantJson(restaurant, response);
    }

    private String returnRestaurantJson(Restaurant restaurant, HttpServletResponse response) {
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
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorResponses.INTERNAL_ERROR;
        }
    }

    private void saveClientPreferences(Client client, Restaurant restaurant) {
        for (Tag tag : restaurant.getTags()) {
            client.getClientPreferencesTags().replace(tag, client.getClientPreferencesTags().get(tag) + 1);
        }
        client.getClientPreferencesPrices().replace(restaurant.getPrice(),
                client.getClientPreferencesPrices().get(restaurant.getPrice()) + 1);
        clientService.updateClient(client);
    }

}
