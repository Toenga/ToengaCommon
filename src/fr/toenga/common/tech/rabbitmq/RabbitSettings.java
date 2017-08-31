package fr.toenga.common.tech.rabbitmq;

import java.util.Random;

import com.rabbitmq.client.ConnectionFactory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RabbitSettings 
{

	private String[]	hostnames;
	private int			port;
	private String		username;
	private String		virtualHost;
	private String		password;
	private boolean		automaticRecovery;
	private int			connectionTimeout;
	private int			requestedHeartbeat;
	private int			workerThreads			= 32;

	ConnectionFactory getFactory() 
	{
		ConnectionFactory connectionFactory = new ConnectionFactory();
		Random random = new Random();
		connectionFactory.setHost(getHostnames()[random.nextInt(getHostnames().length)]);
		connectionFactory.setPort(getPort());
		connectionFactory.setUsername(getUsername());
		connectionFactory.setVirtualHost(getVirtualHost());
		connectionFactory.setPassword(getPassword());
		connectionFactory.setAutomaticRecoveryEnabled(isAutomaticRecovery());
		connectionFactory.setConnectionTimeout(getConnectionTimeout());
		connectionFactory.setRequestedHeartbeat(getRequestedHeartbeat());
		return connectionFactory;
	}

}
