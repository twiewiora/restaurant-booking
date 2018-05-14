package com.application.restaurantBooking.persistence.model;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String login;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "clients")
    private Set<Restaurant> visitedRestaurants;

    public Client(){
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

    public Set<Restaurant> getVisitedRestaurants() {
        return visitedRestaurants;
    }

    public void setVisitedRestaurants(Set<Restaurant> visitedRestaurants) {
        this.visitedRestaurants = visitedRestaurants;
    }
}
