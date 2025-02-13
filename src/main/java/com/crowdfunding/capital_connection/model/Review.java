package com.crowdfunding.capital_connection.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Review {
    private Long id;
    private float stars;
    private String reviewText;

    public Review(Long id, float stars, String reviewText) {
        this.id = id;
        this.stars = stars;
        this.reviewText = reviewText;
    }

    public Review() {
        this.id= null;
        this.stars = 0f;
        this.reviewText = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Float.compare(stars, review.stars) == 0 && Objects.equals(id, review.id) && Objects.equals(reviewText, review.reviewText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stars, reviewText);
    }
}
