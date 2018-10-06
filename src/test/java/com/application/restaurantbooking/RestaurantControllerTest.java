package com.application.restaurantbooking;

import com.application.restaurantbooking.controllers.RestaurantController;
import com.application.restaurantbooking.controllers.UrlRequests;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.Tag;
import com.application.restaurantbooking.persistence.service.RestaurantService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(restaurantController)
                .build();
    }

    @Test
    public void getTagsTest() throws Exception {
        List<Tag> tags = Arrays.asList(Tag.values());

        mockMvc.perform(get(UrlRequests.GET_TAGS)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(Tag.values().length)))
                .andExpect(jsonPath("$[0]").value(tags.get(0).toString()))
                .andExpect(jsonPath("$[1]").value(tags.get(1).toString()))
                .andExpect(jsonPath("$[2]").value(tags.get(2).toString()));
    }

    public void getAllRestaurantsTest() throws Exception{
        Restaurant restaurant = new Restaurant();
        restaurant.setName("name");
        restaurant.setCity("city");
        restaurant.setPhoneNumber("123");
        restaurant.setStreet("street");
        Mockito.when(restaurantService.getAll()).thenReturn(Collections.singletonList(restaurant));

        mockMvc.perform(get(UrlRequests.GET_SURROUNDING_RESTAURANTS)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurants").isArray())
                .andExpect(jsonPath("$.restaurants", hasSize(1)))
                .andExpect(jsonPath("$.restaurants[0]").value("name"))
                .andExpect(jsonPath("$.restaurants[0].city").value("city"))
                .andExpect(jsonPath("$.restaurants[0].street").value("street"))
                .andExpect(jsonPath("$.restaurants[0].phoneNumber").value("123"));
        Mockito.verify(restaurantService, times(1)).getAll();
        Mockito.verifyNoMoreInteractions(restaurantService);
    }

}
