package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Restorer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorerRepository extends CrudRepository<Restorer, Long> {

}
