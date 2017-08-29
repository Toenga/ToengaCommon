package fr.toenga.common.tech.rabbitmq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

public class RabbitConnector 
{
	
	@Getter private static RabbitConnector	instance = new RabbitConnector();
	
	private Map<String, RabbitService>		services = new ConcurrentHashMap<>();
	
	public void registerService(RabbitService rabbitService)
	{
		services.put(rabbitService.getName(), rabbitService);
	}
	
	public RabbitService createService(String serviceName, RabbitSettings rabbitSettings)
	{
		return new RabbitService(serviceName, rabbitSettings);
	}
	
}
