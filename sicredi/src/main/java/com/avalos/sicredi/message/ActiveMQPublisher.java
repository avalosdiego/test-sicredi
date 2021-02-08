package com.avalos.sicredi.message;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQPublisher {

	private static final Logger log = LoggerFactory.getLogger(ActiveMQPublisher.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Queue queue;

	public String publicarMensagem(String conteudo) {
		jmsTemplate.convertAndSend(queue, conteudo);
		log.info("Mensagem publicada: " + conteudo);
		return "Sucesso";
	}

}
