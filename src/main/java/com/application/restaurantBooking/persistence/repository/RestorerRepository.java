package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Restorer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RestorerRepository extends JpaRepository<Restorer, Long> {

    Restorer findByUsername(String username);

}
