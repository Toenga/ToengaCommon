package fr.toenga.common.tech.rabbitmq.packet;

import lombok.Getter;

@Getter
public class RabbitPacket {

	private RabbitPacketMessage		rabbitMessage;
	private String		 		queue;
	private boolean 			debug;
	private RabbitPacketEncoder		encoder;
	private RabbitPacketType	type;
	
}
