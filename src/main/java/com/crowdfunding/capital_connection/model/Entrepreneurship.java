package com.crowdfunding.capital_connection.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Entrepreneurship {

    private Long id;
    private String name;
    private ArrayList<String> images;
    private String description;
    private ArrayList<String> videos;
    private BigDecimal goal;
    private String category;
    private BigDecimal collected;
    private Boolean activated;


    public Entrepreneurship(String name, String description, BigDecimal goal, String category, BigDecimal collected, Boolean activated) {
        this.name = name;
        this.images = new ArrayList<>();
        this.description = description;
        this.videos = new ArrayList<>();
        this.goal = goal;
        this.category = category;
        this.collected = collected;
        this.activated = activated;
    }

    public Entrepreneurship() {
        this.id = null;
        this.name = null;
        this.images = new ArrayList<>();
        this.description = null;
        this.videos = new ArrayList<>();
        this.goal = null;
        this.category = null;
        this.collected = null;
        this.activated = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrepreneurship that = (Entrepreneurship) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(images, that.images) && Objects.equals(description, that.description) && Objects.equals(videos, that.videos) && Objects.equals(goal, that.goal) && Objects.equals(category, that.category) && Objects.equals(collected, that.collected) && Objects.equals(activated, that.activated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, images, description, videos, goal, category, collected, activated);
    }
}
