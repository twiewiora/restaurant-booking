package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.persistence.model.Reservation;

public interface ReservationService {

    Reservation getById(Long id);

    Reservation createReservation(Reservation reservation);

    void cancelReservation(Reservation reservation);

}
