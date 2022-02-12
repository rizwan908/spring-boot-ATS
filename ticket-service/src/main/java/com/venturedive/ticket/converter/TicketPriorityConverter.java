package com.venturedive.ticket.converter;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.venturedive.ticket.entity.TicketPriority;

@Converter
public class TicketPriorityConverter implements AttributeConverter<TicketPriority, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TicketPriority attribute) {
		return Integer.parseInt(attribute.toString());
	}

	@Override
	public TicketPriority convertToEntityAttribute(Integer dbData) {
		return Arrays.stream(TicketPriority.values()).filter(d -> d.toString().equals(dbData.toString())).findFirst()
				.orElseThrow();
	}
}
