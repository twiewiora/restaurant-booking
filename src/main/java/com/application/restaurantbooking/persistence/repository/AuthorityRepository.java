package com.application.restaurantbooking.persistence.repository;

import com.application.restaurantbooking.persistence.model.Authority;
import com.application.restaurantbooking.persistence.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(AuthorityName name);

}