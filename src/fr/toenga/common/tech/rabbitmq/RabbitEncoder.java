package fr.toenga.common.tech.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RabbitEncoder {

	UTF8("UTF-8");
	
	private String name;
	
}
