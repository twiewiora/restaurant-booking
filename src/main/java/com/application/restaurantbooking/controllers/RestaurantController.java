package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.controllers.dto.OpenHoursDTO;
import com.application.restaurantbooking.controllers.dto.ProposalHoursDTO;
import com.application.restaurantbooking.controllers.dto.RestaurantDTO;
import com.application.restaurantbooking.controllers.dto.RestaurantSearchQueryResultDTO;
import com.application.restaurantbooking.persistence.builder.OpenHoursBuilder;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@RestController
public class RestaurantController {

    private static final Logger LOGGER = Logger.getLogger(RestaurantController.class.getName());

    private UserServiceManager userServiceManager;

    private ClientService clientService;

    private RestaurantService restaurantService;

    private TableSearcher tableSearcher;

    private RestaurantSearcher restaurantSearcher;

    private GeocodeUtil geocodeUtil;

    private ModelMapper modelMapper;

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
        this.modelMapper = new ModelMapper();
    }

    @ApiOperation(value = "Get restaurant for restorer", response = RestaurantDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurant Details", response = RestaurantDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_RESTAURANT_BY_RESTORER,
            produces = "application/json; charset=UTF-8")
    public RestaurantDTO getRestaurantByRestorerJwt(HttpServletRequest request,
                                                    HttpServletResponse response) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return modelMapper.map(restorer.getRestaurant(), RestaurantDTO.class);
    }

    @ApiOperation(value = "Create restaurant", response = RestaurantDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Restaurant created", response = RestaurantDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_RESTAURANT_ADD,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public RestaurantDTO createRestaurant(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody RestaurantDTO restaurantDTO) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        try {
            Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
            restaurant.setRestorer(restorer);
            Localization localization = geocodeUtil.getLocalizationByAddress(restaurant.getCity(),
                    restaurant.getStreet(), restaurant.getStreetNumber());
            restaurant.setLongitude(localization.getLongitude());
            restaurant.setLatitude(localization.getLatitude());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Map<DayOfWeek, OpenHours> openHoursMap = new EnumMap<>(DayOfWeek.class);
            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                OpenHours day = new OpenHoursBuilder()
                        .openHour(sdf.parse("12:00"))
                        .closeHour(sdf.parse("22:00"))
                        .isClose(false)
                        .build();
                openHoursMap.put(dayOfWeek, day);
            }
            restaurantService.addOpenHours(restaurant, openHoursMap);
            Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return modelMapper.map(createdRestaurant, RestaurantDTO.class);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @ApiOperation(value = "Update restaurant details", response = RestaurantDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurant updated", response = RestaurantDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_RESTAURANT_UPDATE,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public RestaurantDTO updateRestaurant(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody RestaurantDTO restaurantDTO) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        if (restaurant != null) {
            restaurant.setName(StringUtils.stripAccents(restaurantDTO.getName()));
            restaurant.setCity(StringUtils.stripAccents(restaurantDTO.getCity()));
            restaurant.setStreet(StringUtils.stripAccents(restaurantDTO.getStreet()));
            restaurant.setStreetNumber(restaurantDTO.getStreetNumber());
            restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
            restaurant.setWebsite(restaurantDTO.getWebsite());
            restaurant.setPrice(restaurantDTO.getRestaurantPrice().stream().findAny().orElse(null));
            Set<Tag> tags = Sets.newHashSet(restaurantDTO.getTags());
            restaurant.setTags(tags);

            Localization localization = geocodeUtil.getLocalizationByAddress(restaurant.getCity(),
                    restaurant.getStreet(), restaurant.getStreetNumber());
            restaurant.setLongitude(localization.getLongitude());
            restaurant.setLatitude(localization.getLatitude());
            restaurantService.updateRestaurant(restaurant);
            restaurantService.updateRestaurantTags(restaurant, tags);
            response.setStatus(HttpServletResponse.SC_OK);
            return modelMapper.map(restaurant, RestaurantDTO.class);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Get restaurant open hours for all days")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Open hours details"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_OPEN_HOURS_ALL,
            produces = "application/json; charset=UTF-8")
    public Map<DayOfWeek, OpenHoursDTO> getOpenHoursForAllDays(HttpServletRequest request,
                                                               HttpServletResponse response) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        if (restaurant != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            return restaurant.getOpenHoursMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                    entry -> modelMapper.map(entry.getValue(), OpenHoursDTO.class)));
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Get restaurant open hours for some day", response = OpenHoursDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Open hours details for day", response = OpenHoursDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_OPEN_HOURS_DAY,
            produces = "application/json; charset=UTF-8")
    public OpenHoursDTO getOpenHoursForDay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String day) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restorer.getRestaurant();
        if (restaurant != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            OpenHours openHours = restaurant.getOpenHoursMap().get(DayOfWeek.valueOf(day.toUpperCase()));
            return modelMapper.map(openHours, OpenHoursDTO.class);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ApiOperation(value = "Update restaurant open hours")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Open hours updated"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping(value = UrlRequests.POST_OPEN_HOURS_UPDATE,
            consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    public Map<DayOfWeek, OpenHoursDTO> updateOpenHours(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestBody String json) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
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
                    openHour.setIsClose(dayNode.get(2).asBoolean());
                }
                restaurantService.updateOpenHours(restaurant);
                response.setStatus(HttpServletResponse.SC_OK);
                return openHoursMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> modelMapper.map(entry.getValue(), OpenHoursDTO.class)));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @ApiOperation(value = "Get all tags")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All tags"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_TAGS,
            produces = "application/json; charset=UTF-8")
    public List<Tag> getTags(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return Arrays.asList(Tag.values());
    }

    @ApiOperation(value = "Get all prices")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All prices"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_PRICES,
            produces = "application/json; charset=UTF-8")
    public List<Price> getPrices(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return Arrays.asList(Price.values());
    }

    @ApiOperation(value = "Get list restaurants by query", response = RestaurantSearchQueryResultDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurants list", response = RestaurantSearchQueryResultDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_SEARCH_QUERY_RESTAURANTS,
            produces = "application/json; charset=UTF-8")
    public RestaurantSearchQueryResultDTO getSurroundingRestaurants(HttpServletRequest request,
                                                                    HttpServletResponse response,
                                                                    @RequestParam (required = false) Double lat,
                                                                    @RequestParam (required = false) Double lon,
                                                                    @RequestParam (required = false) Integer radius,
                                                                    @RequestParam(required = false) String[] tags,
                                                                    @RequestParam(required = false) String[] prices,
                                                                    @RequestParam(required = false) String name) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Set<Tag> tagsSet = new HashSet<>();
        for (String tag : Optional.ofNullable(tags).orElse(new String[0])) {
            tagsSet.add(Tag.valueOf(tag.toUpperCase()));
        }
        Set<Price> pricesSet = new HashSet<>();
        for (String price : Optional.ofNullable(prices).orElse(new String[0])) {
            pricesSet.add(Price.valueOf(price.toUpperCase()));
        }
        Localization clientLocalization = new Localization(lat, lon);
        RestaurantSearcherRequest restaurantSearcherRequest = new RestaurantSearcherRequest(clientLocalization,
                radius, tagsSet, pricesSet, name);
        List<Restaurant> result = restaurantSearcher.getRestaurantsByQuery(restaurantSearcherRequest, client);
        response.setStatus(HttpServletResponse.SC_OK);
        return RestaurantSearchQueryResultDTO.builder().restaurants(result
                .stream().map(restaurant -> modelMapper.map(restaurant, RestaurantDTO.class))
                .collect(Collectors.toList())).build();
    }

    @ApiOperation(value = "Get list proposal hours for reservation", response = ProposalHoursDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurants list", response = ProposalHoursDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_RESTAURANT_FREE_DATES,
            produces = "application/json; charset=UTF-8")
    public ProposalHoursDTO getProposalHours(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @PathVariable String id,
                                   @RequestParam String date,
                                   @RequestParam int length,
                                   @RequestParam int places) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restaurantService.getById(Long.decode(id));
        try {
            if (restaurant != null) {
                saveClientPreferences(client, restaurant);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
                TableSearcherRequest tableRequest = new TableSearcherRequest(sdf.parse(date), length, places);
                List<String> proposalHours = tableSearcher.getNearHoursReservation(restaurant, tableRequest);
                response.setStatus(HttpServletResponse.SC_OK);
                return ProposalHoursDTO.builder().proposalHours(proposalHours).build();
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

    @ApiOperation(value = "Get restaurant for client by restaurant id", response = RestaurantDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restaurant details", response = RestaurantDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_RESTAURANT_BY_ID,
            produces = "application/json; charset=UTF-8")
    public RestaurantDTO getRestaurantById(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id) {
        Client client = userServiceManager.getClientByJwt(request);
        if (client == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Restaurant restaurant = restaurantService.getById(Long.decode(id));
        restaurantSearcher.calculateRestaurantPriority(client, restaurant);
        return modelMapper.map(restaurant, RestaurantDTO.class);
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
