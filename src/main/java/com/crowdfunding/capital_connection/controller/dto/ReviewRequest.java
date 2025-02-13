package com.crowdfunding.capital_connection.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Payload for review information")
@Getter
@Setter
public class ReviewRequest {

    @Schema(description = "ID of the review", example = "1")
    private Long id;

    @Schema(description = "Star rating of the review", example = "4.5")
    private float stars;

    @Schema(description = "Text of the review", example = "Great project with a promising future!")
    private String reviewText;

    @Schema(description = "ID of the entrepreneurship that gets the review", example = "2")
    private Long id_entrepreneurship;

    @Schema(description = "ID of the user that makes the review", example = "5")
    private Long id_user;


    @Schema(description = "Username of the user that makes the review", example = "5")
    private String username;

    @Schema(description = "if this data is activated", example = "true")
    private Boolean isActivated;

}
