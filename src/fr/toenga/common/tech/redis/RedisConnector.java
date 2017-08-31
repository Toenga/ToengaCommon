package fr.toenga.common.tech.redis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.toenga.common.tech.redis.setting.RedisSettings;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Main class where all interactions with Redis technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, like using the key/value storage
 * @author xMalware
 */
@Data public class RedisConnector 
{

	// RedisConnector singleton instance
	@Getter@Setter private static 	RedisConnector 								instance		= new RedisConnector();

	// Private fields
	private							GsonBuilder									gsonBuilder		= new GsonBuilder();
	private							Gson										gson			= getGsonBuilder().create();
	private							Gson										exposeGson		= getGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private 					   	ConcurrentMap<String, RedisService>			services		= new ConcurrentHashMap<>();
	
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
	
	/**
	 * Register a new service
	 * @param redisService		> Redis service
	 * @return 
	 */
	public RedisService registerService(RedisService redisService)
	{
		services.put(redisService.getName(), redisService);
		return redisService;
	}
	
	/**
	 * Unregister an existing service
	 * @param redisService		> Redis service
	 */
	public RedisService unregisterService(RedisService redisService) 
	{
		services.remove(redisService.getName());
		return redisService;
	}
	
}
