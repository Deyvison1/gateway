package com.gateway.api.security.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;

import com.gateway.api.security.RoleBasedAuthorizationManager;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secret;

	private final RoleBasedAuthorizationManager authorizationManager;

	public SecurityConfig(RoleBasedAuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}
	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
	    return http
	        .csrf(ServerHttpSecurity.CsrfSpec::disable)
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        .authorizeExchange(exchanges -> exchanges
	            .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 🔥 Permite preflight
	            .pathMatchers("/api/users/create-user", "/api/auth/signin", "/api/products/get-all").permitAll()
	            .anyExchange().access(authorizationManager)
	        )
	        .oauth2ResourceServer(oauth2 -> oauth2
	            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
	        )
	        .build();
	}
	@Bean
	public Converter<Jwt, ? extends Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(new CustomRoleConverter());
		return new ReactiveJwtAuthenticationConverterAdapter(converter);
	}

	@Bean
	public ReactiveJwtDecoder reactiveJwtDecoder() {
		SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		var a = NimbusReactiveJwtDecoder.withSecretKey(key).build();
		return NimbusReactiveJwtDecoder.withSecretKey(key).build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowedOrigins(List.of("http://localhost:4200"));
	    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    config.setAllowedHeaders(List.of("*"));
	    config.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return source;
	}
}
