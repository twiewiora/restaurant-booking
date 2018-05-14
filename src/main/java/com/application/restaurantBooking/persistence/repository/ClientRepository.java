package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

}
