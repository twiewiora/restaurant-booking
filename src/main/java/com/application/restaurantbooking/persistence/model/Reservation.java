package com.application.restaurantbooking.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantTable_id")
    private RestaurantTable restaurantTable;

    private Integer reservedPlaces;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm")
    private Date reservationDate;

    private Integer reservationLength;

    private String comment;

    private Boolean isCancelled = false;

    public Reservation(){
        // empty constructor for hibernate
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    @JsonProperty
    public Long getRestaurantTableId() {
        if (restaurantTable != null) {
            return restaurantTable.getId();
        }
        return null;
    }

    @JsonProperty
    public Long getRestaurantId() {
        if (restaurantTable != null && restaurantTable.getRestaurant() != null) {
            return restaurantTable.getRestaurant().getId();
        }
        return null;
    }

    public Integer getReservedPlaces() {
        return reservedPlaces;
    }

    public void setReservedPlaces(Integer reservedPlaces) {
        this.reservedPlaces = reservedPlaces;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    @JsonProperty
    public String getDateReservation() {
        if (reservationDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            return sdf.format(reservationDate);
        }
        return "";
    }

    public Integer getReservationLength() {
        return reservationLength;
    }

    public void setReservationLength(Integer reservationLength) {
        this.reservationLength = reservationLength;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }
}
