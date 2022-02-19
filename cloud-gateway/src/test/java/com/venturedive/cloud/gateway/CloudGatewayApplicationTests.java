package com.venturedive.cloud.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudGatewayApplicationTests {

	@LocalServerPort
	int port;
	private WebTestClient client;

	@BeforeEach
	public void setup() {
		client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void pathRouteWorks() {
		client.get().uri("/login")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.consumeWith(result -> {
					assertThat(result.getResponseBody()).isNotEmpty();
				});
	}
}
