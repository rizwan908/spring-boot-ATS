package com.venturedive.monitorservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.venturedive.monitorservice.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	Ticket findByDeliveryIdAndIsActiveTrue(int deliveryId);
}
