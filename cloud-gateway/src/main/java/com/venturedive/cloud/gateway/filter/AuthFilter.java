package com.venturedive.cloud.gateway.filter;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.venturedive.cloud.gateway.utils.JwtUtils;

import reactor.core.publisher.Mono;

@Component
@RefreshScope
public class AuthFilter implements GlobalFilter {

	@Autowired
	private JwtUtils jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
			String token = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);

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
		}
		return chain.filter(exchange);
	}
}