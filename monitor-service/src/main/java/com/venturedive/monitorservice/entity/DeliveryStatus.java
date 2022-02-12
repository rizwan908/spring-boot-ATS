package com.venturedive.monitorservice.entity;

public enum DeliveryStatus {

	ORDER_RECEIVED("Order Received"), ORDER_PREPARING("Order Preparing"), ORDER_PICKEDUP("Order Pickedup"),
	ORDER_DELIVERED("Order Delivered");

	private String status;

	private DeliveryStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}
}
