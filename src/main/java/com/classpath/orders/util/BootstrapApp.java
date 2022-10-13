package com.classpath.orders.util;

import static java.util.stream.IntStream.range;

import java.util.stream.IntStream;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.classpath.orders.model.LineItem;
import com.classpath.orders.model.Order;
import com.classpath.orders.repository.OrderJPARepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BootstrapApp {
	
	private final OrderJPARepository orderRepository;
	private Faker faker = new Faker();
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadAppData(ApplicationReadyEvent event) {
		
		range(0, 10).forEach(index -> {
			Name name = faker.name();
			Order order = Order.builder().customerName(name.firstName()).email(name.firstName()+ "@"+faker.internet().domainName())
					.price(faker.number().randomDouble(2, 4000, 8000)).build();
			
			IntStream.range(0, faker.number().numberBetween(2, 5)).forEach(value -> {
				LineItem lineItem = LineItem.builder().name(faker.commerce().productName()).qty(faker.number().numberBetween(2, 4))
						.price(faker.number().randomDouble(2, 400, 800)).build();
				order.addLineItem(lineItem);
			});
			this.orderRepository.save(order);
		});
	}

}
