package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Modifying
    @Query("update Restaurant r set r.name = ?2, r.city = ?3, r.street = ?4, r.phoneNumber = ?5 where r.id = ?1")
    void updateRestaurant(Long id, String name, String city, String street, String phoneNumber);

}
