package com.venturedive.cloud.gateway.filter;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.naming.AuthenticationException;

import com.venturedive.cloud.gateway.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
@RefreshScope
public class AuthFilter implements GlobalFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		final List<String> apiEndpoints = List.of("/login");

		Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
				.noneMatch(uri -> r.getURI().getPath().contains(uri));

		if (isApiSecured.test(request)) {
			if (!request.getHeaders().containsKey("Authorization")) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.UNAUTHORIZED);

				return response.setComplete();
			}

			String token = request.getHeaders().getOrEmpty("Authorization").get(0);

			if (token.startsWith("Bearer")) {
				token = token.substring(7);
			}
			try {
				jwtUtil.validateToken(token);
			} catch (AuthenticationException e) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.BAD_REQUEST);

				return response.setComplete();
			}

			Claims claims = jwtUtil.getClaims(token);
			UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(claims.getSubject(),
					null, (Collection<? extends GrantedAuthority>) claims.get("roles"));
			SecurityContextHolder.getContext().setAuthentication(authReq);
			exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id"))).build();
		}

		return chain.filter(exchange);
	}

}