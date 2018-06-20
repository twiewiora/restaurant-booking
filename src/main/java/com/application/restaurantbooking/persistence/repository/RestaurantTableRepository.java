package com.application.restaurantbooking.persistence.repository;

import com.application.restaurantbooking.persistence.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    @Modifying
    @Query("update RestaurantTable rt set rt.maxPlaces = ?2 where rt.id = ?1")
    void updateRestaurantTable(Long id, Integer maxPlaces);

}
