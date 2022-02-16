package com.venturedive.login;

import java.util.List;
import java.util.stream.Collectors;

import com.venturedive.login.service.UserService;
import com.venturedive.login.utils.JwtUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ReactivePreAuthenticatedAuthenticationManager;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private UserService userService;

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http
				.csrf().disable()
				.httpBasic().disable()
				.formLogin().disable()
				.logout().disable()

				.authenticationManager(authenticationManager())
				.securityContextRepository(securityContextRepository())
				.authorizeExchange().pathMatchers("/login").permitAll()
				.and().authorizeExchange().anyExchange().authenticated()
				.and().build();
	}

	@Bean
	ReactivePreAuthenticatedAuthenticationManager authenticationManager() {
		return new ReactivePreAuthenticatedAuthenticationManager(userService);
	}

	@Bean
	ServerSecurityContextRepository securityContextRepository() {
		return new ServerSecurityContextRepository() {
			public static final String AUTH_TYPE = "Bearer ";
			public static final String HEADER_AUTH = "Authorization";

			@Override
			public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
				return Mono.empty();
			}

			@Override
			public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
				ServerHttpRequest request = serverWebExchange.getRequest();

				List<String> headers = request.getHeaders().get(HEADER_AUTH);
				if (CollectionUtils.isEmpty(headers))
					return Mono.empty();

				String authorization = headers.get(0);
				if (StringUtils.isEmpty(authorization))
					return Mono.empty();

				String token = authorization.substring(AUTH_TYPE.length());
				if (StringUtils.isEmpty(token))
					return Mono.empty();

				Claims claims = jwtUtil.getClaims(token);
				List<String> roles = (List<String>) claims.get("roles");
				List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
				return authenticationManager()
						.authenticate(new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities))
						.map(SecurityContextImpl::new);
			}
		};
	}
}