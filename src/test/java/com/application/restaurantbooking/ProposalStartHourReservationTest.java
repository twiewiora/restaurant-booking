package com.application.restaurantbooking;

import com.application.restaurantbooking.persistence.builder.OpenHoursBuilder;
import com.application.restaurantbooking.persistence.builder.ReservationBuilder;
import com.application.restaurantbooking.persistence.builder.RestaurantTableBuilder;
import com.application.restaurantbooking.persistence.model.Reservation;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.RestaurantTable;
import com.application.restaurantbooking.utils.TableSearcher;
import com.application.restaurantbooking.utils.TableSearcherRequest;
import org.junit.After;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProposalStartHourReservationTest {

    private TableSearcher tableSearcher;

    private Restaurant restaurant;

    private RestaurantTable table1;

    private RestaurantTable table2;

    private RestaurantTable table3;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");

    private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    public ProposalStartHourReservationTest() {
        tableSearcher = new TableSearcher();

        restaurant = new Restaurant();
        Set<RestaurantTable> tables = new HashSet<>();
        table1 = createRestaurantTable(restaurant, 7);
        table2 = createRestaurantTable(restaurant, 5);
        table3 = createRestaurantTable(restaurant, 2);
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        restaurant.setRestaurantTables(tables);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            restaurant.getOpenHoursMap().put(DayOfWeek.MONDAY, new OpenHoursBuilder()
                    .openHour(sdf.parse("18:00")).closeHour(sdf.parse("22:00")).isClose(true).build());
            restaurant.getOpenHoursMap().put(DayOfWeek.TUESDAY, new OpenHoursBuilder()
                    .openHour(sdf.parse("18:00")).closeHour(sdf.parse("22:00")).isClose(true).build());
            restaurant.getOpenHoursMap().put(DayOfWeek.WEDNESDAY, new OpenHoursBuilder()
                    .openHour(sdf.parse("18:00")).closeHour(sdf.parse("22:00")).isClose(true).build());
            restaurant.getOpenHoursMap().put(DayOfWeek.THURSDAY, new OpenHoursBuilder()
                    .openHour(sdf.parse("18:00")).closeHour(sdf.parse("22:00")).isClose(true).build());
            restaurant.getOpenHoursMap().put(DayOfWeek.FRIDAY, new OpenHoursBuilder()
                    .openHour(sdf.parse("18:00")).closeHour(sdf.parse("22:00")).isClose(false).build());
            restaurant.getOpenHoursMap().put(DayOfWeek.SATURDAY, new OpenHoursBuilder()
                    .openHour(sdf.parse("18:00")).closeHour(sdf.parse("22:00")).isClose(false).build());
            restaurant.getOpenHoursMap().put(DayOfWeek.SUNDAY, new OpenHoursBuilder()
                    .openHour(sdf.parse("18:00")).closeHour(sdf.parse("22:00")).isClose(false).build());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private RestaurantTable createRestaurantTable(Restaurant restaurant, Integer maxPlaces) {
        return new RestaurantTableBuilder()
                .restaurant(restaurant)
                .maxPlaces(maxPlaces)
                .build();
    }

    private Reservation createReservation(RestaurantTable table, Date date, Integer length, Integer places) {
        return new ReservationBuilder()
                .restaurantTable(table)
                .reservationDate(date)
                .reservationLength(length)
                .reservedPlaces(places)
                .build();
    }

    @After
    public void cleanReservations() {
        restaurant.getRestaurantTables().forEach(table -> table.getReservation().clear());
    }

    @Test
    public void proposalTermsTest1() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, sdf.parse("2018-06-15_18:00"), 2, 7));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_18:00"), 1, 5));
            table3.getReservation().add(createReservation(table3, sdf.parse("2018-06-15_18:30"), 1, 2));
            request = new TableSearcherRequest(date.parse("2018-06-15"), 1, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(3, result.size());
        assertTrue(result.contains("20:00"));
        assertTrue(result.contains("20:30"));
        assertTrue(result.contains("21:00"));
    }

    @Test
    public void proposalTermsTest2() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, sdf.parse("2018-06-15_18:00"), 2, 7));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_18:00"), 1, 5));
            table3.getReservation().add(createReservation(table3, sdf.parse("2018-06-15_18:30"), 1, 2));
            request = new TableSearcherRequest(date.parse("2018-06-14"), 2, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(5, result.size());
        assertTrue(result.contains("18:00"));
        assertTrue(result.contains("18:30"));
        assertTrue(result.contains("19:00"));
        assertTrue(result.contains("19:30"));
        assertTrue(result.contains("20:00"));
    }

    @Test
    public void proposalTermsTest3() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, sdf.parse("2018-06-15_18:00"), 1, 7));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_18:00"), 1, 5));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_19:00"), 1, 5));
            table3.getReservation().add(createReservation(table3, sdf.parse("2018-06-15_18:30"), 1, 2));
            request = new TableSearcherRequest(date.parse("2018-06-15"), 1, 2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(5, result.size());
        assertTrue(result.contains("19:00"));
        assertTrue(result.contains("19:30"));
        assertTrue(result.contains("20:00"));
        assertTrue(result.contains("20:30"));
        assertTrue(result.contains("21:00"));
    }

    @Test
    public void proposalTermsTest4() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, sdf.parse("2018-06-15_18:00"), 1, 7));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_18:00"), 1, 5));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_19:00"), 1, 5));
            table3.getReservation().add(createReservation(table3, sdf.parse("2018-06-15_18:30"), 1, 2));
            request = new TableSearcherRequest(date.parse("2018-06-15"), 3, 4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(1, result.size());
        assertTrue(result.contains("19:00"));
    }

    @Test
    public void proposalTermsTest5() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, sdf.parse("2018-06-15_18:00"), 1, 7));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_18:00"), 1, 5));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_19:00"), 1, 5));
            table3.getReservation().add(createReservation(table3, sdf.parse("2018-06-15_18:30"), 1, 2));
            request = new TableSearcherRequest(date.parse("2018-06-15"), 2, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(2, result.size());
        assertTrue(result.contains("19:30"));
        assertTrue(result.contains("20:00"));
    }

    @Test
    public void proposalTermsTest6() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, sdf.parse("2018-06-15_18:00"), 1, 7));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_18:00"), 1, 5));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-15_19:00"), 1, 5));
            table3.getReservation().add(createReservation(table3, sdf.parse("2018-06-15_18:30"), 1, 2));
            request = new TableSearcherRequest(date.parse("2018-06-15"), 3, 11);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(0, result.size());
    }

    @Test
    public void proposalTermsTest7() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, sdf.parse("2018-06-17_18:00"), 4, 7));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-17_18:00"), 1, 5));
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-17_19:00"), 2, 5));
            table3.getReservation().add(createReservation(table3, sdf.parse("2018-06-17_18:30"), 3, 2));
            request = new TableSearcherRequest(date.parse("2018-06-17"), 1, 5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(1, result.size());
        assertTrue(result.contains("21:00"));
        try {
            table2.getReservation().add(createReservation(table2, sdf.parse("2018-06-17_21:00"), 1, 5));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result = tableSearcher.getProposalStartHourReservation(restaurant, request);
        assertEquals(0, result.size());
    }
}
