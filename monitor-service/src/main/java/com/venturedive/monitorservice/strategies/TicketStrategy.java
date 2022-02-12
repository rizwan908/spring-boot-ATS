package com.venturedive.monitorservice.strategies;

import java.time.LocalDateTime;

import com.venturedive.monitorservice.entity.CustomerType;
import com.venturedive.monitorservice.entity.DeliveryDetail;
import com.venturedive.monitorservice.entity.Ticket;
import com.venturedive.monitorservice.entity.TicketPriority;

public interface TicketStrategy {

	public TicketPriority ticketPriority(CustomerType customerType);

	public String getMessage();

	public boolean test(DeliveryDetail deliveryDetail);
	
	default Ticket createNewTicket(DeliveryDetail deliveryDetail) {
		return new Ticket(deliveryDetail.getDeliveryId(), getMessage(), LocalDateTime.now(), true,
				ticketPriority(deliveryDetail.getCustomerType()));
	}
}
