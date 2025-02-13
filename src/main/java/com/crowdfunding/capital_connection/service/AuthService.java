package com.crowdfunding.capital_connection.service;

import com.crowdfunding.capital_connection.model.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private AccountService accountService;

    public ResponseEntity<Map<String, String>> generateTokens(String username, String credential) {
        // Obtener el providerId del usuario (si existe)
        String providerIdFromOAuth2 = accountService.getProviderIdByUsername(username);

        Authentication authentication;

        if (providerIdFromOAuth2 != null && providerIdFromOAuth2.equals(credential)) {
            // El usuario se autentica con su providerId
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, providerIdFromOAuth2)
            );
        } else {
            // El usuario se autentica con su contraseña
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, credential)
            );
        }

        String subject = authentication.getName();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Long id = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            id = userDetails.getId();
        }

        // Crear el access token
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(instant.plus(1, ChronoUnit.HOURS))
                .issuer("security-service")
                .claim("scope", scope)
                .claim("account_id", id)
                .build();

        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        // Crear el refresh token
        JwtClaimsSet jwtClaimsSetRefresh = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(instant.plus(30, ChronoUnit.DAYS))
                .issuer("security-service")
                .claim("scope", scope)
                .claim("account_id", id)
                .build();

        String jwtRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetRefresh)).getTokenValue();

        Map<String, String> idToken = new HashMap<>();
        idToken.put("access_token", jwtAccessToken);
        idToken.put("refresh_token", jwtRefreshToken);

        return new ResponseEntity<>(idToken, HttpStatus.OK);
    }
}
