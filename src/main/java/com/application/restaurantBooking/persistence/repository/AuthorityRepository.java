package com.application.restaurantBooking.persistence.repository;

import com.application.restaurantBooking.persistence.model.Authority;
import com.application.restaurantBooking.persistence.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityName Name);
}
