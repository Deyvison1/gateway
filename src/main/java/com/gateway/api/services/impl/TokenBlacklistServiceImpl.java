package com.gateway.api.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.gateway.api.services.ITokenBlacklistService;

import io.micrometer.common.util.StringUtils;

@Service
public class TokenBlacklistServiceImpl implements ITokenBlacklistService {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "uH12jPqz0W+o+fSde9q5Tf0mXEtmCqKnm2Vp3xczxUg=";

	private final RedisTemplate<String, String> redisTemplate;
	private static final String BLACKLIST_PREFIX = "blacklist:";

	public TokenBlacklistServiceImpl(final RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void blacklistToken(String token) {
		DecodedJWT decodedToken = decodedToken(token);
		String key = BLACKLIST_PREFIX + decodedToken.getId();

		// Salva o jti com valor qualquer (ex: "revoked") e TTL igual à expiração do
		// token
		redisTemplate.opsForValue().set(key, "revoked");
	}

	public boolean isBlacklisted(String token) {
		if (StringUtils.isBlank(token)) {
			return false;
		}
		DecodedJWT tokenDecoded = decodedToken(token);

		String key = BLACKLIST_PREFIX + tokenDecoded.getId();
		Boolean hasKey = redisTemplate.hasKey(key);
		return hasKey != null && hasKey;
	}

	public DecodedJWT decodedToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier verifier = JWT.require(alg).build();
		return verifier.verify(token);
	}

	public String resolveToken(ServerHttpRequest request) {
		String bearerToken = request.getHeaders().getFirst("Authorization"); // 👈 Aqui o ajuste!
		if (refreshTokenContainsBearer(bearerToken)) {
			return bearerToken.substring("Bearer ".length());
		}
		return null;
	}

	private static boolean refreshTokenContainsBearer(String refreshToken) {
		return StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer ");
	}
}
