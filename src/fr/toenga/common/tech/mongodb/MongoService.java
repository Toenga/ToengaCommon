package fr.toenga.common.tech.mongodb;

import java.util.Random;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import fr.toenga.common.tech.AutoReconnector;
import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class MongoService extends AutoReconnector
{

	private		String						name;
	private		MongoSettings				credentials;
	private		MongoClient					mongoClient;
	private		boolean						isDead;
	private     DB							db;
	private		Random						random;

	public MongoService(String name, MongoSettings credentials)
	{
		this.setCredentials(credentials);
		this.setName(name);
		this.setRandom(new Random());
		// Connect
		this.reconnect();
	}

	@SuppressWarnings("deprecation")
	private void loadMongo()
	{
		try
		{
			String[] hostnames = getCredentials().getHostnames();
			int hostnameId = getRandom().nextInt(hostnames.length);
			setMongoClient(new MongoClient(hostnames[hostnameId], getCredentials().getPort()));
			setDb(client().getDB(getCredentials().getDatabase()));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DB db()
	{
		return this.getDb();
	}

	public MongoClient client() 
	{
		return this.getMongoClient();
	}

	public void remove() 
	{
		try {
			long time = System.currentTimeMillis();
			db().getMongo().close();
			Log.log(LogType.SUCCESS, "[MongoService] Closed MongoDB connection (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception exception) 
		{
			Log.log(LogType.ERROR, "[MongoService] Something gone wrong while trying to close MongoDB connection.");
			exception.printStackTrace();
		}
		MongoConnector.getInstance().getServices().remove(this.getName());
		this.setDead(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isConnected() 
	{
		// Disgusting method, TODO find something better
		try 
		{
			db().getMongo().getDatabaseNames();
			return true;
		} 
		catch (Exception exception)
		{
			return false;
		}
	}

	@Override
	public void reconnect() {
		if (isConnected())
		{
			return;
		}
		try 
		{
			long time = System.currentTimeMillis();
			this.loadMongo();
			Log.log(LogType.SUCCESS, "[MongoService] Successfully (re)connected to MongoDB service (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			Log.log(LogType.ERROR, "[MongoService] Unable to connect to MongoDB service (" + error.getMessage() + ").");
		}
	}

}
