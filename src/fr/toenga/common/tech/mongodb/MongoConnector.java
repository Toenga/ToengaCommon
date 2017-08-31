package fr.toenga.common.tech.mongodb;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;

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
	private							Gson										gson			= new Gson(); // A Gson object
	private 					   	ConcurrentMap<String, MongoService>			services		= new ConcurrentHashMap<>(); // Services list
	private							ConcurrentMap<String, MongoCredentials>		credentials		= new ConcurrentHashMap<>(); // Credentials list

	/**
	 * Create credentials and be back with a MongoCredentials object which is useful for some operations, like using it in different services
	 * @param name 		  > the name of credentials instance
	 * @param hostname    > hostname, we higly recommend DNS
	 * @param port 		  > Mongo Cluster port, 27017 by default
	 * @param password	  > the password of that account
	 * @return a MongoCredentials object
	 */
	public MongoCredentials newCredentials(String name, int port, String username, String password, String database, String... hostnames) 
	{
		return new MongoCredentials(name, port, username, password, database, hostnames);
	}

	/**
	 * Adding a new service by using credentials (with fields)
	 * @param name 		  > the name of the service instance
	 * @param hostname    > hostname, we higly recommend DNS
	 * @param port 		  > Mongo Cluster port, 65525 by default
	 * @param password	  > the password of that account
	 * @return a MongoService ready to work
	 */
	public MongoService newService(String name, int port, String username, String password, String database, String... hostnames) 
	{
		return newService(name, newCredentials(name, port, username, password, database, hostnames));
	}

	/**
	 * Adding a new service by using credentials (with the given MongoCredentials object)
	 * @param name 		  		> the name of the service instance
	 * @param MongoCredentials > the credentials, which will be used by the service
	 * @return a MongoService ready to work
	 */
	public MongoService newService(String name, MongoCredentials MongoCredentials) 
	{
		return new MongoService(name, MongoCredentials);
	}

	/**
	 * Getting credentials with a credentials marked name
	 * @param name > the name which will serve to fetch the requested credentials
	 * @return a MongoCredentials object
	 */
	public MongoCredentials getCredentials(String name) 
	{
		return this.getCredentials().get(name);
	}

	/**
	 * Getting all registered Mongo credentials
	 * @return a Collection of MongoCredentials objects
	 */
	public Collection<MongoCredentials> getMongoCredentials() 
	{
		return this.getCredentials().values();
	}

	/**
	 * Getting a service with a service marked name
	 * @param name > the name which will serve to fetch the requested service
	 * @return a MongoService ready to work
	 */
	public MongoService getService(String name) 
	{
		return this.getServices().get(name);
	}


	/**
	 * Getting all registered Mongo service
	 * @return a Collection of MongoService objects
	 */
	public Collection<MongoService> getMongoServices() 
	{
		return this.getServices().values();
	}

	/**
	 * Removing a service
	 * @param name > service name
	 */
	public void removeService(String name) 
	{
		if (this.getService(name) != null) 
		{
			this.getService(name).remove();
		}
	}

	/**
	 * Removing credentials
	 * @param name > credentials marked name
	 */
	public void removeCredentials(String name) 
	{
		if (this.getCredentials(name) != null)
		{
			this.getCredentials(name).remove();
		}
	}

}
