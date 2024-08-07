package com.venturedive.ticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venturedive.ticket.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findAllByIsActiveTrueOrderByTicketPriorityDesc();

}
