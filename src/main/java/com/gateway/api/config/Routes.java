package com.gateway.api.config;

public final class Routes {

	public static final String ALL_PARAMS = "/**";

	// PLANS - CATEGORIES
	public static final String CATEGORIES_PATH = "/api/categories";
	public static final String CATEGORIES_GET_ALL_PATH = "/api/categories/get-all";
	public static final String CATEGORIES_IMPORT_PATH = "/api/categories/import";
	public static final String CATEGORIES_EXPORT_PATH = "/api/categories/export-page";

	// PLANS - PRODUCTS
	public static final String PRODUCTS_PATH = "/api/products";
	public static final String PRODUCTS_GET_ALL_PATH = "/api/products/get-all";
	
	// AUTH
	public static final String AUTH_CREATE_USER_PATH = "/api/auth/**";
	public static final String AUTH_REFRESH_TOKEN_PATH = "/api/auth/refresh";
	public static final String AUTH_SIGNI_PATH = "/api/auth/signin";
	public static final String AUTH_LOGOUT_PATH = "/api/auth/logout";
}
