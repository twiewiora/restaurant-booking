package restaurantBooking.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import restaurantBooking.persistence.model.Restaurant;

import java.util.List;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {

    @Query("select restaurant from Restaurant restaurant")
    List<Restaurant> getAllRestaurants();

    @Query("select restaurant from Restaurant restaurant where restaurant.id = :restaurantId")
    Restaurant getRestaurantById(@Param("restaurantId") Long restaurantId);

}
