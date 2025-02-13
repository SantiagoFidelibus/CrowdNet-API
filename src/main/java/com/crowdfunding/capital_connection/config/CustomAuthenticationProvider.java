package com.crowdfunding.capital_connection.config;


import com.crowdfunding.capital_connection.model.security.CustomUserDetails;
import com.crowdfunding.capital_connection.service.AccountService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, AccountService accountService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String credential = authentication.getCredentials().toString();

        // Obtener usuario de la base de datos
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        // Verificar si el usuario tiene un providerId asociado
        String providerIdFromDB = accountService.getProviderIdByUsername(username);

        if (providerIdFromDB != null) {
            // OAuth2 Login (Autenticación con providerId)
            if (!providerIdFromDB.equals(credential)) {
                throw new BadCredentialsException("Proveedor de autenticación incorrecto.");
            }
            return new UsernamePasswordAuthenticationToken(userDetails, credential, userDetails.getAuthorities());
        }

        // Login con contraseña tradicional
        if (!passwordEncoder.matches(credential, userDetails.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, credential, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
