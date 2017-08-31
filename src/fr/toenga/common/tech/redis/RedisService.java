package fr.toenga.common.tech.redis;

import java.lang.reflect.Type;
import java.util.Random;

import com.google.gson.Gson;

import fr.toenga.common.tech.AutoReconnector;
import fr.toenga.common.tech.redis.setting.RedisSettings;
import fr.toenga.common.utils.data.Callback;
import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.Jedis;

@EqualsAndHashCode(callSuper = false)
@Data
public class RedisService extends AutoReconnector
{

	private		String						name;
	private		RedisSettings				credentials;
	private		Jedis						jedis;
	private		boolean						isDead;
	private		Random						random;

	public RedisService(String name, RedisSettings credentials) 
	{
		setCredentials(credentials);
		setName(name);
		setRandom(new Random());
		reconnect();
	}

	public void setAsync(String key, String value)
	{
		new Thread() 
		{
			@Override
			public void run() 
			{
				set(key, value);
			}
		}.start();
	}

	public void setAsync(String key, Object value, boolean indented)
	{
		new Thread() 
		{
			@Override
			public void run() 
			{
				set(key, value, indented);
			}
		}.start();
	}

	public void set(String key, String value) 
	{
		getJedis().set(key, value);
	}

	public void set(String key, Object value, boolean indented) 
	{
		set(key, getGson(indented).toJson(value));
	}

	public <T> void getSyncObject(String key, Class<T> clazz, Callback<T> object, boolean indented) 
	{
		getSyncString(key, new Callback<String>() 
		{
			@Override
			public void done(String result, Throwable error) 
			{
				object.done(getGson(indented).fromJson(result, clazz), null);
			}
		});
	}

	public <T> void getSyncObject(String key, Type type, Callback<T> object, boolean indented)
	{
		getSyncString(key, new Callback<String>() 
		{
			@Override
			public void done(String result, Throwable error) 
			{
				object.done(getGson(indented).fromJson(result, type), null);
			}
		});
	}

	public void getSyncString(String key, Callback<String> object)
	{
		object.done(getJedis().get(key), null);
	}

	public void getAsyncString(String key, Callback<String> object)
	{
		new Thread() 
		{
			@Override
			public void run() 
			{
				getSyncString(key, object);
			}
		}.start();
	}

	public <T> void getAsyncObject(String key, Class<T> clazz, Callback<T> object, boolean onlyExpose) 
	{
		new Thread() 
		{
			@Override
			public void run() 
			{
				getSyncObject(key, clazz, object, onlyExpose);
			}
		}.start();
	}

	public <T> void getAsyncObject(String key, Type type, Callback<T> object, boolean onlyExpose) 
	{
		new Thread() 
		{
			@Override
			public void run() 
			{
				getSyncObject(key, type, object, onlyExpose);
			}
		}.start();
	}

	public void delete(String... keys) 
	{
		getJedis().del(keys);
	}

	public void remove()
	{
		if (isDead())
		{
			Log.log(LogType.ERROR, "[RedisConnector] The service is already dead.");
			return;
		}
		long time = System.currentTimeMillis();
		setDead(true); // Set dead
		cancel(); // Cancel AutoReconnector task
		// Close channel
		try 
		{
			getJedis().close();
		}
		catch (Exception error)
		{
			Log.log(LogType.ERROR, "[RedisConnector] Something gone wrong while trying to close Redis connection.");
			error.printStackTrace();
			return;
		}
		RedisConnector.getInstance().getServices().remove(getName());
		Log.log(LogType.SUCCESS, "[RedisConnector] Redis service disconnected (" + (System.currentTimeMillis() - time) + " ms).");
	}

	private Gson getGson(boolean indented) 
	{
		RedisConnector redisConnector = RedisConnector.getInstance();
		if (indented)
		{
			return redisConnector.getExposeGson();
		}
		return redisConnector.getGson();
	}

	@Override
	public boolean isConnected() 
	{
		return getJedis() != null && getJedis().isConnected();
	}

	@Override
	public void reconnect() 
	{
		if (isDead())
		{
			return;
		}
		if (isConnected()) 
		{
			return;
		}
		try 
		{
			long time = System.currentTimeMillis();
			String[] hostnames = getCredentials().getHostnames();
			int hostnameId = getRandom().nextInt(hostnames.length);
			setJedis(new Jedis(hostnames[hostnameId], credentials.getPort()));
			getJedis().auth(getCredentials().getPassword());
			getJedis().select(getCredentials().getDatabase());
			Log.log(LogType.SUCCESS, "[RedisConnector] Successfully (re)connected to Redis service (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			Log.log(LogType.ERROR, "[RedisConnector] Unable to connect to Redis service (" + error.getMessage() + ").");
		}
	}

}
