package com.venturedive.monitorservice.converter;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.venturedive.monitorservice.entity.DeliveryStatus;

@Converter
public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, String> {

	@Override
	public String convertToDatabaseColumn(DeliveryStatus attribute) {
		return attribute.toString();
	}

	@Override
	public DeliveryStatus convertToEntityAttribute(String dbData) {
		return Arrays.stream(DeliveryStatus.values()).filter(s -> s.toString().equals(dbData)).findFirst()
				.orElseThrow();

	}
}
