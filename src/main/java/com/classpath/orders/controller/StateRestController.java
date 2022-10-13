package com.classpath.orders.controller;

import static org.springframework.boot.availability.AvailabilityChangeEvent.publish;
import static org.springframework.boot.availability.LivenessState.BROKEN;
import static org.springframework.boot.availability.LivenessState.CORRECT;
import static org.springframework.boot.availability.ReadinessState.ACCEPTING_TRAFFIC;
import static org.springframework.boot.availability.ReadinessState.REFUSING_TRAFFIC;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/state")
@RequiredArgsConstructor
public class StateRestController {
	
	private final ApplicationAvailability applicationAvailability;
	private final ApplicationEventPublisher applicationEvent;

	
	@PostMapping("/liveness")
	public Map<String, Object> livenessState() {
		LivenessState livenessState = this.applicationAvailability.getLivenessState();
		LivenessState  updatedLivenessState = livenessState == CORRECT ? BROKEN : CORRECT;
		String state = updatedLivenessState == CORRECT ? "System is functioning": "Application is not functioning";
		
		//publish the change event
		publish(applicationEvent, state, updatedLivenessState);
		
		Map<String, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("liveness", updatedLivenessState);
		responseMap.put("state", state);
		return responseMap;
	}
	
	@PostMapping("/readiness")
	public Map<String, Object> readinessState() {
		ReadinessState readinessState = this.applicationAvailability.getReadinessState();
		ReadinessState  updatedReadinessState = readinessState == ACCEPTING_TRAFFIC ? REFUSING_TRAFFIC: ACCEPTING_TRAFFIC;
		String state = updatedReadinessState == ACCEPTING_TRAFFIC ? "System is functioning": "Application is not functioning";
		
		//publish the change event
		publish(applicationEvent, state, updatedReadinessState);
		
		Map<String, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("readiness", updatedReadinessState);
		responseMap.put("state", state);
		return responseMap;
	}
	

}
