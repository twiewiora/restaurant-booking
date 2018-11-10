package com.application.restaurantbooking.persistence.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantTable_id")
    private RestaurantTable restaurantTable;

    private Integer reservedPlaces;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm")
    @Setter(AccessLevel.NONE)
    private Date reservationDate;

    private Integer reservationLength;

    private String comment;

    private Boolean isCancelled = false;

    public Reservation(){
        // empty constructor for hibernate
    }

    public Long getRestaurantId() {
        if (restaurantTable != null && restaurantTable.getRestaurant() != null) {
            return restaurantTable.getRestaurant().getId();
        }
        return null;
    }

    public String getDateReservation() {
        if (reservationDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            return sdf.format(reservationDate);
        }
        return "";
    }

    public void setDateReservation(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        this.reservationDate = sdf.parse(date);
    }

}
