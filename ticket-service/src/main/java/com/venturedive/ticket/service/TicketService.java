package com.venturedive.ticket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.venturedive.ticket.entity.Ticket;
import com.venturedive.ticket.repository.TicketRepository;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	public List<Ticket> getTickets() {
		return ticketRepository.findAllByIsActiveTrueOrderByTicketPriorityDesc();
	}
}
