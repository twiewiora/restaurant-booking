package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

}
