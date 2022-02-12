package com.venturedive.monitorservice.entity;

public enum TicketPriority {
	CRITICAL(2), HIGH(1), NORMAL(0);

	private int priority;

	private TicketPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return String.valueOf(priority);
	}
};