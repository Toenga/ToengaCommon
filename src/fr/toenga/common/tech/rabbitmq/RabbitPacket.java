package fr.toenga.common.tech.rabbitmq;

import lombok.Getter;

@Getter
public class RabbitPacket {

	private RabbitMessage		rabbitMessage;
	private String		 		queue;
	private boolean 			debug;
	private RabbitEncoder		encoder;
	private RabbitPacketType	type;
	
}
