package com.gateway.api.config;

public final class Routes {

	public static final String ALL_PARAMS = "/**";
	
	private static final String PATH_API = "/api";

	// PLANS - CATEGORIES
	public static final String CATEGORIES_PATH = PATH_API + "/categories";
	public static final String CATEGORIES_GET_ALL_PATH = PATH_API + "/categories/get-all";
	public static final String CATEGORIES_IMPORT_PATH = PATH_API + "/categories/import";
	public static final String CATEGORIES_EXPORT_PATH = PATH_API + "/categories/export-page";

	// PLANS - PRODUCTS
	public static final String PRODUCTS_PATH = PATH_API + "/products";
	public static final String PRODUCTS_GET_ALL_PATH = PATH_API + "/products/get-all";
	
	// AUTH
	public static final String AUTH_REFRESH_TOKEN_PATH = PATH_API + "/auth/refresh";
	public static final String AUTH_SIGNI_PATH = PATH_API + "/auth/signin";
	public static final String AUTH_LOGOUT_PATH = PATH_API + "/auth/logout";
	
	// USERS
	public static final String USERS_PATH = PATH_API + "/users";
	public static final String USERS_FIND_BY_LOGIN = PATH_API + "/users/get-login";
	public static final String USERS_FIND_BY_ID = PATH_API + "/users/get-user-id";
	
	public static final String USERS_CREATE_USER_PATH = USERS_PATH + "/create-user";
	
	// ROLES
	public static final String ROLES_PATH = PATH_API + "/roles";
	public static final String ROLES_GET_ALL_PATH = PATH_API + ROLES_PATH + "/get-all";
}
