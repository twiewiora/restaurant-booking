package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Restorer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorerRepository extends JpaRepository<Restorer, Long> {
    Restorer findByUsername(String username);

}
