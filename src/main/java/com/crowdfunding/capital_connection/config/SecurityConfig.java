package com.crowdfunding.capital_connection.config;

import com.crowdfunding.capital_connection.controller.dto.AccountDataRequest;
import com.crowdfunding.capital_connection.model.security.CustomUserDetails;
import com.crowdfunding.capital_connection.service.AccountService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private RsaKeysConfig rsaKeysConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;




    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        return new ProviderManager(customAuthenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService(AccountService accountService) {
        return username -> {
            // Obtener la cuenta desde el servicio (devuelve AccountDataRequest)
            AccountDataRequest accountDataRequest = accountService.getAccountData(username);

            // Verificar si la cuenta existe
            if (accountDataRequest == null) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + username);
            }

            // Asignar un rol predeterminado (USER) si no hay roles en AccountDataRequest
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

            // Mapear la cuenta a un CustomUserDetails
            return new CustomUserDetails(
                    accountDataRequest.getId(), // Incluir el ID del usuario
                    accountDataRequest.getUsername(),
                    accountDataRequest.getPassword(),
                    authorities
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AccountService accountService) throws Exception {
        return http
                // Desactivar CSRF (necesario para JWT)
                .csrf(AbstractHttpConfigurer::disable)
                // Configurar CORS (si es necesario)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Permitir acceso a rutas específicas sin autenticación
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/auth/oauth2/token/**",
                                "/oauth2/authorization/google",
                                "/oauth2/authorization/google?prompt=select_account",
                                "/oauth2/callback",
                                "/callback",
                                "/auth/oauth2/login_google",
                                "/accounts",
                                "/auth/oauth2/token",
                                "/entrepreneurships/**",
                                "/auth/cookie",
                                "/accounts/{accountid}/donations/{id}"  // Permitimos todas las rutas de donaciones con cualquier id
// Permitir acceso a esta ruta sin login
                        ).permitAll() // Permitir login, token y callback
                        .anyRequest().authenticated() // Requiere autenticación para otras rutas
                )

                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            // Invalidar sesión y limpiar autenticación previa
                            request.getSession().invalidate();
                            SecurityContextHolder.clearContext();

                            // Obtener el código de autorización
                            String code = request.getParameter("code");
                            System.out.println("Código de autorización recibido: " + code); // Depuración

                            String origin = request.getHeader("Origin");
                            String frontendUrl;

                            if ("https://capital-connection.onrender.com".equals(origin)) {
                                frontendUrl = "https://capital-connection.onrender.com";
                            } else {
                                frontendUrl = "http://localhost:4200"; // Por defecto, localhost
                            }

                            // Manejar el caso donde no se recibe el código de autorización
                            if (code == null) {
                                System.out.println("Error: No se recibió el código de autorización"); // Depuración
                                response.sendRedirect(frontendUrl + "/login?error=no_code");
                                return;
                            }
                            // Redirigir al frontend con el código de autorización
                            String redirectUrl = frontendUrl + "/callback?code=" + code;
                            response.sendRedirect(redirectUrl);
                        })
                )

                // Configurar formularios de login
                .formLogin(Customizer.withDefaults())
                // Habilitar el manejo de JWT en el servidor de recursos
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt) // Configuración JWT
                // Configuración de logout (borrar JSESSIONID y cerrar sesión)
                .logout(logout -> logout
                        .invalidateHttpSession(true)    // Invalida la sesión HTTP
                        .clearAuthentication(true)      // Limpia la autenticación
                        .deleteCookies("JSESSIONID")    // Elimina la cookie JSESSIONID
                        .logoutUrl("/logout")           // URL de logout
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200","https://capital-connection.onrender.com")); // Permitir solicitudes desde el frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Permitir métodos HTTP
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
@Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeysConfig.publicKey()).build();
    }
    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeysConfig.publicKey()).privateKey(rsaKeysConfig.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);

    }
}
