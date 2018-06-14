package com.application.restaurantBooking.controllers;

public class UrlRequests {

    public static final String GET_TABLES_FOR_RESTAURANT = "/api/tables";

    public static final String GET_TABLES_BY_SEARCH = "/api/tables/search";

    public static final String POST_TABLE_ADD = "/api/table/add";

    public static final String POST_TABLE_UPDATE = "/api/table/update";

    public static final String DELETE_TABLE = "/api/table/delete/tableId={id}";

    public static final String GET_RESTAURANT_BY_RESTORER = "/api/restaurant";

    public static final String GET_ALL_RESTAURANTS = "/api/restaurant/list";

    public static final String POST_RESTAURANT_ADD = "/api/restaurant/add";

    public static final String POST_RESTAURANT_UPDATE = "/api/restaurant/update";

    public static final String GET_RESTAURANT_FREE_DATES = "/api/restaurant{id}/freeDates";

    public static final String GET_OPEN_HOURS_ALL = "/api/openHours/all";

    public static final String GET_OPEN_HOURS_DAY = "/api/openHours/day={day}";

    public static final String POST_OPEN_HOURS_UPDATE = "/api/openHours/update";

    public static final String POST_RESERVATION_ADD = "/api/reservation/add";

    public static final String POST_RESERVATION_CANCEL = "/api/reservation/cancel/reservationId={id}";

    public static final String DELETE_RESERVATION = "/api/reservation/delete/reservationId={id}";

    public static final String GET_RESERVATIONS_ONE_TABLE = "/api/reservation/forTable/tableId={id}/dateFrom={from}/dateTo={to}";

    public static final String GET_RESERVATIONS_ALL_TABLES = "/api/reservation/all/dateFrom={from}/dateTo={to}";

    public static final String POST_RESERVATION_ADD_BY_CLIENT = "/api/restaurant{id}/reservation/add";

    public static final String GET_TAGS = "/api/tags";

}
