package com.application.restaurantbooking.utils.geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class GeocodeUtil {

    private static final Logger LOGGER = Logger.getLogger(GeocodeUtil.class.getName());

    @Value("${api.geocode.key}")
    private String apiKey;

    public GeocodeUtil() {
        // empty constructor for spring @Component
    }

    public Localization getLocalizationByAddress(String city, String street, String streetNumber) {
        String address = new StringJoiner(" ").add(city).add(street).add(streetNumber).toString();
        Localization restaurantLocalization = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(createUriByAddress(address));
            HttpResponse response = client.execute(request);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                result.append(line);
            }

            restaurantLocalization = getLocalizationFromResultJson(result.toString());

        } catch (IOException | URISyntaxException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        return restaurantLocalization;
    }

    private URI createUriByAddress(String address) throws URISyntaxException {
        return new URIBuilder()
                .setScheme("https")
                .setHost("eu1.locationiq.com")
                .setPath("/v1/search.php")
                .addParameter("format", "json")
                .addParameter("key", apiKey)
                .addParameter("q", address)
                .build();
    }

    private Localization getLocalizationFromResultJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object[] nodeArray = objectMapper.readValue(json, Object[].class);
        LinkedHashMap<String, String> geocode = (LinkedHashMap<String, String>) nodeArray[0];
        Double longitude = Double.valueOf(geocode.get("lon"));
        Double latitude = Double.valueOf(geocode.get("lat"));
        return new Localization(latitude, longitude);
    }

    public String getCityByLocalization(Localization localization) {
        String cityName = null;
        if (localization.getLongitude() == null || localization.getLatitude() == null) {
            return null;
        }
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(createURIByLocalization(localization));
            HttpResponse response = client.execute(request);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                result.append(line);
            }

            cityName = getCityNameFromResultJson(result.toString());

        } catch (IOException | URISyntaxException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        return StringUtils.stripAccents(cityName);
    }

    private URI createURIByLocalization(Localization localization) throws URISyntaxException{
        return new URIBuilder()
                .setScheme("https")
                .setHost("eu1.locationiq.com")
                .setPath("/v1/reverse.php")
                .addParameter("format", "json")
                .addParameter("key", apiKey)
                .addParameter("lat", String.valueOf(localization.getLatitude()))
                .addParameter("lon", String.valueOf(localization.getLongitude()))
                .build();
    }

    private String getCityNameFromResultJson(String json) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, Object> mainNode = (LinkedHashMap<String, Object>) objectMapper.readValue(json, Object.class);
        LinkedHashMap<String, String> addressNode = (LinkedHashMap<String, String>) mainNode.get("address");
        if (addressNode.get("city") != null) {
            return addressNode.get("city");
        } else if (addressNode.get("town") != null) {
            return addressNode.get("town");
        } else if (addressNode.get("village") != null) {
            return addressNode.get("village");
        }
        return null;
    }

}
