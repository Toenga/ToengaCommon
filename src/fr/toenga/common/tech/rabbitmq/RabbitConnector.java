package fr.toenga.common.tech.rabbitmq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

import lombok.Getter;

/**
 * Main class where all interactions with RabbitMQ technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, such as listen a queue, an exchange or build and send a queued message
 * @author xMalware
 */
@Getter
public class RabbitConnector 
{
	
	// Singleton instance of RabbitConnector
	@Getter private static RabbitConnector	instance = new RabbitConnector();
	
	// Private fields
	private Map<String, RabbitService>		services	= new ConcurrentHashMap<>();
	private	Gson							gson		= new Gson();

	/**
	 * Create settings and be back with a RabbitSettings object which is useful for some operations, like using it for different services
	 * @param hostnames   > hostnames, we highly recommend DNS
	 * @param port 		  > RabbitMQ Cluster port, 5762 by default
	 * @param username    > username of that account
	 * @param virtualHost > virtual host where the account will be connected to
	 * @param password	  > the password of that account
	 * @return a RabbitSettings object
	 */
	public RabbitSettings createSettings(String[] hostnames, int port, String username, String virtualHost, String password, boolean automaticRecovery, int connectionTimeout, int requestedHeartbeat, int workerThreads)
	{
		return new RabbitSettings(hostnames, port, username, virtualHost, password, automaticRecovery, connectionTimeout, requestedHeartbeat, workerThreads);
	}
	
	/**
	 * Create a new service
	 * @param serviceName 		> name of the service
	 * @param rabbitSettings	> credentials
	 * @return a RabbitService object
	 */
	public RabbitService createService(String serviceName, RabbitSettings rabbitSettings)
	{
		return new RabbitService(serviceName, rabbitSettings);
	}
	
	/**
	 * Register a new service
	 * @param rabbitService		> RabbitMQ service
	 */
	public void registerService(RabbitService rabbitService)
	{
		services.put(rabbitService.getName(), rabbitService);
	}
	
	/**
	 * Unregister an existing service
	 * @param rabbitService		> RabbitMQ service
	 */
	public void unregisterService(RabbitService rabbitService) 
	{
		services.remove(rabbitService.getName());
	}
	
}
