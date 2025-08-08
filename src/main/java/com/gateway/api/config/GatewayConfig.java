package com.gateway.api.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.gateway.api.services.ITokenBlacklistService;

import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

	@Value("${services.plans}")
	private String plansServiceUri;

	@Value("${services.auth}")
	private String authServiceUri;

	@Value("${internal.token}")
	private String secretKey;

	@Value("${internal.key}")
	private String key;

	private final ITokenBlacklistService blacklistService;

	public GatewayConfig(ITokenBlacklistService blacklistService) {
		this.blacklistService = blacklistService;
	}

	private static final String ALL_PARAMS = "/**";

	// PLANS - CATEGORIES
	private static final String CATEGORIES_PATH = "/api/categories";
	private static final String CATEGORIES_GET_ALL_PATH = "/api/categories/get-all";
	private static final String CATEGORIES_IMPORT_PATH = "/api/categories/import";
	private static final String CATEGORIES_EXPORT_PATH = "/api/categories/export-page";

	// AUTH
	private static final String AUTH_CREATE_USER_PATH = "/auth";
	private static final String AUTH_REFRESH_TOKEN_PATH = "/auth/refresh";
	private static final String AUTH_SIGNI_PATH = "/auth/signin";
	private static final String AUTH_LOGOUT_PATH = "/auth/logout";

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()

				// CATEGORIES
				.route("get-categories",
						r -> r.method(HttpMethod.GET).and().path(CATEGORIES_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("get-all-categories",
						r -> r.method(HttpMethod.GET).and().path(CATEGORIES_GET_ALL_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("post-category",
						r -> r.method(HttpMethod.POST).and().path(CATEGORIES_PATH + ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))

				.route("put-category",
						r -> r.method(HttpMethod.PUT).and().path(CATEGORIES_PATH + ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))

				.route("delete-category",
						r -> r.method(HttpMethod.DELETE).and().path(CATEGORIES_PATH + ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("delete-category",
						r -> r.method(HttpMethod.GET).and().path(CATEGORIES_IMPORT_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("delete-category",
						r -> r.method(HttpMethod.GET).and().path(CATEGORIES_EXPORT_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))

				// AUTH
				.route("create-user",
						r -> r.method(HttpMethod.POST).and().path(AUTH_CREATE_USER_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))

				.route("signin",
						r -> r.method(HttpMethod.POST).and().path(AUTH_SIGNI_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("refresh-token",
						r -> r.method(HttpMethod.PUT).and().path(AUTH_REFRESH_TOKEN_PATH + ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))

				.route("logout", r -> r.method(HttpMethod.POST).and().path(AUTH_LOGOUT_PATH)
						.filters(f -> f.filter((exchange, chain) -> {
							ServerHttpResponse response = exchange.getResponse();
							response.setStatusCode(HttpStatus.OK);
							// Se quiser resposta vazia, só:
							return response.setComplete();
						})).uri("no://op"))
				.build();
	}
}
