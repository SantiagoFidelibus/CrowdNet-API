package com.crowdfunding.capital_connection.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
    private boolean withRefreshToken;
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isWithRefreshToken() { return withRefreshToken; }
}
