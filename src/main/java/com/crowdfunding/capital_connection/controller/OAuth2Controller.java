package com.crowdfunding.capital_connection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@EnableWebSecurity
public class OAuth2Controller {

    @Autowired
    private JwtEncoder jwtEncoder;
   @RequestMapping("/")
   public String home() {
       return "welcome to CrowdNet API";
   }
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }



    @GetMapping("/oauth2/callback")
    public ResponseEntity<Map<String, String>> oauth2Callback(@AuthenticationPrincipal OAuth2User user) {
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");

        // Generar JWT
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(instant)
                .expiresAt(instant.plus(30, ChronoUnit.SECONDS))
                .issuer("security-service")
                .claim("name", name)
                .build();

        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtAccessToken);
        return ResponseEntity.ok(tokens);
    }
}