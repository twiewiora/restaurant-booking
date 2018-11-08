package com.application.restaurantbooking.persistence.builder;

import com.application.restaurantbooking.persistence.model.OpenHours;

import java.util.Date;

public class OpenHoursBuilder {

    private OpenHours openHours;

    public OpenHoursBuilder() {
        openHours = new OpenHours();
    }

    public OpenHoursBuilder openHour(Date openHour) {
        openHours.setOpenHour(openHour);
        return this;
    }

    public OpenHoursBuilder closeHour(Date closeHour) {
        openHours.setCloseHour(closeHour);
        return this;
    }

    public OpenHoursBuilder isClose(boolean isClose) {
        openHours.setIsClose(isClose);
        return this;
    }

    public OpenHours build() {
        return openHours;
    }
}
