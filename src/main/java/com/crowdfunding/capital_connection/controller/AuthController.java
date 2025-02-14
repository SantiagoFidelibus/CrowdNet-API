package com.crowdfunding.capital_connection.controller;

import com.crowdfunding.capital_connection.controller.dto.AccountRequest;
import com.crowdfunding.capital_connection.controller.dto.LoginRequest;
import com.crowdfunding.capital_connection.controller.dto.OauthRequest;
import com.crowdfunding.capital_connection.controller.dto.RefreshTokenRequest;
import com.crowdfunding.capital_connection.model.Account;
import com.crowdfunding.capital_connection.model.security.CustomUserDetails;
import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import com.crowdfunding.capital_connection.service.AccountService;
import com.crowdfunding.capital_connection.service.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
@Autowired
private AccountService accountService;
    @Autowired
    private AuthService authService;
    @Autowired
    private TransactionAutoConfiguration.EnableTransactionManagementConfiguration.CglibAutoProxyConfiguration cglibAutoProxyConfiguration;





    // Método para autenticar al usuario y generar el access token
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {

        // Autenticación con username y password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String subject = authentication.getName();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        Long id = null;

        // Obtener el ID del usuario autenticado
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            id = userDetails.getId(); // Obtener el ID del usuario
        }

        // Generar el access token
        Map<String, String> idToken = new HashMap<>();
        Instant instant = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(instant.plus(1, ChronoUnit.HOURS)) // Access token dura 1 minuto
                .issuer("security-service")
                .claim("scope", scope)
                .claim("account_id", id)  // Incluimos el ID del usuario
                .build();

        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        idToken.put("access_token", jwtAccessToken);

        // Generar el refresh token si se requiere
        if (loginRequest.isWithRefreshToken()) {
            JwtClaimsSet jwtClaimsSetRefresh = JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.DAYS)) // Refresh token dura 5 minutos
                    .issuer("security-service")
                    .claim("scope", scope)
                    .claim("account_id", id)
                    .build();

            String jwtRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetRefresh)).getTokenValue();
            idToken.put("refresh_token", jwtRefreshToken);
        }

        return new ResponseEntity<>(idToken, HttpStatus.OK);
    }
    @PostMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (refreshToken == null) {
            return new ResponseEntity<>(Map.of("errorMessage", "El refresh token es requerido"), HttpStatus.UNAUTHORIZED);
        }

        Jwt decodeJWT;
        try {
            decodeJWT = jwtDecoder.decode(refreshToken); // Decodificar el refresh token
        } catch (JwtException e) {
            return new ResponseEntity<>(Map.of("errorMessage", "Token no válido: " + e.getMessage()), HttpStatus.UNAUTHORIZED);
        }

        String subject = decodeJWT.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        String scope = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Long id = null;
        if (userDetails instanceof CustomUserDetails) {
            id = ((CustomUserDetails) userDetails).getId();
        }

        if (id == null) {
            return new ResponseEntity<>(Map.of("errorMessage", "No se pudo obtener el ID del usuario"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Instant now = Instant.now();
        Map<String, String> idToken = new HashMap<>();

        // Generar nuevo access token
        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS)) // El nuevo access token dura 1 hora
                .issuer("security-service")
                .claim("scope", scope)
                .claim("account_id", id)
                .build();

        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();
        idToken.put("access_token", jwtAccessToken);

        // Verificar si el refresh token está próximo a expirar (ej. 30 segundos)
        Instant refreshExpiration = decodeJWT.getExpiresAt();
        long remainingSeconds = ChronoUnit.SECONDS.between(now, refreshExpiration);

        if (remainingSeconds <= 30) {
            JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(now)
                    .expiresAt(now.plus(30, ChronoUnit.DAYS)) // Nuevo refresh token por 30 días
                    .issuer("security-service")
                    .claim("scope", scope)
                    .claim("account_id", id)
                    .build();

            String newRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();
            idToken.put("refresh_token", newRefreshToken);
        }

        return new ResponseEntity<>(idToken, HttpStatus.OK);
    }

    @PostMapping("/oauth2/token")
    public ResponseEntity<?> generateTokenForOAuth2User(@RequestBody Map<String, String> body, HttpServletResponse response) {
        String code = body.get("code");  // Obtener el código del cuerpo de la solicitud
        if (code == null) {
            return ResponseEntity.badRequest().body("Código de autorización no proporcionado");
        }
        // Realizar la solicitud a Google para obtener el token
        String googleUrl = System.getenv("googleUrl");
        String clientId = System.getenv("OAuth2_client-id");
        String clientSecret = System.getenv("OAuth2_client-secret");
        String redirectUri = System.getenv("redirectUriAngular");


        // Debe coincidir con el URI registrado en Google

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Realizar la solicitud POST a Google
            ResponseEntity<Map> responseFromGoogle = restTemplate.postForEntity(googleUrl, requestBody, Map.class);
            Map<String, String> responseBody = responseFromGoogle.getBody();

            // Obtener el access_token de la respuesta
            String accessToken = responseBody.get("access_token");

            // Obtener el perfil del usuario desde Google
            String profileUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
            ResponseEntity<Map> profileResponse = restTemplate.getForEntity(profileUrl, Map.class);
            Map<String, String> profile = profileResponse.getBody();

            String email = profile.get("email");
            String givenName = profile.get("given_name");  // Obtener el nombre
            String familyName = profile.get("family_name");
            String providerId = profile.get("id");

            // Verificar si el correo electrónico está en tu base de datos
            boolean emailExists = accountService.existsByEmail(email); // Implementa esta función

            if (!emailExists) {
                // Si el correo no está en la base de datos, devolver una respuesta para redirigir al frontend

                // Crear la cookie
                Cookie providerIdCookie = new Cookie("provider_id", providerId);
                providerIdCookie.setDomain("capital-connection.onrender.com");  // Usar dominio específico
                providerIdCookie.setPath("/");       // Asegurarse de que sea accesible globalmente
                providerIdCookie.setHttpOnly(false);  // Permitir el acceso desde JavaScript si es necesario
                providerIdCookie.setSecure(true);     // Activar solo en HTTPS
                providerIdCookie.setMaxAge(60 * 60); // Expiración en una hora
                response.addCookie(providerIdCookie);

                response.addCookie(providerIdCookie);

                // Redirigir al frontend
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("redirect", "/signupwgoogle");
                responseMap.put("email", email);

                // Verificar si given_name está presente y no es nulo o vacío
                if (givenName != null && !givenName.isEmpty()) {
                    responseMap.put("given_name", givenName);
                }

                // Verificar si family_name está presente y no es nulo o vacío
                if (familyName != null && !familyName.isEmpty()) {
                    responseMap.put("family_name", familyName);
                }
                responseMap.put("provider_id", providerId);
                // Redirigir al frontend
                return ResponseEntity.ok(responseMap);
            } else {
                Long id = accountService.getAccountIdByEmail(email);
                AccountEntity account = accountService.getAccountById(id);

                if(account.getProviderId() == null) {
                    AccountRequest accAux = new AccountRequest();
                    accAux.setProviderId(providerId);
                    accountService.updateAccountPartial(id,accAux);
                }

                String username = accountService.getUsernameByEmail(email);
                return authService.generateTokens(username, providerId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al obtener el token de Google");
        }
    }

    @PostMapping("/oauth2/login_google")
    public ResponseEntity<Map<String, String>> loginOauth(@RequestBody OauthRequest oauthRequest) {
        return authService.generateTokens(oauthRequest.getUsername(), oauthRequest.getProviderID());
    }


}
