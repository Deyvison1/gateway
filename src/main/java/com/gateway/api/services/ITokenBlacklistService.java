package com.gateway.api.services;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface ITokenBlacklistService {
	void blacklistToken(String token);

	boolean isBlacklisted(String jti);
	
	String resolveToken(ServerHttpRequest request);
}
