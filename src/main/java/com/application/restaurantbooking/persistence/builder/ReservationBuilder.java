package com.application.restaurantbooking.persistence.builder;

import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Reservation;
import com.application.restaurantbooking.persistence.model.RestaurantTable;

import java.util.Date;

public class ReservationBuilder {

    private Reservation reservation;

    public ReservationBuilder() {
        reservation = new Reservation();
    }

    public ReservationBuilder client(Client client) {
        reservation.setClient(client);
        return this;
    }

    public ReservationBuilder restaurantTable(RestaurantTable restaurantTable) {
        reservation.setRestaurantTable(restaurantTable);
        return this;
    }

    public ReservationBuilder reservedPlaces(Integer reservedPlaces) {
        reservation.setReservedPlaces(reservedPlaces);
        return this;
    }

    public ReservationBuilder reservationDate(Date reservationDate ) {
//        reservation.setReservationDate(reservationDate);
        return this;
    }

    public ReservationBuilder reservationLength(Integer reservationLength) {
        reservation.setReservationLength(reservationLength);
        return this;
    }

    public ReservationBuilder comment(String comment) {
        reservation.setComment(comment);
        return this;
    }

    public ReservationBuilder cancelled(Boolean cancelled) {
        reservation.setIsCancelled(cancelled);
        return this;
    }

    public Reservation build() {
        return reservation;
    }
}
