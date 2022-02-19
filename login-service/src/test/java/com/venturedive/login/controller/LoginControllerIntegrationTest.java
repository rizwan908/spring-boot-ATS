package com.venturedive.login.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.venturedive.login.utils.JwtUtils;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerIntegrationTest {

	private WebTestClient webTestClient;
	@Autowired
	private JwtUtils jwtutils;
	
	@BeforeEach
	public void setup() {
		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:9191").build();
	}

	@Test
	public void testLogin() throws URISyntaxException {
		EntityExchangeResult<byte[]> entityExchangeResult = webTestClient.post().uri("/login")
				.body(Mono.just(Map.of("username", "rizwan", "password", "rizwan")), Map.class).exchange().expectStatus()
				.is2xxSuccessful().expectBody().returnResult();
		String token = new String(entityExchangeResult.getResponseBody());
		assertTrue(jwtutils.validateToken(token));	
	}

	@Test
	public void testInvalidLogin() throws URISyntaxException {
		webTestClient.post().uri("/login")
				.body(Mono.just(Map.of("username", "test", "password", "rizwan")), Map.class).exchange().expectStatus()
				.is4xxClientError();
	}
}
