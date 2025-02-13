package com.crowdfunding.capital_connection.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
public class Donation {
   private Long id;
   private BigDecimal amount;
   private Date date;
   private String status;

   public Donation(Long id, BigDecimal amount, Date date, String status) {
      this.id = id;
      this.amount = amount;
      this.date = date;
      this.status = status;
   }

   public Donation() {
      this.id = null;
      this.amount = BigDecimal.ZERO;
   this.date = null;
   this.status = null;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Donation donation = (Donation) o;
      return Objects.equals(id, donation.id) && Objects.equals(amount, donation.amount) && Objects.equals(date, donation.date) && Objects.equals(status, donation.status);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, amount, date, status);
   }
}
