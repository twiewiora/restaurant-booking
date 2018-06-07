package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Reservation;

public interface ReservationService {

    Reservation getById(Long id);

    Reservation createReservation(Reservation reservation);

    void deleteReservation(Long id);

}
