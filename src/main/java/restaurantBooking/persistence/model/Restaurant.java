package restaurantBooking.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "RESTAURANT")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty
    private String name;

    public Restaurant(){
    }

    public Restaurant(String name) {
        this.name = name;
    }

}
