package com.crowdfunding.capital_connection.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Address {
    private Long id;
    private String street;
    private int number;
    private  String locality;
    private String   province;
    private String type;

    public Address(Long id, String street, int number, String locality, String province, String type) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.locality = locality;
        this.province = province;
        this.type = type;
    }

    public Address() {
        this.id = null;
        this.street = null;
        this.number = 0;
        this.locality = null;
        this.province = null;
        this.type = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return number == address.number && Objects.equals(id, address.id) && Objects.equals(street, address.street) && Objects.equals(locality, address.locality) && Objects.equals(province, address.province) && Objects.equals(type, address.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, number, locality, province, type);
    }
}
