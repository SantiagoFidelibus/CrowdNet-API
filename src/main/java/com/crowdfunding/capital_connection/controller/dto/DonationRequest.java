package com.crowdfunding.capital_connection.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Schema(description = "Payload for donation information")
@Getter
@Setter
public class DonationRequest {

    @Schema(description = "ID of donation", example = "1")
    private Long id;

    @Schema(description = "Payment URL")
    private String paymentUrl;

    @Schema(description = "Amount of the donation in USD", example = "10000")
    private BigDecimal amount;

    @Schema(description = "Donation date")
    private Date date;

    @Schema(description = "ID of the entrepreneurship that gets the donation", example = "2")
    private Long id_entrepreneurship;

    @Schema(description = "ID of the user that makes the donation", example = "5")
    private Long id_user;

    @Schema(description = "if this data is activated", example = "true")
    private Boolean isActivated;

    @Schema(description = "STATUS OF PAYMENT", example = "true")
    private String status;

 }
