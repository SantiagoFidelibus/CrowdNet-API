package com.crowdfunding.capital_connection.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    private String refreshToken;
    public String getRefreshToken() { return refreshToken; }
}
