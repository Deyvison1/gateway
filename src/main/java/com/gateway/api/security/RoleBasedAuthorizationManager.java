package com.gateway.api.security;

import java.util.List;
import java.util.Map;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.gateway.api.services.ITokenBlacklistService;

import reactor.core.publisher.Mono;

@Component
public class RoleBasedAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

	private final ITokenBlacklistService blacklistService;

	private static final Map<String, List<String>> ACCESS_CONTROL_MAP = Map.of("/auth/**", List.of(), // rota pública,
																										// lista vazia
			"/api/**", List.of("ROLE_ADMIN") // rota protegida
	);

	public RoleBasedAuthorizationManager(final ITokenBlacklistService blacklistService) {
		this.blacklistService = blacklistService;
	}

	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
		ServerHttpRequest request = context.getExchange().getRequest();
		String path = request.getPath().pathWithinApplication().value();

		// Filtra as regras que combinam com o path da requisição
		List<String> allowedRoles = ACCESS_CONTROL_MAP.entrySet().stream()
				.filter(entry -> pathMatcher(entry.getKey(), path)).map(Map.Entry::getValue).findFirst()
				.orElse(List.of()); // Se não achar regra, libera ninguém

		// Extrai o token do request reativo
		String token = blacklistService.resolveToken(request); // ✅ Correto

		// ⚠️ Se o path for exatamente "/logout", permite direto
		if (path.equals("/auth/logout")) {
			blacklistService.blacklistToken(token);
			return Mono.just(new AuthorizationDecision(true));
		}

		// Agora que isola a verificação da blacklist
		if (blacklistService.isBlacklisted(token)) {
			// Em contexto reativo, não dá pra simplesmente "interromper" com response.
			// Então você pode negar o acesso aqui:
			return Mono.just(new AuthorizationDecision(false));
		}

		return authentication.filter(Authentication::isAuthenticated).flatMapIterable(auth -> auth.getAuthorities())
				.map(GrantedAuthority::getAuthority).filter(auth -> auth.startsWith("ROLE_"))
				.filter(allowedRoles::contains).hasElements().map(AuthorizationDecision::new)
				.defaultIfEmpty(new AuthorizationDecision(false)); // Aqui era 'true' antes, agora 'false' p/ segurança
	}

	private boolean pathMatcher(String pattern, String path) {
		AntPathMatcher matcher = new AntPathMatcher();
		boolean result = matcher.match(pattern, path);
		return result;
	}

}
