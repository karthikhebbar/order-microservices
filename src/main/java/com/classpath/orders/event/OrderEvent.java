package com.classpath.orders.event;

import com.classpath.orders.model.Order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderEvent {
	
	private final Order order;
	private final Event event;
}
