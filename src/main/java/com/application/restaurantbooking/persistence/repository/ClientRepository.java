package com.application.restaurantbooking.persistence.repository;

import com.application.restaurantbooking.persistence.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUsername(String username);

}
