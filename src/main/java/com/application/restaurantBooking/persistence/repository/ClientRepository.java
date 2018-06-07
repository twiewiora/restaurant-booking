package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

}
