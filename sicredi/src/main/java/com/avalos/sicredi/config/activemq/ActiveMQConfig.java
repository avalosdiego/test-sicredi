package com.avalos.sicredi.config.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@Configuration
public class ActiveMQConfig {

	@Bean
	public Queue createQueue() {
		return new ActiveMQQueue("local.inmemory.queue");
	}

}