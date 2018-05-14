package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.RestaurantTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends CrudRepository<RestaurantTable, Long> {

}
