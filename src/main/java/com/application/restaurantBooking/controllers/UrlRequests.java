package com.application.restaurantBooking.controllers;

public enum UrlRequests {
    GET_ALL_RESTAURANTS {
    @Override
        public String toString() {
            return "/restaurants/getAll";
        }
    },
    GET_RESTAURANT_BY_ID {
        @Override
        public String toString() {
            return "/restaurant/id{id}";
        }
    }
}
