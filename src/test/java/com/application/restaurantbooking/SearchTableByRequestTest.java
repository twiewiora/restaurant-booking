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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchTableByRequestTest {

    private TableSearcher tableSearcher;

    private Restaurant restaurant;

    private RestaurantTable table1;

    private RestaurantTable table2;

    private RestaurantTable table3;

    private RestaurantTable table4;

    private RestaurantTable table5;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");

    public SearchTableByRequestTest() {
        tableSearcher = new TableSearcher();

        restaurant = new Restaurant();
        Set<RestaurantTable> tables = new HashSet<>();
        table1 = createRestaurantTable(restaurant, 10);
        table2 = createRestaurantTable(restaurant, 7);
        table3 = createRestaurantTable(restaurant, 5);
        table4 = createRestaurantTable(restaurant, 3);
        table5 = createRestaurantTable(restaurant, 2);
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        tables.add(table4);
        tables.add(table5);
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

    private Reservation createReservation(RestaurantTable table, String date, Integer length, Integer places) throws ParseException {
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
    public void availableTermTest1() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_18:00", 120, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_18:00", 60, 5));
            table3.getReservation().add(createReservation(table3, "2018-06-15_18:30", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_20:00"), 60, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(1, result.size());
        assertTrue(result.contains(table1));
    }

    @Test
    public void availableTermTest2() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_18:00", 120, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_18:00", 60, 5));
            table3.getReservation().add(createReservation(table3, "2018-06-15_18:30", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_20:00"), 60, 11);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(2, result.size());
        assertTrue(result.contains(table1));
        assertTrue(result.contains(table5));
    }

    @Test
    public void availableTermTest3() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_18:00", 120, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_18:00", 60, 5));
            table3.getReservation().add(createReservation(table3, "2018-06-15_18:30", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-16_18:00"), 120, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(1, result.size());
        assertTrue(result.contains(table1));
    }

    @Test
    public void availableTermTest4() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_18:00", 120, 9));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_18:00"), 120, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(2, result.size());
        assertTrue(result.contains(table2));
        assertTrue(result.contains(table5));
    }

    @Test
    public void availableTermTest5() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_17:00", 120, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_17:30", 60, 5));
            table5.getReservation().add(createReservation(table5, "2018-06-15_19:30", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_18:30"), 120, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(2, result.size());
        assertTrue(result.contains(table2));
        assertTrue(result.contains(table4));
    }

    @Test
    public void availableTermTest6() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_17:00", 120, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_17:30", 60, 5));
            table5.getReservation().add(createReservation(table5, "2018-06-15_20:30", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_18:30"), 120, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(2, result.size());
        assertTrue(result.contains(table2));
        assertTrue(result.contains(table5));
    }

    @Test
    public void availableTermTest7() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_17:00", 120, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_17:30", 60, 5));
            table5.getReservation().add(createReservation(table5, "2018-06-15_19:00", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_17:30"), 120, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(0, result.size());
    }

    @Test
    public void availableTermTest8() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_17:00", 120, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_17:30", 120, 5));
            table5.getReservation().add(createReservation(table5, "2018-06-15_20:30", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_18:30"), 120, 8);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(2, result.size());
        assertTrue(result.contains(table3));
        assertTrue(result.contains(table4));
    }

    @Test
    public void availableTermTest9() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_17:00", 40, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_17:30", 120, 5));
            table5.getReservation().add(createReservation(table5, "2018-06-15_20:30", 60, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_18:40"), 120, 8);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(1, result.size());
        assertTrue(result.contains(table1));
    }

    @Test
    public void availableTermTest10() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_18:00", 40, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_18:30", 120, 5));
            table5.getReservation().add(createReservation(table5, "2018-06-15_21:30", 30, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_18:30"), 120, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(3, result.size());
        assertTrue(result.contains(table3));
        assertTrue(result.contains(table4));
        assertTrue(result.contains(table5));

    }

    @Test
    public void availableTermTest11() {
        TableSearcherRequest request = null;
        try {
            table1.getReservation().add(createReservation(table1, "2018-06-15_18:00", 40, 9));
            table2.getReservation().add(createReservation(table2, "2018-06-15_18:30", 120, 5));
            table5.getReservation().add(createReservation(table5, "2018-06-15_21:30", 30, 4));
            request = new TableSearcherRequest(sdf.parse("2018-06-15_18:30"), 120, 8);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<RestaurantTable> result = tableSearcher.searchTableByRequest(restaurant, request);
        assertEquals(2, result.size());
        assertTrue(result.contains(table3));
        assertTrue(result.contains(table4));
    }

}
