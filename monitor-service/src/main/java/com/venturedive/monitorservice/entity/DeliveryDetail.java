package com.venturedive.monitorservice.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.venturedive.monitorservice.converter.DeliveryStatusConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeliveryDetail {

	@Id
	private int deliveryId;
	@Enumerated(EnumType.STRING)
	private CustomerType customerType;
	@Convert(converter = DeliveryStatusConverter.class)
	private DeliveryStatus deliveryStatus;
	private LocalDateTime expectedDeliveryTime;
	private int currentDistanceFromDestination;
	private LocalTime timeToReachDestination;
	private double riderRating;
	private LocalTime foodPreparationTime;
	private LocalDateTime orderPlacementTime;
	private LocalDateTime scheduledTime;
}
