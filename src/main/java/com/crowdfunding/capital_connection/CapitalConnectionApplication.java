package com.crowdfunding.capital_connection;

import com.crowdfunding.capital_connection.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
public class CapitalConnectionApplication {

	public static void main(String[] args) {

		System.out.println("🔍 bd_url: " + System.getenv("bd_url"));
		System.out.println("🔍 bd_username: " + System.getenv("bd_username"));
		System.out.println("🔍 bd_password: " + System.getenv("bd_password"));
		System.out.println("🔍 OAuth2_client-id: " + System.getenv("OAuth2_client-id"));
		System.out.println("🔍 auth-id: " + System.getenv("OAuth2_authorization-uri"));
		System.out.println("🔍 OAuth2_client-secret: " + System.getenv("OAuth2_client-secret"));
		System.out.println("🔍 OAuth2_redirect: " + System.getenv("OAuth2_issuer-uri"));
		System.out.println("🔍 OAuth2_issuer: " + System.getenv("OAuth2_issuer-uri"));
		SpringApplication.run(CapitalConnectionApplication.class, args);

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
