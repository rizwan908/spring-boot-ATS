package com.venturedive.ticket.controller;

import java.net.URISyntaxException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketControllerIntegrationTest {

	private WebTestClient webTestClient;
	
	@BeforeEach
	public void setup() {
		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:9191").build();
	}

	@Test
	public void testGetDeliveryTickets() throws URISyntaxException {
		/*
		 * get token from login api and use it to get tickets
		 */
		EntityExchangeResult<byte[]> entityExchangeResult = webTestClient.post().uri("/login")
				.body(Mono.just(Map.of("username", "rizwan", "password", "rizwan")), Map.class).exchange().expectStatus()
				.is2xxSuccessful().expectBody().returnResult();
		String token = new String(entityExchangeResult.getResponseBody());
		webTestClient.get().uri("/ticket").header("Authorization", "Bearer " + token).exchange().expectStatus()
				.is2xxSuccessful();
		
	}
}
