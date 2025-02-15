package com.crowdfunding.capital_connection.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "reviews")
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Stars must not be null")
    @PositiveOrZero(message = "Stars must be positive")
    @Column(nullable = false)
    private float stars;

    @NotBlank(message = "Review text must not be blank")
    @Column(nullable = false, length = 500)
    private String reviewText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false) // Relación con AccountEntity
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "entrepreneurship_id", referencedColumnName = "id", nullable = false, updatable = false)
    private EntrepreneurshipEntity entrepreneurship;

    private Boolean isActivated=true;

    public ReviewEntity() {}

    public ReviewEntity(Long id, float stars, String reviewText, AccountEntity account, EntrepreneurshipEntity entrepreneurship) {
        this.id = id;
        this.stars = stars;
        this.reviewText = reviewText;
        this.account = account;
        this.entrepreneurship = entrepreneurship;
        this.isActivated = true;

    }

    public void deactivate() {
        this.isActivated = false;
    }
}
