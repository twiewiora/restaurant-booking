package com.application.restaurantbooking;

import com.application.restaurantbooking.utils.geocoding.GeocodeUtil;
import com.application.restaurantbooking.utils.geocoding.Localization;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

public class GeocodeUtilTest {

    private GeocodeUtil geocodeUtil;

    public GeocodeUtilTest() {
        this.geocodeUtil = new GeocodeUtil();
        ReflectionTestUtils.setField(geocodeUtil, "apiKey", "892c74bbd90306");
    }

    @Test
    public void getLocalizationByAddressTest() {
        try {
            Localization localization = geocodeUtil.getLocalizationByAddress("Kraków", "Kawiory", "21");
            Localization expectedLocalization = new Localization( 50.0680966, 19.9125399);
            assertEquals(expectedLocalization, localization);
            Thread.sleep(500);
            localization = geocodeUtil.getLocalizationByAddress("Kraków", "Karmelicka", "6");
            expectedLocalization = new Localization(50.0636734, 19.9325239);
            assertEquals(expectedLocalization, localization);
            Thread.sleep(500);
            localization = geocodeUtil.getLocalizationByAddress("Kraków", "Rynek Główny", "46");
            expectedLocalization = new Localization(50.0622651, 19.9388086);
            assertEquals(expectedLocalization, localization);
            Thread.sleep(500);
            localization = geocodeUtil.getLocalizationByAddress("Nowy Sącz", "Piastowska", "3");
            expectedLocalization = new Localization(49.6249586, 20.6894017);
            assertEquals(expectedLocalization, localization);
            Thread.sleep(500);
            localization = geocodeUtil.getLocalizationByAddress("Nowy Sącz", "Jagiellońska", "11");
            expectedLocalization = new Localization(49.6237992, 20.6923289);
            assertEquals(expectedLocalization, localization);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getCityByLocalizationTest() {
        try {
            Localization localization = new Localization(49.6237992, 20.6923289);
            assertEquals("Nowy Sacz", geocodeUtil.getCityByLocalization(localization));
            Thread.sleep(500);
            localization = new Localization(49.6249586, 20.6894017);
            assertEquals("Nowy Sacz", geocodeUtil.getCityByLocalization(localization));
            Thread.sleep(500);
            localization = new Localization(50.0622651, 19.9388086);
            assertEquals("Krakow", geocodeUtil.getCityByLocalization(localization));
            Thread.sleep(500);
            localization = new Localization(50.0636734, 19.9325239);
            assertEquals("Krakow", geocodeUtil.getCityByLocalization(localization));
            Thread.sleep(500);
            localization = new Localization(50.0636734, 19.9325239);
            assertEquals("Krakow", geocodeUtil.getCityByLocalization(localization));
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
