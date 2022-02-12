package com.venturedive.monitorservice.entity;

public enum CustomerType {
	VIP {
		@Override
		public TicketPriority priority() {
			return TicketPriority.CRITICAL;
		}
	},

	Loyal {
		@Override
		public TicketPriority priority() {
			return TicketPriority.HIGH;
		}
	},

	NEW {
		@Override
		public TicketPriority priority() {
			return TicketPriority.NORMAL;
		}
	};

	abstract public TicketPriority priority();
}
