package com.classpath.orders.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutputChannel {
	
	/**
	 * Name of the output channel.
	 */
	String ORDER = "orders";

	/**
	 * @return output channel
	 */
	@Output(OutputChannel.ORDER)
	MessageChannel orderOutput();

}
