package com.crowdfunding.capital_connection.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthRequest {
    private String username;
    private String providerID;
    private boolean withRefreshToken;

}