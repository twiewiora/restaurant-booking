package com.application.restaurantbooking.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "openHours")
@Getter
@Setter
public class OpenHours {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean isClose = false;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date openHour;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date closeHour;

    public OpenHours(){
        // empty constructor for hibernate
    }

}


