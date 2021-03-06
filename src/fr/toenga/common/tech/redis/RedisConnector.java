package fr.toenga.common.tech.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.toenga.common.tech.Connector;
import fr.toenga.common.tech.redis.setting.RedisSettings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Main class where all interactions with Redis technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, like using the key/value storage
 * @author xMalware
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class RedisConnector extends Connector<RedisService>
{

	// RedisConnector singleton instance
	@Getter@Setter private static 	RedisConnector 								instance		= new RedisConnector();

	// Private fields
	private							GsonBuilder									gsonBuilder		= new GsonBuilder();
	private							Gson										exposeGson		= getGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	/**
	 * Create settings and be back with a RedisSettings object who is useful for some operations, like using it in different services
	 * @param hostnames   > hostnames, we higly recommend DNS
	 * @param port 		  > Redis Cluster port, 6379 by default
	 * @param password	  > the password of that account
	 * @param database 	  > database id which will be used
	 * @return a RedisSettings object
	 */
	public RedisSettings createSettings(String[] hostnames, int port, String password, int database, int workerThreads)
	{
		return new RedisSettings(hostnames, port, password, database, workerThreads);
	}
	
	/**
	 * Adding a new service
	 * @param name 		  	   > name of the service
	 * @param redisSettings    > credentials
	 * @return a RedisService object ready to work
	 */
	public RedisService createService(String name, RedisSettings redisSettings)
	{
		return new RedisService(name, redisSettings);
	}
	
}
