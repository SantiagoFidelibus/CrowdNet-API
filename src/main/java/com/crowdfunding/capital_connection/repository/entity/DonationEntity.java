package com.crowdfunding.capital_connection.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "donations")
@Getter
@Setter
public class DonationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;


    @Column(nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepreneurship_id", referencedColumnName = "id", nullable = false)
    private EntrepreneurshipEntity entrepreneurship;

    private Boolean isActivated=true;

    private String status;

    public DonationEntity() {
    }

    public DonationEntity(Long id, BigDecimal amount, Date date, AccountEntity account, EntrepreneurshipEntity entrepreneurship, Boolean isActivated, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.account = account;
        this.entrepreneurship = entrepreneurship;
        this.isActivated = true;
        this.status = status;
    }

    public void deactivate() {
        this.isActivated = false;
    }

}
