package fr.toenga.common.tech.mongodb;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;

import fr.toenga.common.tech.mongodb.setting.MongoSettings;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Main class where all interactions with MongoDB technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, like using the key/value storage
 * @author xMalware
 */
@Data public class MongoConnector 
{

	// MongoConnector singleton instance
	@Getter@Setter private static 	MongoConnector 								instance		= new MongoConnector();

	// Private fields
	private							Gson										gson			= new Gson();
	private 					   	ConcurrentMap<String, MongoService>			services		= new ConcurrentHashMap<>();

	/**
	 * Create credentials and be back with a MongoCredentials object which is useful for some operations, like using it in different services
	 * @param name 		  > the name of credentials instance
	 * @param hostname    > hostname, we higly recommend DNS
	 * @param port 		  > Mongo Cluster port, 27017 by default
	 * @param password	  > the password of that account
	 * @return a MongoCredentials object
	 */
	public MongoSettings createSettings(String[] hostnames, int port, String username, String database, String password) 
	{
		return new MongoSettings(hostnames, port, username, database, password);
	}
	
	/**
	 * Adding a new service
	 * @param name 		  		> name of the service
	 * @param MongoSettings     > credentials
	 * @return a MongoService ready to work
	 */
	public MongoService newService(String name, MongoSettings MongoCredentials) 
	{
		return new MongoService(name, MongoCredentials);
	}

	/**
	 * Register a new service
	 * @param mongoService		> MongoDB service
	 * @return 
	 */
	public MongoService registerService(MongoService mongoService)
	{
		services.put(mongoService.getName(), mongoService);
		return mongoService;
	}

	/**
	 * Unregister an existing service
	 * @param mongoService		> MongoDB service
	 */
	public MongoService unregisterService(MongoService mongoService) 
	{
		services.remove(mongoService.getName());
		return mongoService;
	}

}
