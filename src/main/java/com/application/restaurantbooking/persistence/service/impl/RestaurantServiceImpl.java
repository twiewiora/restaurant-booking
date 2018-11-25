package com.application.restaurantbooking.persistence.service.impl;

import com.application.restaurantbooking.persistence.model.OpenHours;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.Tag;
import com.application.restaurantbooking.persistence.repository.OpenHoursRepository;
import com.application.restaurantbooking.persistence.repository.RestaurantRepository;
import com.application.restaurantbooking.persistence.service.RestaurantService;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    private OpenHoursRepository openHoursRepository;

    private EntityManager entityManager;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 OpenHoursRepository openHoursRepository,
                                 EntityManager entityManager){
        this.restaurantRepository = restaurantRepository;
        this.openHoursRepository = openHoursRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> getRestaurantByNameAndCity(String name, String city) {
        if (name == null && city == null) {
            return getAll();
        }
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Restaurant.class)
                .get();

        BooleanJunction queryStream = queryBuilder
                .bool();

        if (name != null) {
            queryStream = queryStream.must(queryBuilder
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(1)
                    .withPrefixLength(0)
                    .onField("name")
                    .matching("*" + name + "*")
                    .createQuery());
        }
        if (city != null) {
            queryStream = queryStream.must(queryBuilder
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(1)
                    .withPrefixLength(0)
                    .onField("city")
                    .matching("*" + city + "*")
                    .createQuery());
        }

        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(queryStream.createQuery(), Restaurant.class);
        return jpaQuery.getResultList();
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepository.updateRestaurant(restaurant.getId(), restaurant.getName(), restaurant.getCity(),
                restaurant.getStreet(), restaurant.getStreetNumber(), restaurant.getPhoneNumber(), restaurant.getWebsite());
    }

    @Override
    public void updateRestaurantTags(Restaurant restaurant, Set<Tag> tags) {
        restaurant.setTags(tags);
    }

    @Override
    public void addOpenHours(Restaurant restaurant, Map<DayOfWeek, OpenHours> openHoursMap) {
        openHoursMap.values().forEach(openHours -> openHoursRepository.save(openHours));
        openHoursMap.forEach(restaurant.getOpenHoursMap()::put);
        restaurantRepository.save(restaurant);
    }

    @Override
    public void updateOpenHours(Restaurant restaurant) {
        restaurant.getOpenHoursMap().values().forEach(openHours -> openHoursRepository.save(openHours));
        restaurantRepository.save(restaurant);
    }
}
