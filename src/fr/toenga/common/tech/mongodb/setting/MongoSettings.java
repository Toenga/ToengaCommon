package fr.toenga.common.tech.mongodb.setting;

import java.util.Random;

import com.mongodb.MongoClient;

import fr.toenga.common.tech.Settings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A MongoCredentials object
 * @author xMalware
 *
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class MongoSettings extends Settings
{

	private String[]				hostnames;
	private	int						port;
	private String					username;
	private	String					password;
	private String					database;
	private int						workerThreads;
	
	@Override
	public MongoClient toFactory() {
		try
		{
			String[] hostnames = getHostnames();
			int hostnameId = new Random().nextInt(hostnames.length);
			MongoClient mongoClient = new MongoClient(hostnames[hostnameId], getPort());
			return mongoClient;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
