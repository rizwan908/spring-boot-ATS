package com.venturedive.ticket.entity;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.venturedive.ticket.converter.TicketPriorityConverter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Ticket {

	public Ticket(int deliveryId, String message, LocalDateTime date, boolean isActive, TicketPriority ticketPriority) {
		this.deliveryId = deliveryId;
		this.message = message;
		this.created = date;
		this.isActive = isActive;
		this.ticketPriority = ticketPriority;
	}

	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int deliveryId;
	private String message;
	private LocalDateTime created;
	private boolean isActive;
	@Convert(converter = TicketPriorityConverter.class)
	private TicketPriority ticketPriority;
}