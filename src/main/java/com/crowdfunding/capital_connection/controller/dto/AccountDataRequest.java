package com.crowdfunding.capital_connection.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Payload for Account Data")
@Getter
@Setter
public class AccountDataRequest {

    @Schema(description = "ID of the account", example = "1")
    private Long id;
    @Schema(description = "Username of the account", example = "1")
    private String username;
    @Schema(description = "Email of the account", example = "1")
    private String email;
    @Schema(description = "Password crypting", example = "1")
    private String password;



}
