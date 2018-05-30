package com.application.restaurantBooking.controllers;

public class UrlRequests {

    public static final String ERROR = "Error";

    public static final String GET_TABLES_FOR_RESTAURANT = "/table/restaurantId={id}";

    public static final String POST_TABLE_ADD = "/table/add";

    public static final String DELETE_TABLE = "/table/delete/tableId={id}";

    public static final String POST_RESTAURANT_ADD = "/restaurant/add";

    public static final String POST_RESTAURANT_UPDATE = "/restaurant/update";

    public static final String GET_OPEN_HOURS_ALL = "/openHours/restaurantId={id}/all";

    public static final String GET_OPEN_HOURS_DAY = "/openHours/restaurantId={id}/day={day}";

    public static final String POST_OPEN_HOURS_UPDATE = "/openHours/update";

//

    public static final String GET_RESTORER_BY_ID = "/restorer/id{id}";

    public static final String URL_GET_RESERVATIONS_ONE_TABLE = "/reservation/list/tableId={tableId}&dateFrom={dateFrom}&dateTo={dateTo}";

    public static final String URL_GET_RESERVATIONS_ALL_TABLES = "/reservation/list/dateFrom={dateFrom}&dateTo={dateTo}";

    public static final String URL_POST_ADD_RESERVATION = "/reservation/add";

    public static final String URL_DELETE_RESERVATION = "/reservation/delete/reservationId={id}";

}
