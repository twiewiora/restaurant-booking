package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.persistence.model.Reservation;

public interface ReservationService {

    Reservation getById(Long id);

    Reservation createReservation(Reservation reservation);

    void deleteReservation(Long id);

    void cancelReservation(Reservation reservation);

}
