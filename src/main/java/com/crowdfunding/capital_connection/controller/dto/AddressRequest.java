package com.crowdfunding.capital_connection.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Payload for address information")
@Getter
@Setter
public class AddressRequest {

    @Schema(description = "ID of the address", example = "1")
    private Long id;

    @Schema(description = "Street name", example = "David Ortega")
    private String street;

    @Schema(description = "Street number", example = "1441")
    private int number;

    @Schema(description = "In which locality the address is located", example = "Mar del Plata")
    private  String locality;

    @Schema(description = "In which province the address is located", example = "Buenos Aires")
    private String province;

    @Schema(description = "Type of address", example = "HOME")
    private String type;

    @Schema(description = "if this data is activated", example = "true")
    private Boolean isActivated;
}
