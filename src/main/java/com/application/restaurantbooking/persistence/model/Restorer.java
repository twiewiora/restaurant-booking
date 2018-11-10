package com.application.restaurantbooking.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "restorer")
@Getter
@Setter
public class Restorer extends User {

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "restorer", orphanRemoval = true)
    private Restaurant restaurant;

}
