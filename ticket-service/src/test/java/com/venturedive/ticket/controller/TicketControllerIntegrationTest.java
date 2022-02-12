package com.venturedive.ticket.controller;

import java.net.URISyntaxException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketControllerIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void testGetDeliveryTickets() throws URISyntaxException {
		/*
		 * get token from login api and use it to get tickets
		 */
		EntityExchangeResult<byte[]> entityExchangeResult = webTestClient.post().uri("http://localhost:9191/login")
				.body(Mono.just(Map.of("username", "test", "password", "test")), Map.class).exchange().expectStatus()
				.is2xxSuccessful().expectBody().returnResult();
		String token = new String(entityExchangeResult.getResponseBody());
		webTestClient.get().uri("http://localhost:9191/ticket").header("Authorization", token).exchange().expectStatus()
				.is2xxSuccessful();
	}
}
