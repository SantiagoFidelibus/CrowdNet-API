package com.crowdfunding.capital_connection.repository.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "addresses")
@Getter
@Setter
public class AddressEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String street;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false, length = 100)
    private String locality;

    @Column(nullable = false, length = 100)
    private String province;

    @Column(nullable = false, length = 50)
    private String type;

    @OneToOne(mappedBy = "address")

    private AccountEntity account;

    private Boolean isActivated=true;

    public AddressEntity() {
    }

    public AddressEntity(Long id, String street, int number, String locality, String province, String type, AccountEntity account) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.locality = locality;
        this.province = province;
        this.type = type;
        this.account = account;
        this.isActivated = true;

    }

    public void deactivate() {
        this.isActivated = false;
    }
}
