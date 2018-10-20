package com.application.restaurantbooking.utils;

import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.utils.geocoding.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestaurantSearcher {

    private ClientService clientService;

    @Autowired
    public RestaurantSearcher(ClientService clientService) {
        this.clientService = clientService;
    }

    public List<Restaurant> getSurroundingRestaurant(List<Restaurant> restaurantsInCity,
                                                     RestaurantSearcherRequest request,
                                                     Client client) {
        int restaurantsLimit = 20;
        List<Restaurant> restaurantsInRange = restaurantsInCity.stream()
                .filter(restaurant -> isRestaurantInRange(restaurant, request))
                .filter(restaurant -> request.getTags().isEmpty() || !Collections.disjoint(restaurant.getTags(), request.getTags()))
                .collect(Collectors.toList());

        restaurantsInRange.forEach(restaurant -> calculateRestaurantPriority(client, restaurant));
        restaurantsInRange.sort(Comparator.comparing(Restaurant::getPriority).reversed());
        return restaurantsInRange.stream().limit(restaurantsLimit).collect(Collectors.toList());
    }

    private boolean isRestaurantInRange(Restaurant restaurant, RestaurantSearcherRequest request) {
        return getDistanceToRestaurant(request.getLocalization(),
                new Localization(restaurant.getLatitude(), restaurant.getLongitude())) < request.getRadius();
    }

    private Double getDistanceToRestaurant(Localization clientLocalization, Localization restaurantLocalization) {
        return Math.sqrt(Math.pow(clientLocalization.getLatitude() - restaurantLocalization.getLatitude(), 2) +
                Math.pow(Math.cos((restaurantLocalization.getLatitude() * Math.PI) / 180) *
                        (clientLocalization.getLongitude() - restaurantLocalization.getLongitude()), 2)) *
                (40075.704 / 360) * 1000;
    }

    public void calculateRestaurantPriority(Client client, Restaurant restaurant) {
        double restaurantVisits = (double) clientService.getStatisticsForRestaurant(client.getId(), restaurant.getId());
        double tagsVisits = (double) restaurant.getTags().stream()
                .mapToInt(tag -> clientService.getStatisticsForTag(client.getId(), tag))
                .sum();
        double priceVisits = (double) clientService.getStatisticsForPrice(client.getId(), restaurant.getPrice());
        double tagsSearch = (double) restaurant.getTags().stream()
                .mapToInt(tag -> client.getClientPreferencesTags().get(tag))
                .sum();
        double pricesSearch = (double) client.getClientPreferencesPrices().get(restaurant.getPrice());
        double allVisits = (double) clientService.getAmountClientReservations(client.getId());
        double allSearch = (double) client.getClientPreferencesPrices().entrySet().stream()
                .mapToInt(Map.Entry::getValue)
                .sum();
        double restaurantPriority = 0.0;
        if (allSearch != 0 && allVisits != 0) {
            restaurantPriority = restaurantVisits / allVisits * 0.3 + priceVisits / allVisits * 0.2 +
                    tagsVisits / (allVisits * restaurant.getTags().size()) * 0.2 + tagsSearch / allSearch * 0.15 +
                    pricesSearch / allSearch * 0.15;
        }
        restaurant.setPriority(restaurantPriority);
    }
}
