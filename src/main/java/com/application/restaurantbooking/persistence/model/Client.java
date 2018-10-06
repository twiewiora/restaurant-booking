package com.application.restaurantbooking.persistence.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "client")
public class Client extends User {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    @ElementCollection(fetch = FetchType.LAZY)
    private Map<Tag, Integer> clientTagsMap = new EnumMap<>(Tag.class);

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Map<Tag, Integer> getClientTagsMap() {
        return clientTagsMap;
    }

    public void setClientTagsMap(Map<Tag, Integer> clientTagsMap) {
        this.clientTagsMap = clientTagsMap;
    }
}
