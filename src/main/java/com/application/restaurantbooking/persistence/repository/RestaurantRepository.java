package com.application.restaurantbooking.persistence.repository;

import com.application.restaurantbooking.persistence.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Modifying
    @Query("update Restaurant r set r.name = ?2, r.city = ?3, r.street = ?4, r.streetNumber = ?5, r.phoneNumber = ?6, r.website = ?7 where r.id = ?1")
    void updateRestaurant(Long id, String name, String city, String street, String streetNumber, String phoneNumber, String website);

    List<Restaurant> findByCity(String city);
}
