package fr.toenga.common.tech.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RabbitListener
{

	private String				name;
	private RabbitListenerType	type;
	
	public void load()
	{
		
	}
	
}
