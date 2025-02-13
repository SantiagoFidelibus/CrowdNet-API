package com.crowdfunding.capital_connection.repository.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrepreneurships")
@Getter
@Setter
public class EntrepreneurshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private AccountEntity account;

    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Column(length = 1000)
    private String description;

    @Positive(message = "Goal must be a positive number")
    @Column(nullable = false)
    private BigDecimal goal;

    @Min(value = 0, message = "Collected must be zero or greater")
    @Column(nullable = false)
    private BigDecimal collected;

    @Column(nullable = false)
    private String category;

    @ElementCollection
    @CollectionTable(name = "entrepreneurship_images", joinColumns = @JoinColumn(name = "entrepreneurship_id"))
    @Column(name = "link_image")
    private List<String> images;

    @ElementCollection
    @CollectionTable(name = "entrepreneurship_videos", joinColumns = @JoinColumn(name = "entrepreneurship_id"))
    @Column(name = "link_video")
    private List<String> videos;

    private Boolean isActivated=true;

    @OneToMany(mappedBy = "entrepreneurship", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();


    @ManyToMany(mappedBy = "favoriteEntrepreneurships", fetch = FetchType.LAZY)
    @JsonBackReference // Evita que se serialice la lista en la entidad relacionada
    private List<AccountEntity> usersWhoFavorited = new ArrayList<>();

    @OneToMany(mappedBy = "entrepreneurship", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonationEntity> donations = new ArrayList<>();


    public EntrepreneurshipEntity() {
    }

    public EntrepreneurshipEntity(Long id, AccountEntity account, String name, String description, BigDecimal goal, BigDecimal collected, String category, List<String> images, List<String> videos) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.description = description;
        this.goal = goal;
        this.collected = collected;
        this.category = category;
        this.images = images;
        this.videos = videos;
        this.isActivated = true;
        this.reviews = new ArrayList<>();
        this.usersWhoFavorited = new ArrayList<>();
        this.donations = new ArrayList<>();
    }

    public void deactivate() {
        this.isActivated = false;
    }

    public void addCollectedAmount(BigDecimal amount) {
        this.collected = this.collected.add(amount);
    }


}
