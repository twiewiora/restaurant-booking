package com.application.restaurantBooking.utils;

import java.util.Date;

public class TableSearcherRequest {

    private Date date;

    private Integer length;

    private Integer places;

    public TableSearcherRequest(Date date, Integer length, Integer places) {
        this.date = date;
        this.length = length;
        this.places = places;
    }

    public Date getDate() {
        return date;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getPlaces() {
        return places;
    }
}
