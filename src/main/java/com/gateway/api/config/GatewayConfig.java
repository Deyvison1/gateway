package com.gateway.api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

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

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()

				// CATEGORIES
				.route("get-categories",
						r -> r.method(HttpMethod.GET).and().path(Routes.CATEGORIES_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("get-categories-complet",
						r -> r.method(HttpMethod.GET).and().path(Routes.CATEGORIES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("get-all-categories",
						r -> r.method(HttpMethod.GET).and().path(Routes.CATEGORIES_GET_ALL_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("post-category",
						r -> r.method(HttpMethod.POST).and().path(Routes.CATEGORIES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))

				.route("put-category",
						r -> r.method(HttpMethod.PUT).and().path(Routes.CATEGORIES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))

				.route("delete-category",
						r -> r.method(HttpMethod.DELETE).and().path(Routes.CATEGORIES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("import-category",
						r -> r.method(HttpMethod.GET).and().path(Routes.CATEGORIES_IMPORT_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("export-category",
						r -> r.method(HttpMethod.GET).and().path(Routes.CATEGORIES_EXPORT_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				
				// PRODUCTS
				.route("get-categories",
						r -> r.method(HttpMethod.GET).and().path(Routes.PRODUCTS_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("get-all-categories",
						r -> r.method(HttpMethod.GET).and().path(Routes.PRODUCTS_GET_ALL_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("get-by-id",
						r -> r.method(HttpMethod.GET).and().path(Routes.PRODUCTS_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				.route("post-category",
						r -> r.method(HttpMethod.POST).and().path(Routes.PRODUCTS_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))

				.route("put-category",
						r -> r.method(HttpMethod.PUT).and().path(Routes.PRODUCTS_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))

				.route("delete-category",
						r -> r.method(HttpMethod.DELETE).and().path(Routes.PRODUCTS_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(plansServiceUri))
				
				// ROLES
				.route("get-roles",
						r -> r.method(HttpMethod.GET).and().path(Routes.ROLES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("delete-role",
						r -> r.method(HttpMethod.DELETE).and().path(Routes.ROLES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				// ROLES
				.route("create-roles",
						r -> r.method(HttpMethod.POST).and().path(Routes.ROLES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				
				.route("update-roles",
						r -> r.method(HttpMethod.PUT).and().path(Routes.ROLES_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				
				.route("get-all",
						r -> r.method(HttpMethod.POST).and().path(Routes.ROLES_GET_ALL_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				// USERS
				
				.route("get-login",
						r -> r.method(HttpMethod.GET).and().path(Routes.USERS_FIND_BY_LOGIN + Routes.ALL_PARAMS)
							.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("find-by-id",
						r -> r.method(HttpMethod.GET).and().path(Routes.USERS_PATH + Routes.ALL_PARAMS)
							.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				
				.route("get-user-id",
						r -> r.method(HttpMethod.GET).and().path(Routes.USERS_FIND_BY_ID + Routes.ALL_PARAMS)
							.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("create-user",
						r -> r.method(HttpMethod.POST).and().path(Routes.USERS_PATH + Routes.ALL_PARAMS)
							.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("update-user",
						r -> r.method(HttpMethod.PUT).and().path(Routes.USERS_PATH + Routes.ALL_PARAMS)
							.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("update-simple-user",
						r -> r.method(HttpMethod.PATCH).and().path(Routes.USERS_PATH + Routes.ALL_PARAMS)
							.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("delete-user",
						r -> r.method(HttpMethod.DELETE).and().path(Routes.USERS_PATH + Routes.ALL_PARAMS)
							.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))

				// AUTH
				.route("signin",
						r -> r.method(HttpMethod.POST).and().path(Routes.AUTH_SIGNI_PATH)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))
				.route("refresh-token",
						r -> r.method(HttpMethod.PUT).and().path(Routes.AUTH_REFRESH_TOKEN_PATH + Routes.ALL_PARAMS)
								.filters(f -> f.addRequestHeader(key, secretKey)).uri(authServiceUri))

				.route("logout", r -> r.method(HttpMethod.POST).and().path(Routes.AUTH_LOGOUT_PATH)
						.filters(f -> f.filter((exchange, chain) -> {
							ServerHttpResponse response = exchange.getResponse();
							response.setStatusCode(HttpStatus.OK);
							// Se quiser resposta vazia, só:
							return response.setComplete();
						})).uri("no://op"))
				.build();
	}
}
