package com.application.restaurantBooking.utils;

import com.application.restaurantBooking.persistence.model.OpenHours;
import com.application.restaurantBooking.persistence.model.Reservation;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TableSearcher {

    public TableSearcher() {
    }

    public List<RestaurantTable> searchTableByRequest(Restaurant restaurant, TableSearcherRequest request) {
        List<Reservation> reservations = new ArrayList<>();
        restaurant.getRestaurantTables().forEach(table -> reservations.addAll(table.getReservation()));
        Date dateFrom = request.getDate();
        Date dateTo = DateUtils.addHours(request.getDate(), request.getLength());

        Set<RestaurantTable> freeRestaurantTables = reservations.stream()
                .filter(res -> !isReservationBlocker(res, dateFrom, dateTo))
                .map(Reservation::getRestaurantTable)
                .collect(Collectors.toSet());

        return searchTheBestTableConfiguration(freeRestaurantTables, request.getPlaces());
    }

    private boolean isReservationBlocker(Reservation reservation, Date dateFrom, Date dateTo) {
        Date startReservation = reservation.getReservationDate();
        Date endReservation = DateUtils.addHours(startReservation, reservation.getReservationLength());

        return startReservation.before(dateFrom) && endReservation.after(dateFrom)
                || startReservation.before(dateTo) && endReservation.after(dateTo)
                || startReservation.before(dateFrom) && endReservation.after(dateTo)
                || startReservation.after(dateFrom) && endReservation.before(dateTo)
                || startReservation.equals(dateFrom) || endReservation.equals(dateTo);
    }

    private List<RestaurantTable> searchTheBestTableConfiguration(Set<RestaurantTable> restaurantTables, Integer places){
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
        OpenHours openHours = restaurant.getOpenHoursMap().get(DayOfWeek.of(day.get(Calendar.DAY_OF_WEEK) - 1));

        Date currentHour = convertOpenCloseHourToCurrentDate(day, openHours.getOpenHour());
        Date closeHour = convertOpenCloseHourToCurrentDate(day, openHours.getCloseHour());

        while (currentHour.before(DateUtils.addHours(closeHour, -request.getLength()))
                || currentHour.equals(DateUtils.addHours(closeHour, -request.getLength()))) {
            TableSearcherRequest tableRequest = new TableSearcherRequest(currentHour, request.getLength(), request.getPlaces());
            if (!searchTableByRequest(restaurant, tableRequest).isEmpty()) {
                proposalStartHours.add(sdf.format(currentHour));
            }
            currentHour = DateUtils.addMinutes(currentHour, 30);
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
}
