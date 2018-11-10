package com.application.restaurantbooking.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client extends User {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    @MapKeyClass(value = Tag.class)
    @MapKeyEnumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Map<Tag, Integer> clientPreferencesTags = new EnumMap<>(Tag.class);

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

}
