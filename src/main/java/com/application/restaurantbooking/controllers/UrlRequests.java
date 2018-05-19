package com.application.restaurantbooking.controllers;

public class UrlRequests {

    private UrlRequests(){
    }
    
    public static final String URL_TO_CROSS_ORIGIN = "https://restaurant-booker.herokuapp.com";

    // validation for users
    public static final String VALIDATE_RESTORER = "/api/restorer/validate";
    public static final String VALIDATE_CLIENT = "/api/client/validate";
    // API for restorers
    public static final String GET_TABLES_FOR_RESTAURANT = "/api/tables";
    public static final String GET_TABLES_BY_SEARCH = "/api/tables/search";
    public static final String GET_FREE_TABLES = "/api/freeTables";
    public static final String POST_TABLE_ADD = "/api/table/add";
    public static final String POST_TABLE_UPDATE = "/api/table/update";
    public static final String DELETE_TABLE = "/api/table/delete/tableId={id}";
    public static final String GET_RESTAURANT_BY_RESTORER = "/api/restaurant";
    public static final String POST_RESTAURANT_ADD = "/api/restaurant/add";
    public static final String POST_RESTAURANT_UPDATE = "/api/restaurant/update";
    public static final String GET_OPEN_HOURS_ALL = "/api/openHours/all";
    public static final String GET_OPEN_HOURS_DAY = "/api/openHours/day={day}";
    public static final String POST_OPEN_HOURS_UPDATE = "/api/openHours/update";
    public static final String POST_RESERVATION_ADD = "/api/reservation/add";
    public static final String POST_RESERVATION_CANCEL = "/api/reservation/cancel/reservationId={id}";
    public static final String GET_RESERVATIONS_ONE_TABLE = "/api/reservation/forTable/tableId={id}/dateFrom={from}/dateTo={to}";
    public static final String GET_RESERVATIONS_ALL_TABLES = "/api/reservation/all/dateFrom={from}/dateTo={to}";
    public static final String GET_TAGS = "/api/tags";
    public static final String GET_PRICES = "/api/prices";
    public static final String GET_CLIENT_BY_ID = "/api/client{id}";

    // API for clients
    public static final String GET_RESTAURANT_BY_ID = "/api/client/restaurant{id}";
    public static final String GET_SEARCH_QUERY_RESTAURANTS = "/api/client/searchQueryRestaurant";
    public static final String GET_RESTAURANT_FREE_DATES = "/api/client/restaurant{id}/freeDates";
    public static final String POST_RESERVATION_ADD_BY_CLIENT = "/api/client/restaurant{id}/reservation/add";
    public static final String GET_RESERVATIONS_LIST= "/api/client/reservations";
    public static final String POST_RESERVATION_CANCEL_BY_CLIENT = "/api/client/reservation{id}/cancel";

}
