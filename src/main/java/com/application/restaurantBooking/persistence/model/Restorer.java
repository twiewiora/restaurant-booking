package com.application.restaurantBooking.persistence.model;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restorer")
public class Restorer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String login;

    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restorer", cascade = CascadeType.ALL)
    private Set<Restaurant> restaurants = new HashSet<>();

    public Restorer() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
