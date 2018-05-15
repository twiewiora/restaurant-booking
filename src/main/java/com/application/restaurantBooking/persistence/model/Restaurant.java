package com.application.restaurantBooking.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.*;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty
    private String name;

    private String city;

    private String street;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restorer_id", nullable = false)
    private Restorer restorer;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Set<RestaurantTable> restaurantTables = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private Map<DayOfWeek, OpenHours> openHoursMap = new HashMap<>();

    public Restaurant(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Restorer getRestorer() {
        return restorer;
    }

    public void setRestorer(Restorer restorer) {
        this.restorer = restorer;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<RestaurantTable> getRestaurantTables() {
        return restaurantTables;
    }

    public void setRestaurantTables(Set<RestaurantTable> restaurantTables) {
        this.restaurantTables = restaurantTables;
    }

    public Map<DayOfWeek, OpenHours> getOpenHoursMap() {
        return openHoursMap;
    }

    public void setOpenHoursMap(Map<DayOfWeek, OpenHours> openHoursMap) {
        this.openHoursMap = openHoursMap;
    }
}
