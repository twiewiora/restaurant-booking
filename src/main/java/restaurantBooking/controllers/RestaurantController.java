package restaurantBooking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restaurantBooking.persistence.service.RestaurantService;

@RestController
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    private final String URL_GET_ALL_RESTAURANTS = "/restaurants/getAll";

    public RestaurantController(){
    }

    @RequestMapping(value = URL_GET_ALL_RESTAURANTS,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getAllTasks() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(restaurantService.getAllRestaurants());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "false";
        }
    }

}
