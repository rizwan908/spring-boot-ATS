package com.venturedive.monitorservice.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.venturedive.monitorservice.entity.CustomerType;
import com.venturedive.monitorservice.entity.DeliveryDetail;
import com.venturedive.monitorservice.entity.DeliveryStatus;
import com.venturedive.monitorservice.entity.Ticket;
import com.venturedive.monitorservice.repository.DeliveryDetailsRepository;
import com.venturedive.monitorservice.repository.TicketRepository;
import com.venturedive.monitorservice.strategies.EstimatedExceedExpectedStrategy;
import com.venturedive.monitorservice.strategies.ExpectedTimePassedStrategy;

@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DeilveryServiceIntegrationTest {

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private DeliveryDetailsRepository deliveryDetailsRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Test
	void testProccessDeliveryDetails_ESTGreaterThanEXPTime() {
		LocalDateTime now = LocalDateTime.now();

		DeliveryDetail deliveryDetail = new DeliveryDetail();
		deliveryDetail.setCustomerType(CustomerType.VIP);
		deliveryDetail.setDeliveryId(0);
		deliveryDetail.setDeliveryStatus(DeliveryStatus.ORDER_RECEIVED);
		deliveryDetail.setExpectedDeliveryTime(now.plusMinutes(30));
		deliveryDetail.setFoodPreparationTime(LocalTime.of(0, 15, 20));
		deliveryDetail.setOrderPlacementTime(now);
		deliveryDetail.setRiderRating(9);
		deliveryDetail.setScheduledTime(now.plusMinutes(3));
		deliveryDetail.setTimeToReachDestination(LocalTime.of(0, 20, 20));

		deliveryDetailsRepository.save(deliveryDetail);

		deliveryService.processDeliveryDetails(
				deliveryDetailsRepository.findAllByDeliveryStatusNot(DeliveryStatus.ORDER_DELIVERED));

		List<Ticket> tickets = ticketRepository.findAll();

		Assertions.assertFalse(tickets.isEmpty());
		Assertions.assertEquals(1, tickets.size());
		Assertions.assertEquals(EstimatedExceedExpectedStrategy.MESSAGE, tickets.get(0).getMessage());
	}

	@Test
	void testProccessDeliveryDetails_CURRGreaterThanEXPTime() {
		LocalDateTime now = LocalDateTime.now();

		DeliveryDetail deliveryDetail = new DeliveryDetail();
		deliveryDetail.setCustomerType(CustomerType.VIP);
		deliveryDetail.setDeliveryId(0);
		deliveryDetail.setDeliveryStatus(DeliveryStatus.ORDER_RECEIVED);
		deliveryDetail.setExpectedDeliveryTime(now.minusMinutes(10));
		deliveryDetail.setFoodPreparationTime(LocalTime.of(0, 15, 20));
		deliveryDetail.setOrderPlacementTime(now);
		deliveryDetail.setRiderRating(9);
		deliveryDetail.setScheduledTime(now.plusMinutes(31));
		deliveryDetail.setTimeToReachDestination(LocalTime.of(0, 20, 20));

		deliveryDetailsRepository.save(deliveryDetail);

		deliveryService.processDeliveryDetails(
				deliveryDetailsRepository.findAllByDeliveryStatusNot(DeliveryStatus.ORDER_DELIVERED));

		List<Ticket> tickets = ticketRepository.findAll();

		Assertions.assertFalse(tickets.isEmpty());
		Assertions.assertEquals(1, tickets.size());
		Assertions.assertEquals(ExpectedTimePassedStrategy.MESSAGE, tickets.get(0).getMessage());
	}

}
