package com.venturedive.ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venturedive.ticket.entity.Ticket;
import com.venturedive.ticket.service.TicketService;

import reactor.core.publisher.Mono;

@RestController
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@PreAuthorize("hasAuthority('ticket') or hasAuthority('admin')")
	@GetMapping("/ticket")
	public Mono<ResponseEntity<List<Ticket>>> getDeliveryTickets() {
		return Mono.just(ResponseEntity.ok().body(ticketService.getTickets()));
	}
}
