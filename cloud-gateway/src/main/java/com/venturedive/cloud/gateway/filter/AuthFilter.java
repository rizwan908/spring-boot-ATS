package com.venturedive.cloud.gateway.filter;

import java.util.List;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import com.venturedive.cloud.gateway.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
@RefreshScope
public class AuthFilter implements GlobalFilter {

	@Autowired
	private JwtUtils jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		if (request.getHeaders().containsKey("Authorization")) {
			String token = request.getHeaders().getOrEmpty("Authorization").get(0);
			if (token.startsWith("Bearer")) {
				token = token.substring(7);
			}
			try {
				jwtUtil.validateToken(token);
			} catch (AuthenticationException e) {
				ServerHttpResponse response = exchange.getResponse();
				DataBufferFactory factory = response.bufferFactory();
				DataBuffer buffer = factory.wrap(e.getLocalizedMessage().getBytes());
				response.setStatusCode(HttpStatus.BAD_REQUEST);
				return response.writeWith(Mono.just(buffer));
			}

			Claims claims = jwtUtil.getClaims(token);
			List<String> roles = (List<String>) claims.get("roles");
			List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
			UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(claims.getSubject(),
					null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authReq);
			exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id")))
					.header("user", (String) authReq.getPrincipal())
					.header("roles", roles.stream().collect(Collectors.joining(",")))
					.build();
		}

		return chain.filter(exchange);
	}

}