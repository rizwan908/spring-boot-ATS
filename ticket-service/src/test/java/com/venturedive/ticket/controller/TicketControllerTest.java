package com.venturedive.ticket.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.venturedive.ticket.entity.Ticket;
import com.venturedive.ticket.entity.TicketPriority;
import com.venturedive.ticket.service.TicketService;

@ExtendWith(SpringExtension.class)
public class TicketControllerTest {

	@Mock
	TicketService ticketService;

	@InjectMocks
	TicketController ticketController;

	@Test
	void testGetDeliveryTickets() throws Exception {
		Ticket ticket1 = new Ticket();
		ticket1.setActive(true);
		ticket1.setCreated(LocalDateTime.now());
		ticket1.setDeliveryId(1);
		ticket1.setMessage("ticket 1");
		ticket1.setTicketPriority(TicketPriority.CRITICAL);

		Ticket ticket2 = new Ticket();
		ticket1.setActive(true);
		ticket1.setCreated(LocalDateTime.now());
		ticket1.setDeliveryId(2);
		ticket1.setMessage("ticket 2");
		ticket1.setTicketPriority(TicketPriority.HIGH);

		Mockito.when(ticketService.getTickets()).thenReturn(Arrays.asList(ticket1, ticket2));
		List<Ticket> ticketsList = ticketController.getDeliveryTickets().block().getBody();

		assertNotNull(ticketsList);
		assertEquals(2, ticketsList.size());
		assertEquals(ticket1, ticketsList.get(0));
		assertEquals(ticket2, ticketsList.get(1));
	}

	@Test
	void testGetDeliveryTicketsEmptyList() throws Exception {
		Mockito.when(ticketService.getTickets()).thenReturn(Collections.emptyList());
		List<Ticket> ticketsList = ticketController.getDeliveryTickets().block().getBody();

		assertNotNull(ticketsList);
		assertEquals(0, ticketsList.size());
	}
}
