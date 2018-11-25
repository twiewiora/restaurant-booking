package com.application.restaurantbooking.persistence.service.impl;

import com.application.restaurantbooking.persistence.model.Reservation;
import com.application.restaurantbooking.persistence.repository.ReservationRepository;
import com.application.restaurantbooking.persistence.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation getById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        return reservation;
    }

    @Override
    public void cancelReservation(Reservation reservation) {
        reservation.setIsCancelled(true);
        reservationRepository.save(reservation);
    }
}
