package com.venturedive.ticket.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.venturedive.ticket.entity.Ticket;
import com.venturedive.ticket.entity.TicketPriority;
import com.venturedive.ticket.service.TicketService;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class TicketControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TicketService ticketService;

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

		mockMvc.perform(MockMvcRequestBuilders.get("/ticket").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].ticketPriority", is("HIGH")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].active", is(true)));

	}

}
