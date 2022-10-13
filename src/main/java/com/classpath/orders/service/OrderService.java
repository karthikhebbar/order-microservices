package com.classpath.orders.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.classpath.orders.channel.OutputChannel;
import com.classpath.orders.event.Event;
import com.classpath.orders.event.OrderEvent;
import com.classpath.orders.model.Order;
import com.classpath.orders.repository.OrderJPARepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	
	private final OrderJPARepository orderRepository;
	private final WebClient webClient;
	private final OutputChannel source;
	
	@CircuitBreaker(name = "inventoryservice", fallbackMethod = "fallback")
	public Order save(Order order) {
		//invoke the post endpoint for updating the inventory
		/*
		 * long response = this.webClient .post() .uri("/api/inventory")
		 * .exchangeToMono(res -> { if (res.statusCode().equals(HttpStatus.OK)) { return
		 * res.bodyToMono(Long.class); } else { return
		 * res.createException().flatMap(Mono::error); } }) .block();
		 * log.info("Response from inventory microservice :: {}", response);
		 */
		//send the message to the broker
		OrderEvent orderAccepted = new OrderEvent(order, Event.ORDER_ACCEPTED);
		Message<OrderEvent> payload = MessageBuilder.withPayload(orderAccepted).build();
		this.source.orderOutput().send(payload);
		
		return this.orderRepository.save(order);
	}
	
	private Order fallback(Throwable exception) {
		log.error("exception while communicating with inventory service:: switching to fallback");
		return Order.builder().build();
	}
	
	public Set<Order> fetchAllOrders(){
		return new HashSet<>(this.orderRepository.findAll());
	}
	
	public Order fetchOrderById(long orderId) {
		return this.orderRepository.findById(orderId).orElseThrow();
	}
	
	public void deleteOrderById(long orderId) {
		this.orderRepository.deleteById(orderId);
	}

}
