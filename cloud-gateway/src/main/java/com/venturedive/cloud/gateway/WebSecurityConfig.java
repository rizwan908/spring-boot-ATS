package com.venturedive.cloud.gateway;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http.csrf().disable().build();
	}

	@Bean
	ServerSecurityContextRepository securityContextRepository() {
		return new ServerSecurityContextRepository() {
			@Override
			public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
				return null;
			}

			@Override
			public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
				return Mono.empty();
				// return serverWebExchange.getPrincipal()
				// 		.defaultIfEmpty(() -> "empty principal")
				// 		.flatMap(principal -> Mono.just(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(
				// 				principal.getName(), null, Collections.emptyList()))));
			}
		};
	}
}