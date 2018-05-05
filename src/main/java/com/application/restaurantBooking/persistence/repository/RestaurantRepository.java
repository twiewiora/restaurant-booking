package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

}
