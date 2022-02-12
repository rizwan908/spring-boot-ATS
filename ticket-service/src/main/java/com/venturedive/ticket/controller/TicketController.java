package com.venturedive.ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venturedive.ticket.entity.Ticket;
import com.venturedive.ticket.service.TicketService;

@RestController
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@GetMapping("/ticket")
	public ResponseEntity<List<Ticket>> getDeliveryTickets() {
		return new ResponseEntity<List<Ticket>>(ticketService.getTickets(), HttpStatus.OK);
	}
}
