package com.application.restaurantbooking.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.*;

@Entity
@Table(name = "restaurant")
@Indexed
@AnalyzerDef(name = "nameAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
                })
        })
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "restaurant_seq")
    @SequenceGenerator(name = "restaurant_seq", sequenceName = "restaurant_seq", allocationSize = 1)
    private Long id;

    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "nameAnalyzer")
    private String name;

    @Field(termVector = TermVector.YES)
    @Analyzer(definition = "nameAnalyzer")
    private String city;

    private String street;

    private String streetNumber;

    private String phoneNumber;

    private String website;

    private Double longitude;

    private Double latitude;

    @Enumerated(EnumType.STRING)
    private Price price;

    @Transient
    private Double priority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restorer_id", nullable = false)
    private Restorer restorer;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", orphanRemoval = true)
    private Set<RestaurantTable> restaurantTables = new HashSet<>();

    @MapKeyClass(value = DayOfWeek.class)
    @MapKeyEnumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = OpenHours.class)
    private Map<DayOfWeek, OpenHours> openHoursMap = new EnumMap<>(DayOfWeek.class);

    public Restaurant(){
        // empty constructor for hibernate
    }

    public Set<Price> getRestaurantPrice() {
        if (price == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(price);
    }

    public void setRestaurantPrice(Collection<Price> prices) {
        if (prices != null && !prices.isEmpty()) {
            this.price = prices.stream().findAny().orElse(null);
        }
    }

}
