package com.application.restaurantBooking.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String city;

    private String street;

    private String phoneNumber;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restorer_id", nullable = false)
    private Restorer restorer;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", orphanRemoval = true)
    private Set<RestaurantTable> restaurantTables = new HashSet<>();

    @JsonIgnore
    @MapKeyClass(value = DayOfWeek.class)
    @MapKeyEnumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = OpenHours.class)
    private Map<DayOfWeek, OpenHours> openHoursMap = new HashMap<>();

    public Restaurant(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
