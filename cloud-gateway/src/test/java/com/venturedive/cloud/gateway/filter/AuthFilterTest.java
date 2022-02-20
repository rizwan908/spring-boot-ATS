package com.venturedive.cloud.gateway.filter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import com.venturedive.cloud.gateway.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
class AuthFilterTest {

	@Mock
	private ServerWebExchange exchange;

	@Mock
	private ServerHttpRequest request;

	@Mock
	private GatewayFilterChain chain;

	private AuthFilter authFilter = new AuthFilter();
	private MockServerHttpResponse response = new MockServerHttpResponse();

	@BeforeEach
	public void setup() {
		when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
		when(exchange.getRequest()).thenReturn(request);
		when(exchange.getResponse()).thenReturn(response);
	}

	@Test
	void test_headerDoesNotContainAuthorization() {
		when(exchange.getRequest().getHeaders())
				.thenReturn(new HttpHeaders(new MultiValueMapAdapter<>(Map.of("test", List.of("invalid")))));
		assertDoesNotThrow(() -> authFilter.filter(exchange, chain));
	}

	@Test
	void test_headerContainsBadAuthorization() {
		when(exchange.getRequest().getHeaders())
				.thenReturn(new HttpHeaders(
						new MultiValueMapAdapter<>(Map.of(HttpHeaders.AUTHORIZATION, List.of("invalid")))));
		assertDoesNotThrow(() -> authFilter.filter(exchange, chain));
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
