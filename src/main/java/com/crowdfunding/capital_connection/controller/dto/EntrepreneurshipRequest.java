package com.crowdfunding.capital_connection.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Payload for entrepreneurship information")
@Getter
@Setter
public class EntrepreneurshipRequest {

    @Schema(description = "ID of the entrepreneurship", example = "1")
    private Long id;

    @Schema(description = "Name of the entrepreneurship", example = "Eco Solutions")
    private String name;

    @Schema(description = "Images of the entrepreneurship saved on Links")
    private List<String> images;

    @Schema(description = "Description of the entrepreneurship", example = "A project focused on eco-friendly solutions.")
    private String description;

    @Schema(description = "Videos of the entrepreneurship saved on Links")
    private List<String> videos;

    @Schema(description = "Funding goal of the entrepreneurship", example = "10000.0")
    private BigDecimal goal;

    @Schema(description = "Category of the entrepreneurship", example = "Health")
    private String category;

    @Schema(description = "Collected of the entrepreneurship", example = "10000.0")
    private BigDecimal collected;

    @Schema(description = "activated of the entrepreneurship", example = "true")
    private Boolean isActivated;

    @Schema(description = "Id of the account creator", example = "true")
    private Long id_account;

}
