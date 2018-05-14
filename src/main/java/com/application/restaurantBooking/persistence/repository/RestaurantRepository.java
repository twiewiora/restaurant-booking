package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    @Query("select restaurant_table from restaurant_table as res_table where res_table.restaurant_id = :id and is_reserved <> true")
    List<RestaurantTable> getFreeTables(@Param("id") Long id);

}
