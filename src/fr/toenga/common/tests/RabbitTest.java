package fr.toenga.common.tests;

import fr.toenga.common.tech.rabbitmq.RabbitConnector;
import fr.toenga.common.tech.rabbitmq.RabbitService;
import fr.toenga.common.tech.rabbitmq.RabbitSettings;

public class RabbitTest 
{

	private static String[] HOSTNAMES	= new String[] 
			{
					"localhost"
			};
	private static int		PORT		= 5672;
	private static String	USERNAME	= "root";
	private static String	VIRTUALHOST	= "rabbit";
	private static String	PASSWORD	= "defaultpassword";
	private static boolean	RECOVERY	= true;
	private static int		TIMEOUT		= 60_000;
	private static int		HEARTBEAT	= 60;

	public static void main(String[] args) 
	{
		RabbitConnector rabbitConnector = RabbitConnector.getInstance();
		RabbitSettings rabbitSettings = new RabbitSettings(HOSTNAMES, PORT, USERNAME, VIRTUALHOST, PASSWORD, RECOVERY, TIMEOUT, HEARTBEAT);
		RabbitService rabbitService = rabbitConnector.createService("default", rabbitSettings);
		rabbitConnector.registerService(rabbitService);
	}

}
