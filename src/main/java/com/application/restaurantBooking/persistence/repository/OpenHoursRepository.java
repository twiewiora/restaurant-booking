package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.OpenHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OpenHoursRepository extends JpaRepository<OpenHours, Long> {

}
