package com.application.restaurantbooking.persistence.model;

import java.util.stream.Stream;

public enum Tag {
    PIZZA("PIZZA"),
    KEBAB("KEBAB"),
    SUSHI("SUSHI"),
    DUMPLINGS("DUMPLINGS"),
    FAST_FOOD("FAST FOOD"),
    FIT_FOOD("FIT FOOD"),
    SEAFOOD("SEAFOOD"),
    FISH("FISH"),
    BURGER("BURGER"),
    PASTA("PASTA"),
    VEGETARIAN_CUISINE("VEGETARIAN CUISINE"),
    POLISH_CUISINE("POLISH CUISINE"),
    ITALIAN_CUISINE("ITALIAN CUISINE"),
    GREEK_CUISINE("GREEK CUISINE"),
    CHINESE_CUISINE("CHINESE CUISINE"),
    FRENCH_CUISINE("FRENCH CUISINE"),
    SPANISH_CUISINE("SPANISH CUISINE"),
    MEXICAN_CUISINE("MEXICAN CUISINE"),
    AMERICAN_CUISINE("AMERICAN CUISINE"),
    GERMAN_CUISINE("GERMAN CUISINE"),
    HUNGARIAN_CUISINE("HUNGARIAN CUISINE"),
    ASIAN_CUISINE("ASIAN CUISINE");

    private final String name;

    Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Tag getTagByName(String name) {
        return Stream.of(Tag.values()).filter(tag -> tag.getName().equals(name)).findAny().orElse(null);
    }
}
