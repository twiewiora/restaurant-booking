package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select restaurantTable from RestaurantTable restaurantTable where restaurantTable.restaurant = :id and isReserved <> true")
    List<RestaurantTable> getFreeTables(@Param("id") Long id);

}
