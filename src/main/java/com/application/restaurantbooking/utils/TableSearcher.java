package com.application.restaurantbooking.utils;

import com.application.restaurantbooking.persistence.model.OpenHours;
import com.application.restaurantbooking.persistence.model.Reservation;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.RestaurantTable;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

@Component
public class TableSearcher {

    public TableSearcher() {
        // empty constructor for spring @Component
    }

    public List<RestaurantTable> searchTableByRequest(Restaurant restaurant, TableSearcherRequest request) {
        return searchTheBestTableConfiguration(getFreeTables(restaurant, request), request.getPlaces());
    }

    public List<RestaurantTable> getFreeTables(Restaurant restaurant, TableSearcherRequest request) {
        if (isRequestInOpenHours(restaurant, request)) {
            Date dateFrom = request.getDate();
            Date dateTo = DateUtils.addMinutes(request.getDate(), request.getLength());
            Set<RestaurantTable> freeRestaurantTables = new HashSet<>();
            for (RestaurantTable table : restaurant.getRestaurantTables()) {
                if (table.getReservation().stream().noneMatch(res -> isReservationBlocker(res, dateFrom, dateTo))) {
                    freeRestaurantTables.add(table);
                }
            }
            return new ArrayList<>(freeRestaurantTables);
        } else {
            return Collections.emptyList();
        }
    }

    private boolean isRequestInOpenHours(Restaurant restaurant, TableSearcherRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        OpenHours openHours = restaurant.getOpenHoursMap().get(DayOfWeek.valueOf(sdf.format(request.getDate())
                .toUpperCase()));
        if (openHours.getIsClose()) {
            return false;
        }
        Calendar startReservation = Calendar.getInstance();
        startReservation.setTime(request.getDate());
        startReservation.set(Calendar.DAY_OF_MONTH, 1);
        startReservation.set(Calendar.MONTH, 0);
        startReservation.set(Calendar.YEAR, 1970);
        Calendar endReservation = Calendar.getInstance();
        endReservation.setTime(startReservation.getTime());
        endReservation.add(Calendar.MINUTE, request.getLength());
        if (startReservation.getTime().equals(openHours.getOpenHour())
                || endReservation.getTime().equals(openHours.getCloseHour())) {
            return true;
        }
        return startReservation.getTime().after(openHours.getOpenHour())
                && endReservation.getTime().before(openHours.getCloseHour());
    }

    private boolean isReservationBlocker(Reservation reservation, Date dateFrom, Date dateTo) {
        Date startReservation = reservation.getReservationDate();
        Date endReservation = DateUtils.addMinutes(startReservation, reservation.getReservationLength());

        return startReservation.before(dateFrom) && endReservation.after(dateFrom)
                || startReservation.before(dateTo) && endReservation.after(dateTo)
                || startReservation.before(dateFrom) && endReservation.after(dateTo)
                || startReservation.after(dateFrom) && endReservation.before(dateTo)
                || startReservation.equals(dateFrom) || endReservation.equals(dateTo);
    }

    private List<RestaurantTable> searchTheBestTableConfiguration(List<RestaurantTable> restaurantTables, Integer places){
        if (restaurantTables.isEmpty()) {
            return Collections.emptyList();
        }
        List<RestaurantTable> result = new ArrayList<>();
        List<RestaurantTable> freeTables = new LinkedList<>(restaurantTables);
        freeTables.sort(Comparator.comparing(RestaurantTable::getMaxPlaces));

        int reservedPlaces = 0;
        while (reservedPlaces < places) {
            for (RestaurantTable table : freeTables) {
                if (table.getMaxPlaces() >= places - reservedPlaces) {
                    result.add(table);
                    reservedPlaces += table.getMaxPlaces();
                    break;
                }
            }
            if (reservedPlaces < places) {
                result.add(freeTables.get(freeTables.size() - 1));
                reservedPlaces += freeTables.get(freeTables.size() - 1).getMaxPlaces();
                freeTables.remove(freeTables.get(freeTables.size() - 1));
            }
            if (freeTables.isEmpty()) {
                result = Collections.emptyList();
                break;
            }
        }
        return result;
    }

    public List<String> getProposalStartHourReservation(Restaurant restaurant, TableSearcherRequest request) {
        List<String> proposalStartHours = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar day = Calendar.getInstance();
        day.setTime(request.getDate());
        OpenHours openHours;
        if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            openHours = restaurant.getOpenHoursMap().get(DayOfWeek.SUNDAY);
        } else {
            openHours = restaurant.getOpenHoursMap().get(DayOfWeek.of(day.get(Calendar.DAY_OF_WEEK) - 1));
        }

        if (openHours != null) {
            Date currentHour = convertOpenCloseHourToCurrentDate(day, openHours.getOpenHour());
            Date closeHour = convertOpenCloseHourToCurrentDate(day, openHours.getCloseHour());

            while (currentHour.before(DateUtils.addMinutes(closeHour, -request.getLength()))
                    || currentHour.equals(DateUtils.addMinutes(closeHour, -request.getLength()))) {
                TableSearcherRequest tableRequest = new TableSearcherRequest(currentHour, request.getLength(), request.getPlaces());
                if (!searchTableByRequest(restaurant, tableRequest).isEmpty()) {
                    proposalStartHours.add(sdf.format(currentHour));
                }
                currentHour = DateUtils.addMinutes(currentHour, 30);
            }
        }

        return proposalStartHours;
    }

    private Date convertOpenCloseHourToCurrentDate(Calendar day, Date hour) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(hour);
        day.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
        day.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
        return day.getTime();
    }

    public List<String> getNearHoursReservation(Restaurant restaurant, TableSearcherRequest request) {
        List<String> proposalHours = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (int i = -60; i <= 60; i+=30) {
            TableSearcherRequest tableRequest = new TableSearcherRequest(DateUtils.addMinutes(request.getDate(), -i),
                    request.getLength(), request.getPlaces());
            if (!searchTableByRequest(restaurant, tableRequest).isEmpty()) {
                proposalHours.add(sdf.format(DateUtils.addMinutes(request.getDate(), -i)));
            }
        }
        return proposalHours;
    }
}
