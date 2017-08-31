package fr.toenga.common.tech.mongodb;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * A MongoCredentials object who will allow 
 * @author xMalware
 *
 */
@Getter @Setter public class MongoCredentials
{

	private String					name;
	private String					username;
	private	List<String>			hostnames;
	private	int						port;
	private	String					password;
	private String					database;

	public MongoCredentials(String name, int port, String username, String password, String database, String... hostnames)
	{
		this.setName(name);
		this.setUsername(username);
		this.setHostnames(Arrays.asList(hostnames));
		this.setPort(port);
		this.setPassword(password);
		this.setDatabase(database);
		MongoConnector.getInstance().getCredentials().put(this.getName(), this);
		System.out.println("[MongoConnector] Registered new credentials! (" + name + ")");
	}

	public void remove()
	{
		System.out.println("[MongoConnector] Unregistered credentials (" + this.getName() + ")");
		MongoConnector.getInstance().getCredentials().remove(this.getName());
		for (MongoService MongoService : MongoConnector.getInstance().getServices().values())
		{
			if (MongoService.getCredentials().equals(this))
			{
				MongoService.remove();
			}
		}
	}

}
