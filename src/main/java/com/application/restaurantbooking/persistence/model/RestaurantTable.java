package com.application.restaurantbooking.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurantTable")
@Getter
@Setter
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurantTable", orphanRemoval = true)
    private Set<Reservation> reservation = new HashSet<>();

    private Integer maxPlaces;

    private Boolean isReserved = false;

    private String comment;

    private String identifier;

    public RestaurantTable() {
        // empty constructor for hibernate
    }

}
