package com.application.restaurantbooking.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OpenHoursDTO {

    private Long id;

    private Boolean isClose;

    private Date openHour;

    private Date closeHour;

}
