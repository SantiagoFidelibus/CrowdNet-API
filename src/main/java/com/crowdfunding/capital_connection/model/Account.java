package com.crowdfunding.capital_connection.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter

public class Account {

private Long id;
private String username;
private String password;
private String email;
private String name;
private String surname;
private String dateOfBirth;
private Integer yearsOfExperience;
private String industry;
private BigDecimal wallet;


        public Account(Long id, String username, String password, String email, String name, String surname, String dateOfBirth, Integer yearsOfExperience, String industry, BigDecimal wallet) {
                this.id = id;
                this.username = username;
                this.password = password;
                this.email = email;
                this.name = name;
                this.surname = surname;
                this.dateOfBirth = dateOfBirth;
                this.yearsOfExperience = yearsOfExperience;
                this.industry = industry;
                this.wallet = wallet;
        }

        public Account() {
                this.id = null;
                this.username = null;
                this.password = null;
                this.email = null;
                this.name = null;
                this.surname = null;
                this.dateOfBirth = null;
                this.yearsOfExperience = null;
                this.industry = null;
                this.wallet = null;
        }



        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Account account = (Account) o;
                return Objects.equals(id, account.id) && Objects.equals(username, account.username) && Objects.equals(password, account.password) && Objects.equals(email, account.email) && Objects.equals(name, account.name) && Objects.equals(surname, account.surname) && Objects.equals(dateOfBirth, account.dateOfBirth) && Objects.equals(yearsOfExperience, account.yearsOfExperience) && Objects.equals(industry, account.industry) && Objects.equals(wallet, account.wallet);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, username, password, email, name, surname, dateOfBirth, yearsOfExperience, industry, wallet);
        }
}
