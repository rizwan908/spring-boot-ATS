package com.venturedive.monitorservice.strategies;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.venturedive.monitorservice.entity.CustomerType;
import com.venturedive.monitorservice.entity.DeliveryDetail;
import com.venturedive.monitorservice.entity.TicketPriority;

@Component
@Order(10)
public class ExpectedTimePassedStrategy implements TicketStrategy {

	public static final String MESSAGE = "Expected delivery time has passed";

	@Override
	public TicketPriority ticketPriority(CustomerType customerType) {
		return TicketPriority.CRITICAL;
	}

	@Override
	public String getMessage() {
		return MESSAGE;
	}

	@Override
	public boolean test(DeliveryDetail deliveryDetail) {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime expectedDeliveryTime = deliveryDetail.getExpectedDeliveryTime();
		return Duration.between(currentTime, expectedDeliveryTime).isNegative();
	}

}
