package com.application.restaurantbooking.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client extends User {

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    @JsonIgnore
    @MapKeyClass(value = Tag.class)
    @MapKeyEnumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Map<Tag, Integer> clientPreferencesTags = new EnumMap<>(Tag.class);

    @JsonIgnore
    @MapKeyClass(value = Price.class)
    @MapKeyEnumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Map<Price, Integer> clientPreferencesPrices = new EnumMap<>(Price.class);

    public Client() {
        super();
        for (Price price : Price.values()) {
            this.clientPreferencesPrices.put(price, 0);
        }
        for (Tag tag : Tag.values()) {
            this.clientPreferencesTags.put(tag, 0);
        }
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Map<Tag, Integer> getClientPreferencesTags() {
        return clientPreferencesTags;
    }

    public void setClientPreferencesTags(Map<Tag, Integer> clientPreferencesTags) {
        this.clientPreferencesTags = clientPreferencesTags;
    }

    public Map<Price, Integer> getClientPreferencesPrices() {
        return clientPreferencesPrices;
    }

    public void setClientPreferencesPrices(Map<Price, Integer> clientPreferencesPrices) {
        this.clientPreferencesPrices = clientPreferencesPrices;
    }
}
