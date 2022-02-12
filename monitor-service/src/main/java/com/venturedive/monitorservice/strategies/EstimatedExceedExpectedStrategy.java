package com.venturedive.monitorservice.strategies;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.venturedive.monitorservice.entity.CustomerType;
import com.venturedive.monitorservice.entity.DeliveryDetail;
import com.venturedive.monitorservice.entity.TicketPriority;

@Component
@Order(20)
public class EstimatedExceedExpectedStrategy implements TicketStrategy {
	
	public static final String MESSAGE = "Estimated delivery time is exceeding expected time";

	@Override
	public TicketPriority ticketPriority(CustomerType customerType) {
		return customerType.priority();
	}

	@Override
	public String getMessage() {
		return MESSAGE;
	}

	@Override
	public boolean test(DeliveryDetail deliveryDetail) {
		LocalDateTime scheduledTime = deliveryDetail.getScheduledTime();
		LocalTime foodPrepTime = deliveryDetail.getFoodPreparationTime();
		LocalTime destReachTime = deliveryDetail.getTimeToReachDestination();

		LocalDateTime estTimeDelivey = scheduledTime.plusHours(foodPrepTime.getHour())
				.plusMinutes(foodPrepTime.getMinute()).plusSeconds(foodPrepTime.getSecond())
				.plusHours(destReachTime.getHour()).plusMinutes(destReachTime.getMinute())
				.plusSeconds(destReachTime.getSecond());

		return Duration.between(estTimeDelivey, deliveryDetail.getExpectedDeliveryTime()).isNegative();
	}
}
