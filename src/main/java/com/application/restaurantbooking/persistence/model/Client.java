package com.application.restaurantbooking.persistence.model;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String login;

    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    private String phoneNumber;

    private String email;

    @ElementCollection(fetch = FetchType.LAZY)
    private Map<Tag, Integer> clientTagsMap = new EnumMap<>(Tag.class);

    public Client(){
        // empty constructor for hibernate
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<Tag, Integer> getClientTagsMap() {
        return clientTagsMap;
    }

    public void setClientTagsMap(Map<Tag, Integer> clientTagsMap) {
        this.clientTagsMap = clientTagsMap;
    }
}
