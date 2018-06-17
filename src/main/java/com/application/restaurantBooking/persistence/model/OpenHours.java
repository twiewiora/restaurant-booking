package com.application.restaurantBooking.persistence.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "openHours")
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsClose() {
        return isClose;
    }

    public void setClose(Boolean close) {
        isClose = close;
    }

    public Date getOpenHour() {
        return openHour;
    }

    public void setOpenHour(Date openHour) {
        this.openHour = openHour;
    }

    public Date getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(Date closeHour) {
        this.closeHour = closeHour;
    }
}


