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
	private		MongoCredentials			credentials;
	private		MongoClient					mongoClient;
	private		boolean						isDead;
	private     DB							db;
	private		Random						random;

	public MongoService(String name, MongoCredentials credentials)
	{
		this.setCredentials(credentials);
		this.setName(name);
		this.setRandom(new Random());
		// Connect
		this.loadMongo();
		MongoConnector.getInstance().getServices().put(this.getName(), this);
		System.out.println("[MongoConnector] Registered new service (" + name + ")");
	}

	@SuppressWarnings("deprecation")
	private void loadMongo()
	{
		try
		{
			int hostnameId = getRandom().nextInt(getCredentials().getHostnames().size());
			setMongoClient(new MongoClient(getCredentials().getHostnames().get(hostnameId), getCredentials().getPort()));
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
		System.out.println("[MongoConnector] Unregistered service! (" + this.getName() + ")");
		try {
			db().getMongo().close();
		}
		catch(Exception exception) 
		{
			System.out.println("[MongoConnector] Error during the disconnection: " + exception.getMessage() + ")");
		}
		MongoConnector.getInstance().getServices().remove(this.getName());
		this.setDead(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isConnected() {
		// Disgusting method, TODO find something better
		try {
			db().getMongo().getDatabaseNames();
			return true;
		} catch (Exception exception) {
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
			Log.log(LogType.ERROR, "Successfully (re)connected to MongoDB service. (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			Log.log(LogType.ERROR, "Unable to connect to MongoDB service. (" + error.getMessage() + ").");
		}
	}

}
