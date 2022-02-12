package com.venturedive.monitorservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.venturedive.monitorservice.entity.DeliveryDetail;
import com.venturedive.monitorservice.entity.DeliveryStatus;
import com.venturedive.monitorservice.entity.Ticket;
import com.venturedive.monitorservice.repository.DeliveryDetailsRepository;
import com.venturedive.monitorservice.repository.TicketRepository;
import com.venturedive.monitorservice.strategies.TicketStrategy;

@Service
public class DeliveryService {

	@Autowired
	private DeliveryDetailsRepository deliveryDetailsRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	List<TicketStrategy> strategies;

	@Scheduled(fixedRate = 120000)
//	@Scheduled(fixedRate = 2000)
	private void worker() {

		List<DeliveryDetail> deliveryDetailsLocal = deliveryDetailsRepository
				.findAllByDeliveryStatusNot(DeliveryStatus.ORDER_DELIVERED);

		processDeliveryDetails(deliveryDetailsLocal);
	}

	public synchronized void processDeliveryDetails(List<DeliveryDetail> deliveryDetailsLocal) {
		/*
		 * more conditions for ticket creation can be done by creating new startegies
		 */
		for (DeliveryDetail deliveryDetail : deliveryDetailsLocal) {
			for (TicketStrategy s : strategies) {
				if (s.test(deliveryDetail)) {
					createUpdateTicket(deliveryDetail, s.createNewTicket(deliveryDetail));
					break;
				}
			}
		}
	}

	private void createUpdateTicket(DeliveryDetail deliveryDetail, Ticket newTicket) {
		Ticket currentTicket = ticketRepository.findByDeliveryIdAndIsActiveTrue(deliveryDetail.getDeliveryId());

		if (currentTicket != null) {
			currentTicket.setTicketPriority(newTicket.getTicketPriority());
			currentTicket.setMessage(newTicket.getMessage());
			currentTicket.setTicketPriority(newTicket.getTicketPriority());
			currentTicket.setCreated(newTicket.getCreated());
			ticketRepository.save(currentTicket);
		} else
			ticketRepository.save(newTicket);
	}
}
