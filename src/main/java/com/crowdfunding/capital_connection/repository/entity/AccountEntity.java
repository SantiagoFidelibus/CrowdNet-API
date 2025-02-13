package com.crowdfunding.capital_connection.repository.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EntrepreneurshipEntity> entrepreneurships = new ArrayList<>();

    @NotBlank(message = "Username must not be blank")
    @Column(nullable = false, length = 75, unique = true)
    private String username;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = true, length = 1024, unique = true)
    private String providerId;

    @NotBlank(message = "Email must not be blank")
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Surname must not be blank")
    @Column(nullable = false, length = 50)
    private String surname;

    @NotNull(message = "Date of birth must not be null")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotNull(message = "Years of experience must not be null")
    @PositiveOrZero(message = "Years of experience must be zero or a positive number")
    @Column(nullable = false)
    private Integer yearsOfExperience;

    @NotBlank(message = "Industry must not be blank")
    @Column(nullable = false, length = 50)
    private String industry;

    @NotNull(message = "Wallet must not be null")
    @PositiveOrZero(message = "Wallet must be zero or a positive number")
    @Column(nullable = false)
    private BigDecimal wallet;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id", unique = true, nullable = true)

    private AddressEntity address;


    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonationEntity> donations = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "account_favorites",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "entrepreneurship_id")
    )
    @JsonManagedReference // Serializa esta relación en la entidad principal
    private List<EntrepreneurshipEntity> favoriteEntrepreneurships = new ArrayList<>();


    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();

    private Boolean isActivated=true;

    public AccountEntity() {
    }

    public AccountEntity(Long id, String username, String password, String email, String name, String surname, LocalDate dateOfBirth, Integer yearsOfExperience, String industry, BigDecimal wallet, AddressEntity address, String providerId) {
        this.id = id;
        this.entrepreneurships = new ArrayList<>();
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.yearsOfExperience = yearsOfExperience;
        this.industry = industry;
        this.wallet = wallet;
        this.address = address;
        this.donations = new ArrayList<>();
        this.favoriteEntrepreneurships = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.isActivated = true;
        this.providerId = providerId;
    }

    public void deactivate() {
        this.isActivated = false;
    }
}
