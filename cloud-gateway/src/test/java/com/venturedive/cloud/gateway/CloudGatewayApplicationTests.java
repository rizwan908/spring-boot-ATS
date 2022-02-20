package com.venturedive.cloud.gateway;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudGatewayApplicationTests {

	private WebTestClient client;

	@BeforeEach
	public void setup() {
		client = WebTestClient.bindToServer().baseUrl("http://localhost:9191").build();
	}

	@Test
	public void pathRouteWorks() {
		client.post().uri("/login").body(Mono.just(Map.of("username", "rizwan", "password", "rizwan")), Map.class)
				.exchange().expectStatus().is2xxSuccessful();
	}

	/*
	 * More Integration test cases can be added here
	 */
}
